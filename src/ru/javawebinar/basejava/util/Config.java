package ru.javawebinar.basejava.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private static final Config CONFIG = new Config();
    private static final Properties properties = new Properties();
    private static final Path directory;


    static {
        Path confPath = Paths.get("config/resume.properties");
        try (InputStream inputStream = Files.newInputStream(confPath)) {
            properties.load(inputStream);
            directory = Paths.get(properties.getProperty("dir.storage"));
        } catch (IOException e) {
            throw new IllegalStateException("Configuration of app has been broken.", e);
        }
    }

    private Config() {

    }

    public static Config get() {
        return CONFIG;
    }

    public static Path getDirectory() {
        return directory;
    }
}
