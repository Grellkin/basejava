import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SortedArrayStorage;
import ru.javawebinar.basejava.storage.Storage;

/**
 * Test for your com.urise.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    private static final Storage STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        final Resume r1 = new Resume();
        r1.setUuid("uuid1");
        final Resume r2 = new Resume();
        r2.setUuid("uuid2");
        final Resume r3 = new Resume();
        r3.setUuid("uuid3");
        
        STORAGE.save(r1);
        STORAGE.save(r2);
        STORAGE.save(r3);

        System.out.println("Get r1: " + STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + STORAGE.size());

        System.out.println("Get dummy: " + STORAGE.get("dummy"));

        printAll();
        STORAGE.delete(r1.getUuid());
        printAll();
        STORAGE.clear();
        printAll();

        System.out.println("Size: " + STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}
