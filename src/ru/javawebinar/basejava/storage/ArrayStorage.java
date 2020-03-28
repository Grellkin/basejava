package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertResume(int index, Resume r) {
        storage[cursor++] = r;
    }

    @Override
    protected void removeResume(int index) {
        storage[index] = storage[size() - 1];
        storage[--cursor] = null;
    }

    @Override
    protected Integer findSearchKey(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected List<Resume> getCopyOfStorage() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size()));
    }
}
