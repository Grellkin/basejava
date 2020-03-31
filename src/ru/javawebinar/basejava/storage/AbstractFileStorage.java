package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<Path> {

    private Path directory;

    public AbstractFileStorage(Path directory) {
        Objects.requireNonNull(directory, "directory " + directory.getFileName() + " must be not null");
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(directory.getFileName() + " is not a directory");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException("directory has constraints of reading/writing");
        }
        this.directory = directory;
    }

    @Override
    protected Path findSearchKey(String file) {
        return directory.resolve(file);
    }

    @Override
    protected boolean isElementPresentInStorage(Path path) {
        return Files.exists(path);
    }

    @Override
    protected List<Resume> getCopyOfStorage() {
        MyWalker walker = new MyWalker(MyWalker.COPY_ALL_PARAM);
        try {
            Files.walkFileTree(directory, walker);
        } catch (IOException e) {
            throw new StorageException("IOException while traversing file tree.", directory.getFileName().toString(), e);
        }
        return walker.resumes;
    }

    @Override
    protected void removeElement(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new StorageException("IOException while deleting file.", file.getFileName().toString(), e);
        }
    }

    @Override
    protected void insertElement(Path file, Resume resume) {
        try {
            Files.createFile(file);
        } catch (IOException e) {
            throw new StorageException("IOException while creating file.", file.getFileName().toString(), e);
        }
        updateElement(file, resume);
    }

    @Override
    protected void updateElement(Path file, Resume resume) {
        try(BufferedOutputStream os = new BufferedOutputStream(Files.newOutputStream(file))) {
            writeResumeToFile(os, resume);
        } catch (IOException e) {
            throw new StorageException("IOException while writing to file.", file.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume getElement(Path file) {
        try {
            return readResumeFromFile(new BufferedInputStream(Files.newInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IOException while writing to file.", file.getFileName().toString(), e);
        }
    }

    @Override
    public void clear() {
        MyWalker walker = new MyWalker(MyWalker.CLEAR_PARAM);
        try {
            Files.walkFileTree(directory, walker);
        } catch (IOException e) {
            throw new StorageException("IOException while traversing file tree.", directory.getFileName().toString(), e);
        }
    }

    @Override
    public int size() {
        MyWalker walker = new MyWalker(MyWalker.SIZE_PARAM);
        try {
            Files.walkFileTree(directory, walker);
        } catch (IOException e) {
            throw new StorageException("IOException while traversing file tree.", directory.getFileName().toString(), e);
        }
        return walker.countOfFiles;
    }

    protected abstract Resume readResumeFromFile(InputStream inputStream);

    protected abstract void writeResumeToFile(OutputStream outputStream, Resume resume) throws IOException;

    private class MyWalker extends SimpleFileVisitor<Path> {

        private int countOfFiles = 0;
        private static final int CLEAR_PARAM = 1;
        private static final int SIZE_PARAM = 2;
        private static final int COPY_ALL_PARAM = 3;
        private final int CHOSEN_PARAM;
        private List<Resume> resumes = new ArrayList<>();

        private MyWalker(int param) {
            CHOSEN_PARAM = param;
        }


        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

            switch (CHOSEN_PARAM) {
                case CLEAR_PARAM:
                    Files.delete(file);
                    break;
                case SIZE_PARAM:
                    countOfFiles++;
                    break;
                case COPY_ALL_PARAM:
                    resumes.add(getElement(file));
                    break;
                default:
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
