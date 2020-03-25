package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int cursor;


    @Override
    protected boolean isElementPresentInStorage(String uuid) {
        return searchPositionByUuid(uuid) >= 0;
    }

    @Override
    protected Resume getElement(String uuid) {
        return storage[searchPositionByUuid(uuid)];
    }

    @Override
    protected void removeElement(String uuid) {
        removeResume(searchPositionByUuid(uuid));
    }

    @Override
    protected void insertElement(Resume resume) {
        String uuid = resume.getUuid();
        if (size() == STORAGE_LIMIT) {
            throw new StorageException("Sorry, storage is full.", uuid);
        }
        insertResume(searchPositionByUuid(uuid), resume);
    }

    @Override
    protected void updateElement(Resume resume) {
        storage[searchPositionByUuid(resume.getUuid())] = resume;
    }


    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size());
    }

    public int size() {return cursor; }

    public void clear() {
        Arrays.fill(storage, null);
        cursor = 0;
    }

    protected abstract void insertResume(int index, Resume r);

    protected abstract void removeResume(int index);

    protected abstract int searchPositionByUuid(String uuid);


    //    public void save(Resume resume) {
//        String uuid = resume.getUuid();
//        int index = searchPositionByUuid(uuid);
//        if (index >= 0) {
//            throw new ExistStorageException(uuid);
//        } else if (size() == STORAGE_LIMIT) {
//            throw new StorageException("Sorry, storage is full.", uuid);
//        }
//        insertResume(index, resume);
//    }

    //    public void delete(String uuid) {
//        int index = searchPositionByUuid(uuid);
//        if (index < 0) {
//            throw new NotExistStorageException(uuid);
//        }
//        removeResume(index);
//    }

    //    public Resume get(String uuid) {
//        if (searchPositionByUuid(uuid) >= 0) {
//            return storage[searchPositionByUuid(uuid)];
//        }
//        throw new NotExistStorageException(uuid);
//    }

    //    public void update(Resume resume) {
//        int index;
//        if ((index = searchPositionByUuid(resume.getUuid())) < 0) {
//            throw new NotExistStorageException(resume.getUuid());
//        }
//        storage[index] = resume;
//
//    }
}


