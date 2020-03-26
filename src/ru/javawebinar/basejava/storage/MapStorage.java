package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapStorage extends AbstractStorage {

    private Map<String, Resume> storage = new HashMap<>();



    @Override
    protected Object findSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isElementPresentInStorage(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }

    @Override
    protected Resume getElement(Object searchKey) {
        return storage.get((String) searchKey);
    }

    @Override
    protected void removeElement(Object searchKey) {
        storage.remove((String) searchKey);
    }

    @Override
    protected void insertElement(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    protected void updateElement(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        return storage.values().stream().sorted(Resume.comparatorByFullNameAndUuid).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
