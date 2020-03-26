package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int cursor;


    @Override
    protected boolean isElementPresentInStorage(Object searchKey) {
        return (Integer) searchKey >= 0;
    }

    @Override
    protected Resume getElement(Object searchKey) {
        return storage[(Integer) searchKey];
    }

    @Override
    protected void removeElement(Object searchKey) {
        removeResume((Integer) searchKey);
    }

    @Override
    protected void insertElement(Object searchKey, Resume resume) {
        if (size() == STORAGE_LIMIT) {
            throw new StorageException("Sorry, storage is full.", resume.getUuid());
        }
        insertResume((Integer) searchKey, resume);
    }

    @Override
    protected void updateElement(Object searchKey, Resume resume) {
        storage[(Integer) searchKey] = resume;
    }


    public int size() {return cursor; }

    public void clear() {
        Arrays.fill(storage, null);
        cursor = 0;
    }

    protected abstract void insertResume(int index, Resume r);

    protected abstract void removeResume(int index);




}


