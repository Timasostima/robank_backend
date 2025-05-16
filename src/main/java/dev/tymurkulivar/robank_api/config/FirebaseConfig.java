package dev.tymurkulivar.robank_api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.Map;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void initializeFirebase() {
        try {
            Map<String, String> env = System.getenv();

            String projectId = getEnvVariable(env, "FIREBASE_PROJECT_ID");
            String privateKeyId = getEnvVariable(env, "FIREBASE_PRIVATE_KEY_ID");
            String privateKey = getEnvVariable(env, "FIREBASE_PRIVATE_KEY").replace("\\n", "\n");
            String clientEmail = getEnvVariable(env, "FIREBASE_CLIENT_EMAIL");
            String clientId = getEnvVariable(env, "FIREBASE_CLIENT_ID");
            String firebaseX509 = getEnvVariable(env, "FIREBASE_x509");

            final String firebaseConfig = "{"
                + "\"type\": \"service_account\","
                + "\"project_id\": \"" + projectId + "\","
                + "\"private_key_id\": \"" + privateKeyId + "\","
                + "\"private_key\": \"" + privateKey + "\","
                + "\"client_email\": \"" + clientEmail + "\","
                + "\"client_id\": \"" + clientId + "\","
                + "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\","
                + "\"token_uri\": \"https://oauth2.googleapis.com/token\","
                + "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\","
                + "\"client_x509_cert_url\": \"" + firebaseX509 + "\""
                + "}";

            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(firebaseConfig.getBytes()))).build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    private String getEnvVariable(Map<String, String> env, String key) {
        String value = env.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Environment variable " + key + " is not set.");
        }
        return value;
    }
}