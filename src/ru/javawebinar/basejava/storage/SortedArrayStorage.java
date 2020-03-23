package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertResume(int index, Resume r) {
        index = -(index +1);
        System.arraycopy(storage, index, storage, index+1, cursor-index);
        cursor++;
        storage[index] = r;
    }

    @Override
    protected void removeResume(int index) {
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
