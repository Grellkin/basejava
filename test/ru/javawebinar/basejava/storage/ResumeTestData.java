package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.*;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResumeTestData {

    protected static final String UUID_1 = "UIID_1";
    protected static final String UUID_2 = "UIID_2";
    protected static final String UUID_3 = "UIID_3";

    protected static final String NAME_1 = "Anika";
    protected static final String NAME_3 = "Carko";
    protected static final String NAME_2 = "Bolo";

    protected static final Resume FIRST_RESUME = new Resume(UUID_1, NAME_1);
    protected static final Resume SECOND_RESUME = new Resume(UUID_2, NAME_2);
    protected static final Resume THIRD_RESUME = new Resume(UUID_3, NAME_3);

    protected static final TextSection TEXT_SECTION_PERSONAL_ONE = new TextSection("Personal info 1");
    protected static final TextSection TEXT_SECTION_PERSONAL_TWO = new TextSection("Personal info 2");
    protected static final TextSection TEXT_SECTION_PERSONAL_THREE = new TextSection("Personal info 3");

    protected static final TextSection TEXT_SECTION_OBJECTIVE_ONE = new TextSection("Objective info 1");
    protected static final TextSection TEXT_SECTION_OBJECTIVE_TWO = new TextSection("Objective info 2");
    protected static final TextSection TEXT_SECTION_OBJECTIVE_THREE = new TextSection("Objective info 3");

    protected static final List<String> ACHIEVEMENT_1 = Arrays.asList("Achievement of first person.", "Another achiev");
    protected static final List<String> ACHIEVEMENT_2 = Arrays.asList("Achievement of second person.", "Another achiev");
    protected static final List<String> ACHIEVEMENT_3 = Arrays.asList("Achievement of second person.", "Another achiev");

    protected static final List<String> QUALIFICATION_1 = Arrays.asList("Qualification of first person.", "Another achiev");
    protected static final List<String> QUALIFICATION_2 = Arrays.asList("Qualification of second person.", "Another achiev");
    protected static final List<String> QUALIFICATION_3 = Arrays.asList("Qualification of third person.", "Another achiev");

    protected static final Organization.Position POSITION_1 =
            new Organization.Position(1999, Month.of(2), "Cook", "Best cook");
    protected static final Organization.Position POSITION_2 =
            new Organization.Position(2012, Month.of(3), "Waiter", "Best waiter");
    protected static final Organization.Position POSITION_3 =
            new Organization.Position(1984, Month.of(4), "Painter", "Best painter");
    protected static final Organization.Position POSITION_4 =
            new Organization.Position(2011, Month.of(5), "Pirate", "Best pirate");
    protected static final Organization.Position POSITION_5 =
            new Organization.Position(1999, Month.of(11), "Fighter", "Best fighter");

    protected static final Organization ORGANIZATION_1 =
            new Organization("PANCAKE","",  POSITION_1, POSITION_3);
    protected static final Organization ORGANIZATION_2 =
            new Organization("Heart","someUrl",  POSITION_2);
    protected static final Organization ORGANIZATION_3 =
            new Organization("Dance hill","www.danceHill.com",  POSITION_4);
    protected static final Organization ORGANIZATION_4 =
            new Organization("ULA","www.ULAoma.com",  POSITION_1, POSITION_2);
    protected static final Organization ORGANIZATION_5 =
            new Organization("Uncle Vova","www.dadyavova.com", POSITION_5);
    


    public static void createSections() {
               
        FIRST_RESUME.addSection(SectionType.PERSONAL, TEXT_SECTION_PERSONAL_ONE);
        FIRST_RESUME.addSection(SectionType.OBJECTIVE, TEXT_SECTION_OBJECTIVE_ONE);
        FIRST_RESUME.addSection(SectionType.ACHIEVEMENT, new ListSection(ACHIEVEMENT_1));
        FIRST_RESUME.addSection(SectionType.QUALIFICATIONS, new ListSection(QUALIFICATION_1));
        FIRST_RESUME.addSection(SectionType.EXPERIENCE, new OrganizationSection(ORGANIZATION_1));

        SECOND_RESUME.addSection(SectionType.PERSONAL, TEXT_SECTION_PERSONAL_TWO);
        SECOND_RESUME.addSection(SectionType.OBJECTIVE, TEXT_SECTION_OBJECTIVE_TWO);
        SECOND_RESUME.addSection(SectionType.ACHIEVEMENT, new ListSection(ACHIEVEMENT_2));
        SECOND_RESUME.addSection(SectionType.QUALIFICATIONS, new ListSection(QUALIFICATION_2));
        SECOND_RESUME.addSection(SectionType.EXPERIENCE, new OrganizationSection(ORGANIZATION_2, ORGANIZATION_3));
        
        THIRD_RESUME.addSection(SectionType.PERSONAL, TEXT_SECTION_PERSONAL_THREE);
        THIRD_RESUME.addSection(SectionType.OBJECTIVE, TEXT_SECTION_OBJECTIVE_THREE);
        THIRD_RESUME.addSection(SectionType.ACHIEVEMENT, new ListSection(ACHIEVEMENT_3));
        THIRD_RESUME.addSection(SectionType.QUALIFICATIONS, new ListSection(QUALIFICATION_3));
        THIRD_RESUME.addSection(SectionType.EXPERIENCE, new OrganizationSection(ORGANIZATION_4, ORGANIZATION_5));
    }

    public static void createContacts() {
        Map<ContactType, String> contacts1 = FIRST_RESUME.getContacts();
        contacts1.put(ContactType.PHONE, "123456789");
        contacts1.put(ContactType.MAIL, "ton@mail.ru");
        contacts1.put(ContactType.SKYPE, "antoniV44");

        Map<ContactType, String> contacts2 = SECOND_RESUME.getContacts();
        contacts2.put(ContactType.PHONE, "987654321");
        contacts2.put(ContactType.MAIL, "kika@mail.ru");
        contacts2.put(ContactType.SKYPE, "kikaVIrus");

        Map<ContactType, String> contacts3 = THIRD_RESUME.getContacts();
        contacts3.put(ContactType.PHONE, "234343546");
        contacts3.put(ContactType.MAIL, "kot@mail.ru");
        contacts3.put(ContactType.SKYPE, "kotikc");
    }
}
