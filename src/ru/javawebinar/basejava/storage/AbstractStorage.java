package ru.javawebinar.basejava.storage;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger log = LogManager.getLogger();

    @Override
    public Resume get(String uuid) {
        log.debug("Get resume with uuid = " + uuid);
        SK searchKey = findSearchKey(uuid);
        if (!isElementPresentInStorage(searchKey)) {
            log.warn("Element " + uuid + " not present in storage");
            throw new NotExistStorageException(uuid);
        }
        return getElement(searchKey);
    }

    @Override
    public void delete(String uuid) {
        log.info("Delete resume with uuid = " + uuid);
        SK searchKey = findSearchKey(uuid);
        if (!isElementPresentInStorage(searchKey)) {
            log.warn("Element " + uuid + " not present in storage");
            throw new NotExistStorageException(uuid);
        }
        removeElement(searchKey);
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
//        log.info("Save resume with uuid = " + uuid);
        SK searchKey = findSearchKey(uuid);
        if (isElementPresentInStorage(searchKey)) {
            log.warn("Element " + uuid + " already present in storage");
            throw new ExistStorageException(uuid);
        }
        insertElement(searchKey, resume);
    }

    @Override
    public void update(Resume resume) {
        String uuid = resume.getUuid();
        log.info("Update resume with uuid = " + uuid);
        SK searchKey = findSearchKey(uuid);
        if (!isElementPresentInStorage(searchKey)) {
            log.warn("Element " + uuid + " not present in storage");
            throw new NotExistStorageException(uuid);
        }
        updateElement(searchKey, resume);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = getCopyOfStorage();
        list.sort(Resume.comparatorByFullNameAndUuid);
        return list;
    }

    protected abstract List<Resume> getCopyOfStorage();

    protected abstract SK findSearchKey(String uuid);

    protected abstract boolean isElementPresentInStorage(SK searchKey);

    protected abstract Resume getElement(SK searchKey);

    protected abstract void removeElement(SK searchKey);

    protected abstract void insertElement(SK searchKey, Resume resume);

    protected abstract void updateElement(SK searchKey, Resume resume);
}
