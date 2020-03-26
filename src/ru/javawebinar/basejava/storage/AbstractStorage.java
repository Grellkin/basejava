package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage{

    @Override
    public Resume get(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (!isElementPresentInStorage(searchKey)){
            throw new NotExistStorageException(uuid);
        }
        return getElement(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = findSearchKey(uuid);
        if (!isElementPresentInStorage(searchKey)){
            throw new NotExistStorageException(uuid);
        }
        removeElement(searchKey);
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = findSearchKey(uuid);
        if (isElementPresentInStorage(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        insertElement(searchKey, resume);
    }

    @Override
    public void update(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = findSearchKey(uuid);
        if (!isElementPresentInStorage(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        updateElement(searchKey, resume);
    }


    protected abstract Object findSearchKey(String uuid);

    protected abstract boolean isElementPresentInStorage(Object searchKey);

    protected abstract Resume getElement(Object searchKey);

    protected abstract void removeElement(Object searchKey);

    protected abstract void insertElement(Object searchKey, Resume resume);

    protected abstract void updateElement(Object searchKey, Resume resume);
}
