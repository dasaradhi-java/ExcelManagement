package com.excelManagement.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "Xerox Automation", description = "Xerox Automation Controller Description")

public class TestController {

	@Value("${projectPath}")
	private String projectPath;

	@GetMapping("/runTestCase/{id}/{testCase}")
	public ResponseEntity<String> runTestCase(@PathVariable String id, @PathVariable String testCase) {
		String command = "";
		StringBuilder output = new StringBuilder();
		int exitCode = -1;

		try {
			if (testCase.equalsIgnoreCase("StaticProductFetch")) {
				command = "cmd /c mvn -Dtest=StaticProductFetchingTest test -Did=" + id;
			} else if (testCase.equalsIgnoreCase("AddStaticProduct")) {
				command = "cmd /c mvn -Dtest=StaticProductCreationTest test -Did=" + id;
			} else if (testCase.equalsIgnoreCase("EditStaticProduct")) {
				command = "cmd /c mvn -Dtest=StaticProductEditTest test -Did=" + id;
			} else if (testCase.equalsIgnoreCase("DeleteStaticProduct")) {
				command = "cmd /c mvn -Dtest=StaticProductDeleteTest test -Did=" + id;
			} else {
				return new ResponseEntity<>("Invalid test case name", HttpStatus.BAD_REQUEST);
			}

			// Run the command with the specified working directory
			Process process = Runtime.getRuntime().exec(command, null, new java.io.File(projectPath));

			// Capture and print the standard output
			BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String outputLine;
			while ((outputLine = outputReader.readLine()) != null) {
				output.append(outputLine).append("\n");
				System.out.println(outputLine);
			}

			// Capture and print the error output
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String errorLine;
			while ((errorLine = errorReader.readLine()) != null) {
				output.append(errorLine).append("\n");
				System.err.println(errorLine);
			}

			// Wait for the process to complete
			exitCode = process.waitFor();

			if (exitCode == 0) {
				output.append("Maven build succeeded.");
				System.out.println("Maven build succeeded.");
			} else {
				output.append("Maven build failed with exit code: ").append(exitCode);
				System.err.println("Maven build failed with exit code: " + exitCode);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			output.append("Error occurred: ").append(e.getMessage());
		}

		return new ResponseEntity<>(output.toString(),
				exitCode == 0 ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
	}
}