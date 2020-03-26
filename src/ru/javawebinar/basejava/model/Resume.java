package ru.javawebinar.basejava.model;

import java.util.Comparator;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume {

    // Unique identifier
    private final String uuid;
    private String fullName;
    public static final Comparator<Resume> comparatorByFullNameAndUuid =
            Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);
    public static final Comparator<Resume> comparatorByUuid = Comparator.comparing(Resume::getUuid);

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public Resume() {
        this(UUID.randomUUID().toString(), "defaultName");
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return uuid;
    }


}
