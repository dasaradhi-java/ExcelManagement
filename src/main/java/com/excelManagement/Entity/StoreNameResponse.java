package com.excelManagement.Entity;

import java.util.List;
import java.util.Map;

public class StoreNameResponse {

	private List<Map<String,Object>> result;

	public List<Map<String, Object>> getResult() {
		return result;
	}

	public void setResult(List<Map<String, Object>> result) {
		this.result = result;
	}
}