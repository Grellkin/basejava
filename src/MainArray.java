import ru.javawebinar.basejava.storage.SortedArrayStorage;
import ru.javawebinar.basejava.storage.Storage;

/**
 * Interactive test for com.urise.webapp.storage.ArrayStorage implementation
 * (just run, no need to understand)
 */
public class MainArray {
    private final static Storage STORAGE = new SortedArrayStorage();

    public static void main(String[] args){

        //        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        Resume r;
//        while (true) {
//            System.out.print("Введите одну из команд - (list | save uuid | delete uuid | get uuid | clear | update | exit): ");
//            String[] params = reader.readLine().trim().toLowerCase().split(" ");
//            if (params.length < 1 || params.length > 2) {
//                System.out.println("Неверная команда.");
//                continue;
//            }
//            String uuid = null;
//            if (params.length == 2) {
//                uuid = params[1].intern();
//            }
//            switch (params[0]) {
//                case "list":
//                    printAll();
//                    break;
//                case "size":
//                    System.out.println(STORAGE.size());
//                    break;
//                case "save":
//                    r = new Resume(uuid, fullName);
//                    STORAGE.save(r);
//                    printAll();
//                    break;
//                case "delete":
//                    STORAGE.delete(uuid);
//                    printAll();
//                    break;
//                case "get":
//                    System.out.println(STORAGE.get(uuid));
//                    break;
//                case "clear":
//                    STORAGE.clear();
//                    printAll();
//                    break;
//                case "update":
//                    r = new Resume(uuid, fullName);
//                    STORAGE.update(r);
//                    break;
//                case "exit":
//                    return;
//                default:
//                    System.out.println("Неверная команда.");
//                    break;
//            }
//        }
//    }
//
//    static void printAll() {
//        List<Resume> all = STORAGE.getAllSorted();
//        System.out.println("----------------------------");
//        if (all.size() == 0) {
//            System.out.println("Empty");
//        } else {
//            for (Resume r : all) {
//                System.out.println(r);
//            }
//        }
//        System.out.println("----------------------------");
    }
}