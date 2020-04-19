package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResumeServlet extends HttpServlet {

    private Storage storage = Config.get().getStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Resume resume;
        String uuid = request.getParameter("uuid");
        if (uuid != null && uuid.trim().length() != 0) {
            resume = new Resume(request.getParameter("uuid"), request.getParameter("fullName"));
        } else {
            resume = new Resume();
            resume.setFullName(request.getParameter("fullName"));
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                AbstractSection section;
                switch (type){
                    case PERSONAL:
                    case OBJECTIVE:
                        section = new TextSection(value);
                        resume.addSection(type, section);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        section = new ListSection(Arrays.asList(value.split("\n")));
                        resume.addSection(type, section);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");
                        String[] values = request.getParameterValues(type.name());
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (isNotEmpty(name)) {
                                List<Organization.Position> positions = new ArrayList<>();
                                String pfx = type.name() + i;
                                String[] startDates = request.getParameterValues(pfx + "startDate");
                                String[] endDates = request.getParameterValues(pfx + "endDate");
                                String[] titles = request.getParameterValues(pfx + "position");
                                String[] descriptions = request.getParameterValues(pfx + "info");
                                for (int j = 0; j < titles.length; j++) {
                                    if (isNotEmpty(titles[j])) {
                                        positions.add(new Organization.Position(
                                                LocalDate.parse(startDates[j]), LocalDate.parse(endDates[j]), titles[j], descriptions[j] ));
                                    }
                                }
                                orgs.add(new Organization(new Link(name, urls[i]), positions));
                            }
                        }
                        resume.addSection(type, new OrganizationSection(orgs));
                        break;
                }
            } else {
                resume.getSections().remove(type);
            }
        }
        if (uuid != null && uuid.trim().length() != 0) {
            storage.update(resume);
        } else {
            storage.save(resume);
        }

        response.sendRedirect("/resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Organization.Position emptyPos = new Organization.Position();
        Organization emptyOrg = new Organization();
        String uuid = request.getParameter("uuid");
        if (uuid != null && uuid.trim().length() != 0) {
            Resume resume;
            if (uuid.equals("createUUID")){
                resume = new Resume();
                storage.save(resume);
            } else{
                resume = storage.get(uuid);
            }
            switch (request.getParameter("action")) {
                case "view":
                    request.setAttribute("resume", resume);
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(request, response);
                    break;
                case "delete":
                    storage.delete(uuid);
                    break;
                case "edit":
                    request.setAttribute("resume", resume);
                    for (SectionType type :
                            SectionType.values()) {
                        AbstractSection section = resume.getSections().get(type);
                        if (section == null){
                            switch (type){
                                case PERSONAL:
                                case OBJECTIVE:
                                    section = new TextSection("");
                                    break;
                                case ACHIEVEMENT:
                                case QUALIFICATIONS:
                                    section = new ListSection(new ArrayList<>());
                                    break;
                                case EXPERIENCE:
                                case EDUCATION:
                                    section = new OrganizationSection(new ArrayList<>());
                                    break;
                            }
                            resume.addSection(type, section);
                        }
                    }
                    OrganizationSection workSect =(OrganizationSection) resume.getSections().get(SectionType.EXPERIENCE);
                    OrganizationSection eduSect =(OrganizationSection) resume.getSections().get(SectionType.EDUCATION);
                    workSect.getContent().add(emptyOrg);
                    eduSect.getContent().add(emptyOrg);
                    List<Organization> newList = Stream.concat(workSect.getContent().stream(), eduSect.getContent().stream())
                            .collect(Collectors.toList());
                    for (Organization org : newList) {
                        List<Organization.Position> positions = org.getPositions();
                        if (positions == null){
                            positions = new ArrayList<>();
                            org.setPositions(positions);
                        } else {
                            positions.add(emptyPos);
                        }
                    }
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
                    break;
                default:
            }
        }
        request.setAttribute("resumes", storage.getAllSorted());
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
    }

    private static boolean isNotEmpty(String test){
        return test != null && test.trim().length() != 0;
    }
}




