package dev.tymurkulivar.robank_api.services;

import dev.tymurkulivar.robank_api.dto.PreferencesDTO;
import dev.tymurkulivar.robank_api.entities.RobankUser;
import dev.tymurkulivar.robank_api.repositories.RobankUserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final RobankUserRepository userRepository;

    public UserService(RobankUserRepository userRepository) {
        this.userRepository = userRepository;
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
}