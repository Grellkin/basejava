package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private List<Resume> storage = new ArrayList<>();

    @Override
    protected boolean isElementPresentInStorage(Object searchKey) {
        return (Integer) searchKey >= 0;
    }

    @Override
    protected Resume getElement(Object searchKey) {
        return storage.get((Integer) searchKey);
    }

    @Override
    protected void removeElement(Object searchKey) {
        storage.remove((int)searchKey);
    }

    @Override
    protected void insertElement(Object searchKey, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void updateElement(Object searchKey, Resume resume) {
        storage.set((Integer) searchKey, resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        storage.sort(Resume.comparatorByFullNameAndUuid);
        return storage;
    }

    @Override
    public int size() {
       return storage.size();
    }

    @Override
    protected Integer findSearchKey(String uuid){
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)){
                return i;
            }
        }
        return -1;
    }
}
