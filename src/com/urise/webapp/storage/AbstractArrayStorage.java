package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

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
}
