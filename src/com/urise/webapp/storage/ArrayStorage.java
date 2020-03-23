package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    public void save(Resume r) {
        if (searchPositionByUuid(r.getUuid()) != -1){
            System.out.println("Sorry, this resume already exists.");
        } else if (size() == STORAGE_LIMIT) {
            System.out.println("Sorry, storage is full.");
        } else {
            storage[cursor++] = r;
        }
    }

    public Resume get(String uuid) {
        int index = searchPositionByUuid(uuid);
        if (index != -1) {
            return storage[index];
        }
        System.out.println("Can`t find element with uuid =" + uuid);
        return null;
    }

   public void delete(String uuid) {
        int index = searchPositionByUuid(uuid);
        if (index != -1) {
            storage[index] = storage[size() -1];
            storage[--cursor]=null;
        } else{
            System.out.println("Can`t find element with uuid = " + uuid);
        }
    }

    public void update(Resume resume){
        int index;
        if ((index = searchPositionByUuid(resume.getUuid())) != -1) {
            storage[index] = resume;
        }else{
            System.out.println("Resume not found in storage.");
        }
    }

    private int searchPositionByUuid(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
