package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private List<Resume> storage = new ArrayList<>();

    @Override
    protected boolean isElementPresentInStorage(String uuid) {
        return indexOfElement(uuid) >= 0;
    }

    @Override
    protected Resume getElement(String uuid) {
        return storage.get(indexOfElement(uuid));
    }

    @Override
    protected void removeElement(String uuid) {
        storage.remove(indexOfElement(uuid));
    }

    @Override
    protected void insertElement(Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void updateElement(Resume resume) {
        storage.set(indexOfElement(resume.getUuid()), resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }


    private int indexOfElement(String uuid){
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)){
                return i;
            }
        }
        return -1;
    }
}
