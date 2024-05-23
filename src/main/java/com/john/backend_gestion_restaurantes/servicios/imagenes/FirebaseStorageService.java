package com.john.backend_gestion_restaurantes.servicios.imagenes;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FirebaseStorageService {
    
    private final Storage storage;
    private final Bucket bucket;
    private final String bucketName = "gestion-de-restaurantes.appspot.com"; // Nombre del bucket

    public FirebaseStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.bucket = storage.get(bucketName);
    }

    public void uploadImage(MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        String fileName = file.getOriginalFilename();
        bucket.create(fileName, fileContent, file.getContentType());
    }

    public void downloadImage(String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId);
        Path localFilePath = Paths.get("src/main/resources/static/imagenes" + fileName);
        blob.downloadTo(localFilePath);
    }
}
