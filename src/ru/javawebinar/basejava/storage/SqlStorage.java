package ru.javawebinar.basejava.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {

    private final SqlHelper helper;
    private static final Logger LOGGER = LogManager.getLogger("sqlLogger");

    public SqlStorage(String url, String user, String password) {
        this.helper = new SqlHelper(() -> DriverManager.getConnection(url, user, password));
    }

    @Override
    public void clear() {
        LOGGER.info("Delete everything from resume_db");
        helper.doSQL("DELETE FROM resume;", PreparedStatement::executeUpdate);
    }

    @Override
    public void save(Resume resume) {
        LOGGER.info("Save resume with uuid = " + resume.getUuid() + " in resume_db");
        helper.doTransactSQL(connection -> {
            String uuid = resume.getUuid();
            try (PreparedStatement resumeStatement = connection.prepareStatement(
                    "INSERT INTO resume(full_name, uuid) VALUES (?,?);")) {
                resumeStatement.setString(2, uuid);
                resumeStatement.setString(1, resume.getFullName());
                if (resumeStatement.executeUpdate() < 1) {
                    throw new ExistStorageException("Sorry, this resume already present in DB");
                }

            }
            try (PreparedStatement contactStatement = connection.prepareStatement(
                    "INSERT INTO contact(type, value, resume_uuid) VALUES (?,?,?);")) {
                for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                    contactStatement.setString(1, entry.getKey().name());
                    contactStatement.setString(2, entry.getValue());
                    contactStatement.setString(3, uuid);
                    contactStatement.addBatch();
                }
                contactStatement.executeBatch();
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOGGER.info("Get resume with uuid = " + uuid + " from resume_db");
        return helper.doSQL("SELECT r.uuid, r.full_name, c.type, c.value FROM resume r " +
                                    "LEFT OUTER JOIN contact c ON r.uuid = c.resume_uuid " +
                                        "WHERE r.uuid = ?", statement -> {
            statement.setString(1, uuid);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                LOGGER.info("No resume with uuid = " + uuid + " in resume_db");
                throw new NotExistStorageException("There is no resume with such uuid in DB");
            }
            Resume resume = new Resume(set.getString("uuid"), set.getString("full_name"));
            do {
                addCont(set, resume);
            } while (set.next());
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        LOGGER.info("Delete resume with uuid = " + uuid + " from resume_db");
        helper.<Void>doSQL("DELETE FROM resume WHERE uuid = ?;", statement -> {
            statement.setString(1, uuid);
            if (statement.executeUpdate() < 1) {
                throw new NotExistStorageException("No resume with uuid = " + uuid + " found in DB");
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOGGER.info("Get all resumes from resume_db");
        return helper.doSQL("SELECT r.uuid, r.full_name, c.type, c.value FROM resume r " +
                                    "LEFT OUTER JOIN contact c ON r.uuid = c.resume_uuid " +
                                        "ORDER BY r.full_name, r.uuid;", statement -> {
            ResultSet set = statement.executeQuery();
            Map<String, Resume> map = new LinkedHashMap<>();
            while (set.next()) {
                String uuid = set.getString("uuid");
                Resume resume = map.get(uuid);
                if (resume == null) {
                    resume = new Resume(uuid, set.getString("full_name"));
                    map.put(uuid, resume);
                }
                addCont(set, resume);
            }
            return new ArrayList<>(map.values());
        });
    }

    @Override
    public int size() {
        LOGGER.info("Get size of resume storage in resume_db");
        return helper.doSQL("SELECT count(*) FROM resume;", statement -> {
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                throw new StorageException("There is no resume with such uuid in DB");
            }
            return set.getInt(1);
        });
    }

    @Override
    public void update(Resume resume) {
        LOGGER.info("Update resume with uuid = " + resume.getUuid() + " in resume_db");
        helper.doTransactSQL(connection -> {
            String uuid = resume.getUuid();
            try (PreparedStatement resumeStatement = connection.prepareStatement(
                    "UPDATE resume SET full_name = ? WHERE uuid = ?;")) {
                resumeStatement.setString(2, uuid);
                resumeStatement.setString(1, resume.getFullName());
                if (resumeStatement.executeUpdate() < 1) {
                    throw new NotExistStorageException("Sorry, this resume does not present in DB");
                }
            }
            try (PreparedStatement contactStatement = connection.prepareStatement(
                    "INSERT INTO contact(resume_uuid, type, value)\n" +
                            "VALUES (?,?,?)\n" +
                            "ON CONFLICT (resume_uuid, type) DO\n" +
                            "UPDATE SET value = ? WHERE contact.resume_uuid= ? and contact.type = ?;")) {
                for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                    contactStatement.setString(1, uuid);
                    contactStatement.setString(2, entry.getKey().name());
                    contactStatement.setString(3, entry.getValue());
                    contactStatement.setString(4, entry.getValue());
                    contactStatement.setString(5, uuid);
                    contactStatement.setString(6, entry.getKey().name());
                    contactStatement.addBatch();
                }
                contactStatement.executeBatch();
            }
            return null;
        });

    }

    private void addCont(ResultSet set, Resume resume) throws SQLException {
        String value = set.getString("value");
        if (value!=null) {
            resume.addContact(ContactType.valueOf(set.getString("type")), value);
        }
    }


}



