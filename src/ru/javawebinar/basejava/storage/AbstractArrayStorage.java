package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage{

    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int cursor;
    private String uuid;

    public int size() {
        return cursor;
    }

    public void clear() {
        Arrays.fill(storage, null);
        cursor = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size());
    }

    public Resume get(String uuid) {
        if (searchPositionByUuid(uuid) >= 0){
            return storage[searchPositionByUuid(uuid)];
        }
        throw new NotExistStorageException(uuid);
    }

    public void update(Resume resume){
        int index;
        if ((index = searchPositionByUuid(resume.getUuid())) < 0) {
            throw new NotExistStorageException(resume.getUuid());
        }
        storage[index] = resume;

    }

    public void save(Resume r) {
        uuid = r.getUuid();
        int index = searchPositionByUuid(uuid);
        if (index >= 0){
            throw new ExistStorageException(uuid);
        } else if (size() == STORAGE_LIMIT) {
            throw new StorageException("Sorry, storage is full.", uuid);
        }
        insertResume(index, r);
    }

    public void delete(String uuid) {
        int index = searchPositionByUuid(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        removeResume(index);
    }

    protected abstract void insertResume(int index, Resume r);

    protected abstract void removeResume(int index);

    protected abstract int searchPositionByUuid(String uuid);
}
