package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResumeServlet extends HttpServlet {

    private Storage storage = Config.get().getStorage();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try(PrintWriter writer = response.getWriter()){
            writer.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title>HTML Table with a Header, Footer and Body</title>\n" +
                    "    <style>\n" +
                    "        table {\n" +
                    "            width: 300px;\n" +
                    "            border-collapse: collapse;\n" +
                    "        }\n" +
                    "        table, th, td {\n" +
                    "            border: 1px solid black;\n" +
                    "        }\n" +
                    "        th, td {\n" +
                    "            padding: 10px;\n" +
                    "            text-align: left;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <table>\n" +
                    "        <thead>\n" +
                    "            <tr>\n" +
                    "                <th>Uuid</th>\n" +
                    "                <th>Email</th>\n" +
                    "            </tr>\n" +
                    "        </thead>\n" +
                    "        <tbody>\n");
            for (Resume resume :
                    storage.getAllSorted()) {
                String builder = "<tr><td>" +
                        "<a href=\"resume?uuid="+resume.getUuid()+"\">"+resume.getFullName() + "</a>\n"+
                        "</td><td>" +
                        resume.getContacts().get(ContactType.MAIL) +
                        "</td></tr>";
                writer.println(builder);
            }
            writer.println("      </tfoot>\n" +
                    "    </table>\n" +
                    "</body>\n" +
                    "</html>");
            
        }
    }
}
