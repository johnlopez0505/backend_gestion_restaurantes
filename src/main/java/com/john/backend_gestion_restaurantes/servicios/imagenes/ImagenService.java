package com.john.backend_gestion_restaurantes.servicios.imagenes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.john.backend_gestion_restaurantes.seguridad.exepciones.ImageSaveException;

@Service
public class ImagenService {

    @Value("${file.upload-dir}")
    private String imagenesDirectorio;

 
    public  String saveImage(String base64Image) throws IOException {

         if (base64Image == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La imagen no puede ser nula");
        }
    
        byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
         // Verificar la extensión de la imagen
         String[] parts = base64Image.split(";");
         String extension = parts[0].split("/")[1].toUpperCase();
         if (!extension.equals("PNG") && !extension.equals("JPG") && !extension.equals("JPEG")) {
             throw new IllegalArgumentException("Formato de imagen no permitido, solo se admiten PNG, JPG y JPEG");
         }
        String fileName = UUID.randomUUID().toString() + "." + extension.toLowerCase();
        System.out.println("filename image: " + fileName);
        File directory = new File(imagenesDirectorio);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(imageBytes);
        }catch (IOException e) {
            throw new ImageSaveException("Error al guardar la imagen", e);
        }
        return fileName;
    }
}
