package com.excelManagement.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {
	@Value("${excelFilePath}")
	String excelpath1;


	public List<Map<String, Object>> processExcelData(List<String> specifiedHeaders) throws IOException {
        List<Map<String, Object>> jsonData = new ArrayList<>();
         String excelPath=excelpath1;
        try (FileInputStream excelFile = new FileInputStream(new File(excelPath));
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            for (Sheet sheet : workbook) {
                Iterator<Row> rowIterator = sheet.iterator();

                // Skip headers if present
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                } else {
                    throw new IOException("Excel sheet is empty. No data found.");
                }

                // Read headers from the first row
                Row headerRow = sheet.getRow(0);
                List<String> headers = new ArrayList<>();
                for (Cell cell : headerRow) {
                    headers.add(cell.toString());
                }

                // Filter specified headers that exist in the sheet
                List<String> validSpecifiedHeaders = new ArrayList<>();
                for (String specifiedHeader : specifiedHeaders) {
                    if (headers.contains(specifiedHeader)) {
                        validSpecifiedHeaders.add(specifiedHeader);
                    }
                }

                // Read data rows and create JSON objects for specified columns
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Map<String, Object> rowData = new LinkedHashMap<>();

                    for (String header : validSpecifiedHeaders) {
                        int headerIndex = headers.indexOf(header);
                        Cell cell = row.getCell(headerIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String cellValue = cell.toString().trim(); // Trim leading and trailing spaces

                        
                            rowData.put(header, cellValue);
                        
                    }

                    // Add the rowData to jsonData only if it's not empty
                    if (!rowData.isEmpty()) {
                        jsonData.add(rowData);
                    }
                }
            }
        }
        return jsonData;
    }
    
   
    }

