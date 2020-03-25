package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.NotExistStorageException;

public class MapStorageTest extends AbstractStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
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
