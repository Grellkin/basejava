package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    public void save(Resume r) {
        int index = searchPositionByUuid(r.getUuid());
        if (index >= 0){
            System.out.println("Sorry, this resume already exists.");
        } else if (size() == STORAGE_LIMIT) {
            System.out.println("Sorry, storage is full.");
        } else {
            index = -(index +1);
            System.arraycopy(storage, index, storage, index+1, cursor-index);
            cursor++;
            storage[index] = r;

        }
    }

    @Override
    public void delete(String uuid) {
        int index = searchPositionByUuid(uuid);
        if (index < 0){
            System.out.println("Can`t find element with uuid =" + uuid);
            return;
        }
        System.arraycopy(storage, index+1, storage, index, (--cursor)-index);
        storage[cursor] = null;
    }

    @Override
    protected int searchPositionByUuid(String uuid) {
        Resume r = new Resume();
        r.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size(), r);
    }
}
