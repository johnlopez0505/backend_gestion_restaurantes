package com.john.backend_gestion_restaurantes.configuracion;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;


@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException  {
        //"/app/src/main/resources/images-firebase.json"
        FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public StorageClient storageClient(FirebaseApp firebaseApp) {
        return StorageClient.getInstance(firebaseApp);
    }

    @Bean
    public Storage googleStorage() {
        return StorageOptions.getDefaultInstance().getService();
    }

    // @Bean
    // public Storage storage() throws IOException {
    //     return StorageOptions.newBuilder()
    //             .setCredentials(GoogleCredentials.fromStream(new FileInputStream("./gestion-reservas-firebase-adminsdk-wv09k-e14e5f5603.json")))
    //             .build()
    //             .getService();
    // }

}
