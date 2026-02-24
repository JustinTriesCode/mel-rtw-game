package com.justintriescode.mellysgame.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;

import com.justintriescode.mellysgame.game.PlayerProfile;

public class DataManager {
    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule()); 
    private static final File SAVE_FILE = new File("savegame.json");

    public static void save(PlayerProfile profile) throws IOException {
        mapper.writeValue(SAVE_FILE, profile);
    }

    public static PlayerProfile load() {
        if (!SAVE_FILE.exists()) return new PlayerProfile();
        try {
            return mapper.readValue(SAVE_FILE, PlayerProfile.class);
        } catch (IOException e) {
            return new PlayerProfile();
        }
    }
}
