package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.assertArrayEquals;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Test
    public void getAllTest() {
        Resume[] resumes = storage.getAll();
        assertArrayEquals(new Resume[]{new Resume(UUID_2), new Resume(UUID_1), new Resume(UUID_3)}, resumes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void sizeTest() {
        storage.size();
    }

    @Test
    public void clearTest() {
        storage.clear();
        try {
            storage.get(UUID_1);
            Assert.fail();
        } catch (NotExistStorageException e) {
        }
        try {
            storage.get(UUID_2);
            Assert.fail();
        } catch (NotExistStorageException e) {
        }


    }


}
