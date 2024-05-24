package com.john.backend_gestion_restaurantes.servicios.imagenes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import com.john.backend_gestion_restaurantes.seguridad.exepciones.ImageSaveException;


@Service
public class ImagenService {

    @Value("${firebase.bucketName}")
    private String bucketName;

    @Value("${file.upload-dir}")
    private String imagenesDirectorio;

    @Autowired
    private Storage storage;
   

    public String saveImage(String base64Image) throws IOException {
        if (base64Image == null) {
            throw new IllegalArgumentException("La imagen no puede ser nula");
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
        // Verificar la extensión de la imagen
        String[] parts = base64Image.split(";");
        String extension = parts[0].split("/")[1].toUpperCase();
        if (!extension.equals("PNG") && !extension.equals("JPG") && !extension.equals("JPEG")) {
            throw new IllegalArgumentException("Formato de imagen no permitido, solo se admiten PNG, JPG y JPEG");
        }
        // Generar un nombre de archivo único
        String fileName = UUID.randomUUID().toString() + "." + extension.toLowerCase();
        // Crear el blob en Firebase Storage
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, imageBytes);

        // Obtener la URL del blob recién creado
        String imageUrl = blobInfo.getMediaLink();

        File directory = new File(imagenesDirectorio);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, imageUrl);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(imageBytes);
        } catch (IOException e) {
            throw new ImageSaveException("Error al guardar la imagen", e);
        }

        return imageUrl;
    }
}
