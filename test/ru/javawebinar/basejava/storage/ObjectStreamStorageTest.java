package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.FIleWR.ObjectStreamFWR;

public class ObjectStreamStorageTest extends AbstractStorageTest {

    public ObjectStreamStorageTest() {
        super(new PathStorage(AbstractStorageTest.storagePath, new ObjectStreamFWR()));
    }

}
