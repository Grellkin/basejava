package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.util.Config;

public class SqlStorageTest extends AbstractStorageTest {

    private static final Config config = Config.get();

    public SqlStorageTest() {
        super(config.getStorage());
    }
}
