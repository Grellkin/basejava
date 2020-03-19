import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int cursor;


    void clear() {
        for (int i = 0; i < cursor; i++) {
            storage[i] = null;
        }
        cursor = 0;
    }

    void save(Resume r) {
        if (cursor == storage.length) {
            System.out.println("Sorry, storage is full.");
        }
        else {
            storage[cursor++] = r;
        }
    }

    Resume get(String uuid) {
        int index = searchPositionByUuid(uuid);
        return index != -1 ? storage[index] : null;
    }

    void delete(String uuid) {
        int index = searchPositionByUuid(uuid);
        if (index != -1) {
            System.arraycopy(storage, index + 1, storage, index, (--cursor) - index);
        } else {
            System.out.println("Sorry, can`t find element with id = " + uuid);
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */

    Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, cursor);
    }

    int size() {
        return cursor;
    }

    private int searchPositionByUuid(String uuid) {
        for (int i = 0; i < cursor; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
