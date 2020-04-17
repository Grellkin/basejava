package ru.javawebinar.basejava.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.*;

public class SqlStorage implements Storage {

    private final SqlHelper helper;
    private static final Logger LOGGER = LogManager.getLogger("sqlLogger");

    public SqlStorage(String url, String user, String password) {
        this.helper = new SqlHelper(() -> DriverManager.getConnection(url, user, password));
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
            insertTextSectionsFromResume(resume, connection, uuid);
            insertOrganizationsFromResume(resume, connection, uuid);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOGGER.info("Get resume with uuid = " + uuid + " from resume_db");

        return helper.doTransactSQL(connection -> {
            Resume resume;
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT r.uuid, r.full_name, c.type, c.value FROM resume r " +
                            "LEFT OUTER JOIN contact c ON r.uuid = c.resume_uuid " +
                            "WHERE r.uuid = ?")) {
                statement.setString(1, uuid);
                ResultSet set = statement.executeQuery();
                if (!set.next()) {
                    LOGGER.info("No resume with uuid = " + uuid + " in resume_db");
                    throw new NotExistStorageException("There is no resume with such uuid in DB");
                }
                resume = new Resume(set.getString("uuid"), set.getString("full_name"));
                do {
                    addCont(set, resume);
                } while (set.next());
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT type, info, resume_uuid  FROM text_section WHERE resume_uuid = ?", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {
                statement.setString(1, uuid);
                ResultSet set = statement.executeQuery();
                addTextSectionsToResume(set, resume);
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT p.position, p.start_date, p.end_date, p.organization_name, p.resume_uuid, p.type,p.info, o.url " +
                            "FROM position p " +
                            "JOIN organization o ON o.organization_name = p.organization_name " +
                            "WHERE p.resume_uuid = ?" +
                            " ORDER BY p.resume_uuid, p.type, p.organization_name", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {
                statement.setString(1, uuid);
                ResultSet set = statement.executeQuery();
                addOrgSectionsToResume(set, resume);
            }

            return resume;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOGGER.info("Get all resumes from resume_db");
        return helper.doTransactSQL(connection -> {
            List<Resume> resumes = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM resume ORDER BY uuid;")) {
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    resumes.add(new Resume(set.getString("uuid"), set.getString("full_name")));
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT type, value, resume_uuid FROM contact ORDER BY resume_uuid;", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {
                ResultSet set = statement.executeQuery();
                for (Resume res :
                        resumes) {
                    addContactsToResume(set, res);
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT type, info, resume_uuid FROM text_section ORDER BY resume_uuid;", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {
                ResultSet set = statement.executeQuery();
                for (Resume res :
                        resumes) {
                    addTextSectionsToResume(set, res);
                }
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT p.position, p.start_date, p.end_date, p.organization_name, p.resume_uuid, p.type, p.info, o.url " +
                            "FROM position p " +
                            "JOIN organization o ON o.organization_name = p.organization_name " +
                            " ORDER BY p.resume_uuid, p.type, p.organization_name", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {
                ResultSet set = statement.executeQuery();
                for (Resume res :
                        resumes) {
                    addOrgSectionsToResume(set, res);
                }
            }

            resumes.sort(Resume.comparatorByFullNameAndUuid);
            return resumes;
        });
    }

    private void addOrgSectionsToResume(ResultSet set, Resume resume) throws SQLException {
        String uuid = resume.getUuid();
        SectionType currentSection = null;
        String currentNameOrg = "";
        Link link;
        List<Organization> listOrg = null;
        List<Organization.Position> positionList = null;
        OrganizationSection section;
        Organization organization;
        while (set.next()) {
            if (!set.getString("resume_uuid").equals(uuid)) {
                set.previous();
                return;
            }
            SectionType newSection = SectionType.valueOf(set.getString("type"));
            if (newSection != currentSection) {
                section = new OrganizationSection();
                resume.getSections().put(newSection, section);
                listOrg = new ArrayList<>();
                section.setContent(listOrg);

                organization = new Organization();
                listOrg.add(organization);

                link = new Link();
                organization.setOrganizationName(link);
                positionList = new ArrayList<>();
                organization.setPositions(positionList);

                currentSection = newSection;

                link.setName(set.getString("organization_name"));
                link.setUrl(set.getString("url"));
                currentNameOrg = set.getString("organization_name");
            } else if (!set.getString("organization_name").equals(currentNameOrg)) {
                organization = new Organization();
                listOrg.add(organization);
                link = new Link();
                organization.setOrganizationName(link);
                positionList = new ArrayList<>();
                organization.setPositions(positionList);

                currentSection = newSection;

                link.setName(set.getString("organization_name"));
                link.setUrl(set.getString("url"));
                currentNameOrg = set.getString("organization_name");
            }
            LocalDate start_date = convertToLocalDateViaInstant(set.getDate("start_date"));
            LocalDate end_date = convertToLocalDateViaInstant(set.getDate("end_date"));
            positionList.add(new Organization.Position(start_date, end_date, set.getString("position"),
            set.getString("info")));
        }
    }
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
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

            try (PreparedStatement delSectStatement = connection.prepareStatement(
                    "DELETE FROM text_section WHERE resume_uuid = ?")) {
                delSectStatement.setString(1, uuid);
                delSectStatement.executeUpdate();
            }

            insertTextSectionsFromResume(resume, connection, uuid);
            try (PreparedStatement delPosStatement = connection.prepareStatement(
                    "DELETE FROM position WHERE resume_uuid = ?")) {
                delPosStatement.setString(1, uuid);
                delPosStatement.executeUpdate();
            }
            insertOrganizationsFromResume(resume, connection, uuid);
            return null;


        });

    }

    @Override
    public void clear() {
        LOGGER.info("Delete everything from resume_db");
        helper.doSQL("DELETE FROM resume;", PreparedStatement::executeUpdate);
        helper.doSQL("DELETE FROM organization;", PreparedStatement::executeUpdate);
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

    private void insertTextSectionsFromResume(Resume resume, Connection connection, String uuid) throws SQLException {
        try (PreparedStatement sectionStatement = connection.prepareStatement(
                "INSERT INTO text_section(type, info, resume_uuid) VALUES (?,?,?);")) {
            for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
                SectionType sectionType = entry.getKey();
                String content;
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        content = ((TextSection) entry.getValue()).getContent();
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        content = ((ListSection) entry.getValue()).getContent().stream()
                                .reduce("", (a, b) -> a + "\n" + b).trim();
                        break;
                    default:
                        continue;
                }
                sectionStatement.setString(1, sectionType.name());
                sectionStatement.setString(2, content);
                sectionStatement.setString(3, uuid);
                sectionStatement.addBatch();
            }
            sectionStatement.executeBatch();
        }
    }

    private void addCont(ResultSet set, Resume resume) throws SQLException {
        String value = set.getString("value");
        if (value != null) {
            resume.addContact(ContactType.valueOf(set.getString("type")), value);
        }
    }

    private void addContactsToResume(ResultSet set, Resume resume) throws SQLException {
        String uuid = resume.getUuid();
        while (set.next()) {
            if (!(set.getString("resume_uuid").equals(uuid))) {
                set.previous();
                return;
            }
            String value = set.getString("value");
            if (value != null) {
                resume.addContact(ContactType.valueOf(set.getString("type")), value);
            }
        }
    }

    private void addTextSectionsToResume(ResultSet set, Resume resume) throws SQLException {
        String uuid = resume.getUuid();
        while (set.next()) {
            if (!set.getString("resume_uuid").equals(uuid)) {
                set.previous();
                return;
            }
            SectionType type = SectionType.valueOf(set.getString("type"));
            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    resume.addSection(type, new TextSection(set.getString("info")));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    List<String> list = Arrays.asList(set.getString("info").split("\n"));
                    resume.addSection(type, new ListSection(list));
                    break;
            }
        }
    }

    private boolean checkString(String string) {
        return string != null && string.trim().length() > 0;
    }

    private void insertOrganizationsFromResume(Resume resume, Connection connection, String uuid) throws SQLException {
        for (Map.Entry<SectionType, AbstractSection> sectEntry :
                resume.getSections().entrySet()) {
            if (sectEntry.getKey().equals(SectionType.EXPERIENCE) || sectEntry.getKey().equals(SectionType.EDUCATION)) {
                OrganizationSection sect = (OrganizationSection) sectEntry.getValue();
                try (PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO organization(ORGANIZATION_NAME, URL) VALUES (?, ?) ON CONFLICT DO NOTHING;")) {
                    for (Organization org :
                            sect.getContent()) {
                        Link orgLink = org.getOrganizationName();
                        if (!checkString(orgLink.getName())) {
                            throw new StorageException("Organization must contain a name!");
                        }
                        preparedStatement.setString(1, orgLink.getName());
                        preparedStatement.setString(2, orgLink.getUrl());
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO position (position,start_date, end_date, organization_name, resume_uuid, type, info) " +
                                "VALUES (?,?,?,?,?,?,?)")) {
                    for (Organization org :
                            sect.getContent()) {
                        for (Organization.Position pos :
                                org.getPositions()) {
                            preparedStatement.setString(1, pos.getPosition());
                            preparedStatement.setObject(2, pos.getDateOfStart(), Types.DATE);
                            preparedStatement.setObject(3, pos.getDateOfEnd(), Types.DATE);
                            preparedStatement.setString(4, org.getOrganizationName().getName());
                            preparedStatement.setString(5, uuid);
                            preparedStatement.setString(6, sectEntry.getKey().name());
                            preparedStatement.setString(7, pos.getInfo());
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                }
            }
        }
    }

}



