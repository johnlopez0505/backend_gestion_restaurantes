package com.john.backend_gestion_restaurantes.servicios.calificacion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ImagenService {
 
    public  String saveImage(String base64Image) throws IOException {

        final String IMAGENES_DIRECTORY = "src/main/resources/stactic/imagenes/";

        byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
         // Verificar la extensión de la imagen
         String[] parts = base64Image.split(";");
         String extension = parts[0].split("/")[1].toUpperCase();
         if (!extension.equals("PNG") && !extension.equals("JPG") && !extension.equals("JPEG")) {
             throw new IllegalArgumentException("Formato de imagen no permitido, solo se admiten PNG, JPG y JPEG");
         }
        String fileName = UUID.randomUUID().toString() + "." + extension.toLowerCase();
        System.out.println("filename image: " + fileName);
        File file = new File(IMAGENES_DIRECTORY + fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(imageBytes);
        outputStream.close();
        return fileName;
    }
}
