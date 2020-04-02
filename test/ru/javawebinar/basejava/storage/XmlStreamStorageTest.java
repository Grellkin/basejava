package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.FIleWR.XmlStreamFWR;

public class XmlStreamStorageTest extends AbstractStorageTest {

    public XmlStreamStorageTest() {
        super(new PathStorage(AbstractStorageTest.storagePath, new XmlStreamFWR()));
    }

}
