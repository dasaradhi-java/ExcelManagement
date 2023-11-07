package com.excelManagement.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.excelManagement.Entity.GetLogs;

@Service
public class ProductService {
	@Value("${addProductPath}")
	private String addProduct;
	
	@Value("${excelFilePath}")
	private String excelPath;
	private final JdbcTemplate jdbcTemplate;

	public ProductService(JdbcTemplate jdbcTemplate) {

		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> getStoreDetails() {

		String sql = "SELECT * FROM StoreManager";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
		return queryForList;
	}

	public List<GetLogs> getLogs(int id) throws IOException {
		String sql = "SELECT * FROM logs WHERE thread_id = " + id;

		List<GetLogs> retlist = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(GetLogs.class));

		return retlist;
	}

	public void moveFiles(String sourceFolderPath, String destinationFolderPath, List<String> filenames)
			throws IOException {
		Path sourceDirectory = Path.of(sourceFolderPath);
		Path destinationDirectory = Path.of(destinationFolderPath);

		// Verify if both source and destination directories exist
		if (Files.exists(sourceDirectory) && Files.exists(destinationDirectory)) {
			for (String filename : filenames) {
				Path sourceFile = sourceDirectory.resolve(filename);
				Path destinationFile = destinationDirectory.resolve(filename);

				// Check if the source file exists before moving
				if (Files.exists(sourceFile) && Files.isRegularFile(sourceFile)) {
					// Move the file to the destination directory
					Files.move(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
					System.out.println("Moved file: " + filename + " to " + destinationFile);
				} else {
					// Handle file not found or not a regular file
					System.out.println("File not found or not a regular file: " + filename);
				}
			}
		} else {
			// Handle directory not found exception
			throw new IOException("Source or destination directory not found.");
		}
	}
	 public List<String> productDetails(){
    	 List<String> fileNames = new ArrayList<>();
    	 File folder = new File(addProduct);

         if (folder.exists() && folder.isDirectory()) {
             File[] files = folder.listFiles();

             if (files != null) {
                 for (File file : files) {
                     if (file.isFile()) {
                         fileNames.add(file.getName());
                     }
                 }
             }
         }

         return fileNames;
     }
	 
	 public int updateStatusById(Long id, String status) {
	        String sql = "UPDATE AutomationProperties SET status = ? WHERE id = ?";
	        return jdbcTemplate.update(sql, status, id);
	    }
	 public List<String> productDetails(String mode) {
	        List<String> fileNames = new ArrayList<>();
	        File folder =null;

	        if ("AddStaticProduct".equals(mode)) {
	            folder = new File(excelPath+"\\AddProduct"); // Specify the folder path for mode1
	        } else if ("EditStaticProduct".equals(mode)) {
	        	folder = new File(excelPath+"\\EditProduct"); // Specify the folder path for mode2
	        } else if ("DeleteStaticProduct".equals(mode)) {
	        	folder = new File(excelPath+"\\DeleteProduct"); // Specify the default folder path
	        }
	        if (folder != null && folder.exists() && folder.isDirectory()) {
	            File[] files = folder.listFiles();
	            if (files.length>0) {
	                for (File file : files) {
	                    if ("AddStaticProduct".equals(mode) && file.isFile() && file.getName().toLowerCase().startsWith("staticproductdetails_")) {
	                    	String productProperties = extendFilepath(file);
	                    	if(productProperties.isBlank()) {
	                    		 fileNames.add(file.getName());
	                    	}else {
	                    		fileNames.add(file.getName()+"|"+productProperties);
	                    	}
	                       
	                    }else  if ("EditStaticProduct".equals(mode) && file.isFile() && file.getName().toLowerCase().startsWith("staticproducteditdetails")) {
	                        fileNames.add(file.getName());
	                    }else if ("DeleteStaticProduct".equals(mode) && file.isFile() && file.getName().toLowerCase().startsWith("staticproductdelete")) {
	                        fileNames.add(file.getName());
	                    }
	                }
	            }
	        }

	        return fileNames;
	    }
	 private String extendFilepath(File file) {
			String fileprefix = file.getName().toLowerCase().replace("staticproductdetails_", "");
			File f = new File(excelPath+"\\AddProduct\\StaticProductPropertyDetails_"+fileprefix);
			if(f.exists()) {
				return f.getName();
			}
			
			return "";
			
		}
}
