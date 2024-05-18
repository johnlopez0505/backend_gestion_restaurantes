package com.john.backend_gestion_restaurantes.controladores;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class ImagenController {

    @Value("${file.upload-dir}")
    private String uploadDir;
    private final String storageDirectoryPath = "/app/imagenes"; // Ruta absoluta dentro del contenedor

    @GetMapping("/imagenes/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
        try {
             // Construir la ruta completa del archivo
          
            String filePath = uploadDir + filename;
            Path path = Paths.get(filePath);
            // Verificar la existencia del archivo
            if (Files.exists(path) && !Files.isDirectory(path)) {
                MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(filename);
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

    @PostMapping("/imagen")
    public ResponseEntity<String> uploadImage(@RequestParam("imagen") MultipartFile imagen) {
        try {
            String filePath = Paths.get(storageDirectoryPath, imagen.getOriginalFilename()).toString();
            Files.write(Paths.get(filePath), imagen.getBytes());
            return ResponseEntity.ok("Imagen subida exitosamente: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la imagen");
        }
    }

    
    
}



class MediaTypeUtils {

    private static final Map<String, MediaType> mediaTypeMap;

    static {
        mediaTypeMap = new HashMap<>();
        mediaTypeMap.put("png", MediaType.IMAGE_PNG);
        mediaTypeMap.put("jpg", MediaType.IMAGE_JPEG);
        mediaTypeMap.put("jpeg", MediaType.IMAGE_JPEG);
        mediaTypeMap.put("gif", MediaType.IMAGE_GIF);
    }

    public static MediaType getMediaTypeForFileName(String fileName) {
        String[] parts = fileName.split("\\.");
        String extension = parts.length > 1 ? parts[parts.length - 1] : "";
        return mediaTypeMap.getOrDefault(extension.toLowerCase(), MediaType.APPLICATION_OCTET_STREAM);
    }
}