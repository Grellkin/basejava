package ru.javawebinar.basejava.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private static final Path PROPS = Paths.get("config/resume.properties");
    private static final Properties PROPERTIES = new Properties();
    private static final Path DIRECTORY;
    private static final String URL_DB;
    private static final String USER_DB;
    private static final String PASS_DB;
    private static final Config CONFIG = new Config();

    static {
        //Resume.class.getClassLoader().getResourceAsStream(PROPS.toString());
        try (InputStream inputStream = Files.newInputStream(PROPS)) {
            PROPERTIES.load(inputStream);
            DIRECTORY = Paths.get(PROPERTIES.getProperty("dir.storage"));
            URL_DB = PROPERTIES.getProperty("db.url");
            USER_DB = PROPERTIES.getProperty("db.user");
            PASS_DB = PROPERTIES.getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Configuration of app has been broken.", e);
        }
    }

    private Config() {

    }

    public static Config get() {
        return CONFIG;
    }

    public String getUrlDb() {
        return URL_DB;
    }

    public String getUserDb() {
        return USER_DB;
    }

    public String getPassDb() {
        return PASS_DB;
    }

    public Path getDirectory() {
        return DIRECTORY;
    }
}
