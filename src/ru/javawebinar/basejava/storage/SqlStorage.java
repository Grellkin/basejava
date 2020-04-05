package ru.javawebinar.basejava.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    private final SqlHelper helper;
    private static final Logger LOGGER = LogManager.getLogger("sqlLogger");

    public SqlStorage(String url, String user, String password) {
        this.helper = new SqlHelper(() -> DriverManager.getConnection(url, user, password));
    }

    @Override
    public void clear() {
        LOGGER.info("Delete everything from resume_db");
        helper.doSQL("DELETE FROM resume;", (SqlHelper.SqlExecutor<Object>) PreparedStatement::executeUpdate);
    }

    @Override
    public void save(Resume resume) {
        LOGGER.info("Save resume with uuid = " + resume.getUuid() + " in resume_db");
        helper.doSQL("INSERT INTO resume(uuid, full_name) VALUES (?,?);", statement -> {
            statement.setString(1, resume.getUuid());
            statement.setString(2, resume.getFullName());
            if (statement.executeUpdate() < 1) {
                throw new ExistStorageException("Sorry, this resume already present in DB");
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOGGER.info("Get resume with uuid = " + uuid + " from resume_db");
        return helper.doSQL("SELECT * FROM resume WHERE uuid = ?;", statement -> {
            statement.setString(1, uuid);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                LOGGER.info("No resume with uuid = " + uuid + " in resume_db");
                throw new NotExistStorageException("There is no resume with such uuid in DB");
            }
            Resume resume = new Resume();
            resume.setUuid(set.getString(1).trim());
            resume.setFullName(set.getString(2));
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        LOGGER.info("Delete resume with uuid = " + uuid + " from resume_db");
        helper.doSQL("DELETE FROM resume WHERE uuid = ?;", statement -> {
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
        List<Resume> retList = helper.doSQL("SELECT * FROM resume;", statement -> {
            ResultSet set = statement.executeQuery();
            List<Resume> list = new ArrayList<>();
            while (set.next()) {
                list.add(new Resume(set.getString(1).trim(), set.getString(2)));
            }
            return list;
        });
        retList.sort(Resume.comparatorByFullNameAndUuid);
        return retList;
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
        helper.doSQL("UPDATE resume SET full_name = ? WHERE uuid = ?;", statement -> {
            statement.setString(1, resume.getFullName());
            statement.setString(2, resume.getUuid());
            if (statement.executeUpdate() < 1) {
                throw new NotExistStorageException("No resume with uuid = " + resume.getUuid() + " found in DB");
            }
            return null;
        });
    }


}



