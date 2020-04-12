package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private static final Path PROPS = Paths.get(getHomeDir()+"/config/resume.properties");
    private static final Properties PROPERTIES = new Properties();
    private static final Path DIRECTORY;
    private static final Config CONFIG = new Config();
    private static Storage storage;

    static {
        //Resume.class.getClassLoader().getResourceAsStream(PROPS.toString());
        try (InputStream inputStream = Files.newInputStream(PROPS)) {
            PROPERTIES.load(inputStream);
            DIRECTORY = Paths.get(PROPERTIES.getProperty("dir.storage"));
            Class.forName("org.postgresql.Driver");
            storage = new SqlStorage(PROPERTIES.getProperty("db.url"),
                    PROPERTIES.getProperty("db.user"), PROPERTIES.getProperty("db.password"));
        } catch (IOException|ClassNotFoundException e) {
            throw new IllegalStateException("Configuration of app has been broken.", e);
        }
    }

    private Config() {

    }

    public static Config get() {
        return CONFIG;
    }

    public Path getDirectory() {
        return DIRECTORY;
    }

    public Storage getStorage() {
        return storage;
    }

    private static String getHomeDir(){
        Path path = Paths.get(System.getProperty("homeDir"));
        if (!Files.isDirectory(path)){
            throw new IllegalStateException("Path "+path.toString()+" is not a directory.");
        }
        return path.toString();
    }
}
