package com.john.backend_gestion_restaurantes.servicios.imagenes;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;


@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {
        ClassPathResource serviceAccountResource = new ClassPathResource(
            "src/main/resources/gestion-de-restaurantes-firebase-adminsdk-ujawi-7f4fcab55c.json");
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccountResource.getInputStream());

        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        return FirebaseApp.initializeApp(firebaseOptions);
    }
    
    @Bean
    public Storage storage() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
