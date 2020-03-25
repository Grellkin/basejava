package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage{

    @Override
    public Resume get(String uuid) {
        if (!isElementPresentInStorage(uuid)){
            throw new NotExistStorageException(uuid);
        }
        return getElement(uuid);
    }

    @Override
    public void delete(String uuid) {
        if (!isElementPresentInStorage(uuid)){
            throw new NotExistStorageException(uuid);
        }
        removeElement(uuid);
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        if (isElementPresentInStorage(uuid)) {
            throw new ExistStorageException(uuid);
        }
        insertElement(resume);
    }

    @Override
    public void update(Resume resume) {
        String uuid = resume.getUuid();
        if (!isElementPresentInStorage(uuid)) {
            throw new NotExistStorageException(uuid);
        }
        updateElement(resume);
    }



    protected abstract boolean isElementPresentInStorage(String uuid);

    protected abstract Resume getElement(String uuid);

    protected abstract void removeElement(String uuid);

    protected abstract void insertElement(Resume resume);

    protected abstract void updateElement(Resume resume);
}
