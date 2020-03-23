package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage{

    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int cursor;

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
        int index = searchPositionByUuid(uuid);
        if (index <0){
            System.out.println("Can`t find element with uuid =" + uuid);
            return null;
        }
        return storage[index];
    }

    public void update(Resume resume){
        int index;
        if ((index = searchPositionByUuid(resume.getUuid())) >= 0) {
            storage[index] = resume;
        }else{
            System.out.println("Resume not found in storage.");
        }
    }

    public void save(Resume r) {
        int index = searchPositionByUuid(r.getUuid());
        if (index >= 0){
            System.out.println("Sorry, this resume already exists.");
        } else if (size() == STORAGE_LIMIT) {
            System.out.println("Sorry, storage is full.");
        } else {
            insertResume(index, r);
        }
    }

    public void delete(String uuid) {
        int index = searchPositionByUuid(uuid);
        if (index >= 0) {
            removeResume(index);
        } else{
            System.out.println("Can`t find element with uuid = " + uuid);
        }
    }

    protected abstract void insertResume(int index, Resume r);

    protected abstract void removeResume(int index);

    protected abstract int searchPositionByUuid(String uuid);
}
