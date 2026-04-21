package com.justintriescode.mellysgame.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;

import com.justintriescode.mellysgame.game.PlayerProfile;
import com.justintriescode.mellysgame.events.Event;
import com.justintriescode.mellysgame.events.EventLog;

/**
 * Manages the saving and loading of player data to and from the local file.
 * This class uses the Jackson library to serialize/deserialize the
 * {@link PlayerProfile}
 * object into a JSON file. This class is not instantiated.
 */
public class DataManager {
    /**
     * The Jackson ObjectMapper instance used for JSON serialization and
     * deserialization.
     */
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    /**
     * The file where the player's profile data is stored. This is located
     * in the user's home directory.
     */
    private static final File SAVE_FILE = new File(System.getProperty("user.home"), "mellysgame_save.json");

    /**
     * Saves the given {@link PlayerProfile} to the save file.
     *
     * @param profile The PlayerProfile object to be saved.
     * @throws IOException if an error occurs during file writing.
     */
    public static void save(PlayerProfile profile) throws IOException {
        try {
            mapper.writeValue(SAVE_FILE, profile);
        } catch (IOException e) {
            EventLog.getInstance().addEvent(new Event("Failed to save player profile", e));
            throw new IOException("Failed to save player profile: " + e.getMessage(), e);
        }
    }

    /**
     * Loads the {@link PlayerProfile} from the save file.
     * If the save file does not exist or an error occurs during reading,
     * a new, default PlayerProfile is returned.
     *
     * @return The loaded PlayerProfile, or a new one if loading fails.
     */
    public static PlayerProfile load() {
        if (!SAVE_FILE.exists())
            return new PlayerProfile();
        try {
            return mapper.readValue(SAVE_FILE, PlayerProfile.class);
        } catch (IOException e) {
            EventLog.getInstance().addEvent(new Event("Failed to load player profile, creating a default one", e));
            return new PlayerProfile();
        }
    }
}
