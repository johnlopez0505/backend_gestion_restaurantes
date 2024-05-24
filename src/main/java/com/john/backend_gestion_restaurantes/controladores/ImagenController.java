package com.john.backend_gestion_restaurantes.controladores;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class ImagenController {

    @Value("${file.upload-dir}")
    private String storageDirectoryPath; // Ruta absoluta dentro del contenedor

    @GetMapping("/imagenes/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
        try {
             // Construir la ruta completa del archivo
          
            String filePath = storageDirectoryPath + filename;
            System.out.println("filePath " + filePath);
            Path path = Paths.get(filePath);
            // Verificar la existencia del archivo
            if (Files.exists(path) && !Files.isDirectory(path)) {
                MediaType mediaType = getMediaTypeForFileName(filename);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(mediaType);
                // Construir y devolver la respuesta con el archivo
                return ResponseEntity.ok().contentType(mediaType).body(Files.readAllBytes(path));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public static MediaType getMediaTypeForFileName(String fileName) {
        Map<String, MediaType> mediaTypeMap = new HashMap<>();
        mediaTypeMap.put("png", MediaType.IMAGE_PNG);
        mediaTypeMap.put("jpg", MediaType.IMAGE_JPEG);
        mediaTypeMap.put("jpeg", MediaType.IMAGE_JPEG);
        mediaTypeMap.put("gif", MediaType.IMAGE_GIF);

        String[] parts = fileName.split("\\.");
        String extension = parts.length > 1 ? parts[parts.length - 1] : "";
        return mediaTypeMap.getOrDefault(extension.toLowerCase(), MediaType.APPLICATION_OCTET_STREAM);
    }
    
    
}



// class MediaTypeUtils {

//     private static final Map<String, MediaType> mediaTypeMap;

//     static {
//         mediaTypeMap = new HashMap<>();
//         mediaTypeMap.put("png", MediaType.IMAGE_PNG);
//         mediaTypeMap.put("jpg", MediaType.IMAGE_JPEG);
//         mediaTypeMap.put("jpeg", MediaType.IMAGE_JPEG);
//         mediaTypeMap.put("gif", MediaType.IMAGE_GIF);
//     }

//     public static MediaType getMediaTypeForFileName(String fileName) {
//         String[] parts = fileName.split("\\.");
//         String extension = parts.length > 1 ? parts[parts.length - 1] : "";
//         return mediaTypeMap.getOrDefault(extension.toLowerCase(), MediaType.APPLICATION_OCTET_STREAM);
//     }
// }