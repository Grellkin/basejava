package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.assertArrayEquals;

public class ArrayStorageTest extends AbstractArrayStorageTest{

    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        assertArrayEquals(new Resume[]{new Resume(UUID_2), new Resume(UUID_1), new Resume(UUID_3)}, resumes);
    }
}