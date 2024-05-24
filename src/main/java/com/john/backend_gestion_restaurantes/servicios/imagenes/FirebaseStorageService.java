package com.john.backend_gestion_restaurantes.servicios.imagenes;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    @Autowired
    private StorageClient storageClient;

    private final String bucketName = "images-70046.appspot.com"; // Nombre del bucket

    public String uploadFile(MultipartFile file) throws IOException {
        Bucket bucket = storageClient.bucket(bucketName);
        String blobName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Blob blob = bucket.create(blobName, file.getBytes(), file.getContentType());
        // Obtener la URL pública del archivo
        String publicUrl = getFileUrl(blob.getName());
        return publicUrl;
    }


    public String uploadBase64Image(String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("La imagen no puede ser nula o vacía");
        }
        // Separar la metadata del contenido de la imagen
        String[] base64Components = base64Image.split(",");
        if (base64Components.length != 2) {
            throw new IllegalArgumentException("Formato de imagen base64 no válido");
        }
         // Decodificar la imagen, manejar posibles errores de decodificación
         byte[] imageBytes;
         try {
             imageBytes = Base64.getDecoder().decode(base64Components[1].trim());
         } catch (IllegalArgumentException e) {
             throw new IllegalArgumentException("La imagen base64 está mal formada o contiene caracteres no válidos");
         }
        // Obtener la extensión del tipo de contenido
        String mimeType = base64Components[0].split(":")[1].split(";")[0];
        String extension = mimeType.split("/")[1].toLowerCase();
        // Validar la extensión de la imagen
        if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg")) {
            throw new IllegalArgumentException("Formato de imagen no permitido, solo se admiten PNG, JPG y JPEG");
        }
        // Generar un nombre de archivo único
        String fileName = UUID.randomUUID().toString() + "." + extension;
        // Subir el archivo a Firebase Storage
        Bucket bucket = storageClient.bucket(bucketName);
        Blob blob = bucket.create(fileName, imageBytes, mimeType);
         // Obtener la URL pública del archivo
        return blob.getName();
    }

    public String deleteFile(String publicUrl) throws IOException{
        // Obtener el nombre del blob de la URL pública
        String blobName = publicUrl.substring(publicUrl.lastIndexOf('/') + 1, publicUrl.indexOf("?"));
        // Eliminar el blob del Firebase Storage
        Bucket bucket = storageClient.bucket(bucketName);
        Blob blob = bucket.get(blobName);
        if (blob != null) {
            blob.delete();
            return "ok";
        } else {
            throw new IllegalArgumentException("La imagen no existe en Firebase Storage");
        }
    }

    public String updateFile(String publicUrl, String newFile) throws IOException {
        // Eliminar la imagen existente
        deleteFile(publicUrl);
        // Subir la nueva imagen
        return uploadBase64Image(newFile);
    }

    // Método para construir la URL pública del archivo
    public String getFileUrl(String blobName) {
        return "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" + blobName + "?alt=media";
    }

}
