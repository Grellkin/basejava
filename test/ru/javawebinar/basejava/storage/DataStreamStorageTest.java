package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.FIleWR.DataStreamFWR;

public class DataStreamStorageTest extends AbstractStorageTest {

    public DataStreamStorageTest() {
        super(new PathStorage(AbstractStorageTest.storagePath, new DataStreamFWR()));
    }

}
