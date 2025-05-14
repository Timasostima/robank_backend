package dev.tymurkulivar.robank_api.services;

import dev.tymurkulivar.robank_api.dto.PreferencesDTO;
import dev.tymurkulivar.robank_api.entities.RobankUser;
import dev.tymurkulivar.robank_api.repositories.RobankUserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final RobankUserRepository userRepository;

    public UserService(RobankUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkNewUser(String userId) {
        return userRepository.existsById(userId);
    }

    public void registerUser(RobankUser user) {
        if (userRepository.existsById(user.getUid())) {
            throw new IllegalArgumentException("User already exists");
        }
        userRepository.save(user);
    }

    public PreferencesDTO getUserPreferences(String uid) {
        RobankUser user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new PreferencesDTO(user.getPreferences());
    }

    public void updateUserPreferences(String uid, Map<String, Object> updates) {
        RobankUser user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "language" -> user.getPreferences().setLanguage((String) value);
                case "currency" -> user.getPreferences().setCurrency((String) value);
                case "theme" -> user.getPreferences().setTheme((String) value);
                case "notifications" -> user.getPreferences().setNotifications(Boolean.parseBoolean(value.toString()));
                default -> throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        });

        userRepository.save(user);
    }

    public RobankUser getUserProfile(String uid) {
        return userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public String uploadUserImageFromUrl(String userId, String imageUrl) {
        RobankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        try {
            // Open a connection to the URL
            URL url = new URL(imageUrl);
            String contentType = url.openConnection().getContentType();

            // Validate the content type
            if (!List.of("image/png", "image/jpeg", "image/webp").contains(contentType)) {
                throw new IllegalArgumentException("Unsupported file type");
            }

            // Generate a filename with the appropriate extension
            String extension = contentType.substring(contentType.lastIndexOf('/') + 1);
            String filename = UUID.randomUUID() + "." + extension;

            Path uploadDir = Paths.get("uploads/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(filename);
            try (InputStream in = url.openStream()) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            user.setPictureUrl(filename);
            userRepository.save(user);

            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Image download failed", e);
        }
    }

    public String uploadUserProfileImage(String userId, MultipartFile file) {
        RobankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();

            if (!List.of("png", "jpg", "jpeg", "webp").contains(extension)) {
                throw new IllegalArgumentException("Unsupported file type");
            }

            String filename = UUID.randomUUID() + "_" + originalFilename;
            Path uploadDir = Paths.get("uploads/");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setPictureUrl(filename);
            userRepository.save(user);

            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    public Resource getUserProfileImage(String userId) throws IOException {
        RobankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String filename = user.getPictureUrl();
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("No profile image found for this user");
        }

        Path imagePath = Paths.get("uploads").resolve(filename);
        if (!Files.exists(imagePath)) {
            throw new IllegalArgumentException("Image file not found");
        }

        return new UrlResource(imagePath.toUri());
    }
}