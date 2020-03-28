package ru.javawebinar.basejava.model;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume {

    // Unique identifier
    private final String uuid;
    private final String fullName;

    public static final Comparator<Resume> comparatorByFullNameAndUuid =
            Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    public static final Comparator<Resume> comparatorByUuid = Comparator.comparing(Resume::getUuid);

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(fullName, "Full name should be not null");
        Objects.requireNonNull(uuid, "Uuid should be not null");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "( " + fullName + ")";
    }


}
