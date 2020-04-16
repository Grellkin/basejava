package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class ResumeServlet extends HttpServlet {

    private Storage storage = Config.get().getStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String uuid = request.getParameter("uuid");
        if (uuid != null && uuid.trim().length() != 0) {
            Resume resume = storage.get(uuid);
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
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
                    break;
                default:
            }
        }
        request.setAttribute("resumes", storage.getAllSorted());
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
    }
}
