package com.excelManagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excelManagement.Entity.AddProductFileNames;
import com.excelManagement.Entity.GetLogs;
import com.excelManagement.Entity.StoreNameResponse;
import com.excelManagement.service.ExcelService;
import com.excelManagement.service.ProductService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/products")
@Api(tags = "Xerox Automation", description = "Xerox Automation Controller Description")
public class ExcelController {

	private final ExcelService excelService;
	private final ProductService productService;

	public ExcelController(ExcelService excelService, ProductService productService) {

		this.excelService = excelService;
		this.productService = productService;
	}

	@GetMapping("/getExcelData")
	public List<Map<String, Object>> excelToJson(@RequestParam(required = false) List<String> specifiedHeaders)
			throws IOException {
		return excelService.processExcelData(specifiedHeaders);
	}
	
	@PostMapping("/processExcel")
    public ResponseEntity<List<Map<String, Object>>> processExcel() {
       List<String> specifiedHeaders = Arrays.asList("Product Name*", "Catalog No", "Visible To Customer");

		
        try {
            List<Map<String, Object>> jsonData = excelService.processExcelData(specifiedHeaders);
            return ResponseEntity.ok(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // Internal Server Error for IO Exception
        }
    }
	
	

	@GetMapping("/getProductImageFileNames")
	public ResponseEntity<AddProductFileNames> pendingProductDetails() {
		List<String> filenames = productService.productDetails();
		AddProductFileNames nameResponse = new AddProductFileNames();
		nameResponse.setFileNames(filenames);
		return ResponseEntity.ok(nameResponse);

	}

	@GetMapping(value = "/storeDetails")
	public ResponseEntity<StoreNameResponse> getStoreDetails() {
		List<Map<String, Object>> storeNames = productService.getStoreDetails();
		StoreNameResponse nameResponse = new StoreNameResponse();
		nameResponse.setResult(storeNames);
		return ResponseEntity.ok(nameResponse);
	}

	@GetMapping("/getLogs/{id}")
	public ResponseEntity<List<GetLogs>> getLogsById(@PathVariable int id) throws IOException {
		List<GetLogs> logsData = productService.getLogs(id);
		return ResponseEntity.ok(logsData);
	}
	@PutMapping("updateStatusById/{id}")
	    public String updateStatusById(@PathVariable Long id, @RequestParam String status) {
	        int rowsAffected = productService.updateStatusById(id, status);
	        if (rowsAffected > 0) {
	            return "Status updated successfully for ID: " + id;
	        } else {
	            return "Failed to update status. ID not found: " + id;
	        }
	    }
	@GetMapping("/getExcelData/{fileName}/{mode}")
	public Map<String,Object> excelToJson(@PathVariable String fileName,@PathVariable String mode)
			throws IOException {
		List<String> specifiedHeaders =getHeaders( mode);
		List<Map<String, Object>> list = null ;//=productService.processExcelData(specifiedHeaders,fileName,mode);
		Map<String,Object> data = new HashMap<>();
		data.put("excelData", list);
		return data;
	}
	
	public List<String> getHeaders(String mode){
		List<String> specifiedHeaders = new ArrayList<>(); 
		
		
		if ("AddStaticProduct".equals(mode)) {
			specifiedHeaders.add("Product ID");
			specifiedHeaders.add("Product Name*");
			specifiedHeaders.add("Catalog No");
			specifiedHeaders.add("Short Description");
			specifiedHeaders.add("Product Detail Step");
			specifiedHeaders.add("Full Description");
        } else if ("EditStaticProduct".equals(mode)) {
    		specifiedHeaders.add("Product Name*");
    		specifiedHeaders.add("Catalog No");
    		specifiedHeaders.add("Short Description");
    		specifiedHeaders.add("Full Description");
        } else if ("DeleteStaticProduct".equals(mode)) {
        	specifiedHeaders.add("Product Name*");
        }
		
		return specifiedHeaders;
	}

}
