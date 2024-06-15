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

/**
 * Configuración de Firebase para la aplicación.
 *
 * <p>Esta clase configura la integración de Firebase en la aplicación utilizando los valores
 * especificados en las propiedades de configuración. Proporciona beans para inicializar Firebase,
 * el cliente de almacenamiento de Firebase y el cliente de almacenamiento de Google Cloud.</p>
 *
 * <p>Para usar esta configuración, asegúrate de tener el archivo de configuración de Firebase
 * (por ejemplo, `images-firebase.json`) en la ruta especificada y de haber configurado las
 * propiedades de `firebase.config.path` en tu archivo de configuración de Spring.</p>
 *
 * @see Configuration
 * @see FirebaseApp
 * @see FirebaseOptions
 * @see GoogleCredentials
 * @see StorageClient
 * @see Storage
 * @see StorageOptions
 * @author john-lopez
 */
@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    /**
     * Inicializa la aplicación de Firebase.
     *
     * <p>Este método lee el archivo de configuración de Firebase desde la ruta especificada y
     * utiliza las credenciales para inicializar {@link FirebaseApp}.</p>
     *
     * @return una instancia de {@link FirebaseApp} inicializada.
     * @throws IOException si ocurre un error al leer el archivo de configuración.
     */
    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException  {
        //"/app/src/main/resources/images-firebase.json"
        //firebaseConfigPath
        try {
            FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    return FirebaseApp.initializeApp(options);
                }
        
                return FirebaseApp.getInstance();
        } catch (IOException e) {
            throw new RuntimeException("Error al inicializar Firebase", e);
        }
    }

    /**
     * Proporciona un cliente de almacenamiento de Firebase.
     *
     * <p>Este método devuelve una instancia de {@link StorageClient} utilizando la aplicación
     * de Firebase inicializada.</p>
     *
     * @param firebaseApp la aplicación de Firebase inicializada.
     * @return una instancia de {@link StorageClient}.
     */
    @Bean
    public StorageClient storageClient(FirebaseApp firebaseApp) {
        return StorageClient.getInstance(firebaseApp);
    }

    /**
     * Proporciona un cliente de almacenamiento de Google Cloud.
     *
     * <p>Este método devuelve una instancia de {@link Storage} que se utiliza para interactuar
     * con Google Cloud Storage.</p>
     *
     * @return una instancia de {@link Storage}.
     */
    @Bean
    public Storage googleStorage() {
        return StorageOptions.getDefaultInstance().getService();
    }

}
