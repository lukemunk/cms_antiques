package com.group1_cms.cms_antiques.configurations;


import java.io.*;
import java.nio.file.*;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    
   public static void saveFile(String uploadDir, String fileName,
           MultipartFile multipartFile) throws IOException {
       Path uploadPath = Paths.get(uploadDir);


        
       if (!Files.exists(uploadPath)) {
           Files.createDirectories(uploadPath);
       }
        
       try (InputStream inputStream = multipartFile.getInputStream()) {
           Path filePath = uploadPath.resolve(fileName);
           Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
       } catch (IOException ioe) {        
           throw new IOException("Could not save image file: " + fileName, ioe);
       }      
   }
   
   public static void deleteFile(String folderPath, String imagePath) {
	   
	   Path deletePath = Paths.get(imagePath);

			try {
			    Files.delete(deletePath);
			} catch (IOException e) {
			    System.err.println("Unable to delete "
			            + deletePath.toAbsolutePath().toString()
			            + " due to...");
			    e.printStackTrace();
			}
			
			deletePath = Paths.get(folderPath);
			try {
			    Files.delete(deletePath);
			} catch (IOException e) {
			    System.err.println("Unable to delete "
			            + deletePath.toAbsolutePath().toString()
			            + " due to...");
			    e.printStackTrace();
			}
	 
   }
}
