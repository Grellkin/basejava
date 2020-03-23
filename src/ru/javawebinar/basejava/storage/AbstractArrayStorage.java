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

    protected abstract int searchPositionByUuid(String uuid);
}
