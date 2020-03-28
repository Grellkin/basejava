package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertResume(int index, Resume r) {
        index = -(index + 1);
        System.arraycopy(storage, index, storage, index + 1, cursor - index);
        cursor++;
        storage[index] = r;
    }

    @Override
    protected void removeResume(int index) {
        System.arraycopy(storage, index + 1, storage, index, (--cursor) - index);
        storage[cursor] = null;
    }

    @Override
    protected Integer findSearchKey(String uuid) {
        Resume r = new Resume(uuid, "defaultName");
        return Arrays.binarySearch(storage, 0, size(), r, Resume.comparatorByUuid);
    }

    @Override
    protected List<Resume> getCopyOfStorage() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size()));
    }
}
