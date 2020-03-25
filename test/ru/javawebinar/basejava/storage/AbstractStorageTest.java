package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public class AbstractStorageTest {

    protected Storage storage;
    protected static final String UUID_1 = "UIID_1";
    protected static final String UUID_2 = "UIID_2";
    protected static final String UUID_3 = "UIID_3";

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp(){
        storage.clear();
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void save() {
        storage.save(new Resume("Abra_Cadabra"));
        storage.save(new Resume("CORBA"));
        Assert.assertEquals("CORBA", storage.get("CORBA").getUuid());
        Assert.assertEquals("Abra_Cadabra", storage.get("Abra_Cadabra").getUuid());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExisting(){
        storage.save(new Resume(UUID_2));
    }


    @Test
    public void update() {

    }

    @Test(expected = NotExistStorageException.class)
    public void updateAbsent(){
        storage.update(new Resume("Abra_Cadabra"));
    }


    @Test
    public void get() {
        Assert.assertEquals(UUID_2, storage.get(UUID_2).getUuid());
        Assert.assertEquals(UUID_3, storage.get(UUID_3).getUuid());
        Assert.assertNotEquals(UUID_3, storage.get(UUID_1).getUuid());
        Assert.assertNotEquals(UUID_1, storage.get(UUID_2).getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void getAbsent(){
        storage.get("Abra_Cadabra");
    }


    @Test
    public void delete() {
        storage.delete(UUID_2);
        storage.delete(UUID_1);
        Assert.assertEquals(UUID_3, storage.getAll()[0].getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteAbsent(){
        storage.delete("Abra_cadabra");
    }
}