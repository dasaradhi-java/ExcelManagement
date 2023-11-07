package com.excelManagement.Entity;

import java.util.List;

public class AddProductFileNames {
	private List<String> fileNames;

	public AddProductFileNames(List<String> fileNames) {
		super();
		this.fileNames = fileNames;
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public AddProductFileNames() {
		super();
	}

}
