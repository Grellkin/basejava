package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.Path;

public class ObjectStreamStorage extends AbstractFileStorage {

    public ObjectStreamStorage(Path directory) {
        super(directory);
    }

    @Override
    protected Resume readResumeFromFile(InputStream inputStream) {
        try(ObjectInputStream ois = new ObjectInputStream(inputStream)){
            return (Resume) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Can`t read from file", null, e);
        }
    }

    @Override
    protected void writeResumeToFile(OutputStream outputStream, Resume resume) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(outputStream)){
            oos.writeObject(resume);
        }
    }
}
