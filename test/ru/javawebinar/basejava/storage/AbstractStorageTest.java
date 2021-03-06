package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.util.Config;

import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static ru.javawebinar.basejava.storage.ResumeTestData.*;

public class AbstractStorageTest {

    protected static final Path storagePath = Config.get().getDirectory();
    protected Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        createContacts();
        createSections();
        storage.save(FIRST_RESUME);
        storage.save(SECOND_RESUME);
        storage.save(THIRD_RESUME);
    }

    @Test
    public void save() {
        storage.save(new Resume("Abra_Cadabra", NAME_3));
        storage.save(new Resume("CORBA", NAME_1));
        Assert.assertEquals(NAME_1, storage.get("CORBA").getFullName());
        Assert.assertEquals(NAME_3, storage.get("Abra_Cadabra").getFullName());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExisting() {
        storage.save(new Resume(UUID_2, NAME_2));
    }

    @Test
    public void update() {
        Resume newResume = new Resume(UUID_1, "newName");
        storage.update(newResume);
        Assert.assertEquals("newName", storage.get(UUID_1).getFullName());
    }

    @Test(expected = NotExistStorageException.class)
    public void updateAbsent() {
        storage.update(new Resume("Abra_Cadabra", NAME_1));
    }

    @Test
    public void get() {
        Assert.assertEquals(FIRST_RESUME, storage.get(UUID_1));
        Assert.assertEquals(THIRD_RESUME, storage.get(UUID_3));
        Assert.assertNotEquals(THIRD_RESUME, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getAbsent() {
        storage.get("Abra_Cadabra");
    }

    @Test
    public void delete() {
        storage.delete(UUID_2);
        storage.delete(UUID_1);
        Assert.assertEquals(THIRD_RESUME, storage.getAllSorted().get(0));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteAbsent() {
        storage.delete("Abra_cadabra");
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void getAllSorted() {
        List<Resume> resumes = storage.getAllSorted();
        assertArrayEquals(new Resume[]{FIRST_RESUME, SECOND_RESUME, THIRD_RESUME}, resumes.toArray());
    }
}