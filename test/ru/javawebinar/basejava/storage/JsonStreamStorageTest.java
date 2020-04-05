package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.FIleWR.JsonStreamFWR;

public class JsonStreamStorageTest extends AbstractStorageTest {

    public JsonStreamStorageTest() {
        super(new PathStorage(AbstractStorageTest.storagePath, new JsonStreamFWR()));
    }

}
