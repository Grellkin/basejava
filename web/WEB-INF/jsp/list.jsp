<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %><%--
  Created by IntelliJ IDEA.
  User: varga
  Date: 12.04.2020
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>All resumes</title>
    <link rel="stylesheet" href="css/tableStyle.css">
</head>
<body>
<jsp:include page="snippets/header.jsp"/>
<h2>All resumes&nbsp<a href="/resume?uuid=createUUID&action=edit"><img src="images/add.png" alt="Add new Resume"
                                                                       width="20dp" height="20dp"></a></h2>
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${resumes}" var="resume">
        <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume"/>
        <tr>
            <td>
                <a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a>
            </td>
            <td>
                <a href="${resume.contacts.get(ContactType.MAIL)}">${resume.contacts.get(ContactType.MAIL)}</a>
            </td>
            <td>
                <a href="resume?uuid=${resume.uuid}&action=delete"><img src="images/delete.png" alt="delete"
                                                                        width="25dp" height="25dp"></a>
            </td>
            <td>
                <a href="resume?uuid=${resume.uuid}&action=edit"><img src="images/edit.png" alt="edit" width="25dp"
                                                                      height="25dp"></a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<jsp:include page="snippets/footer.jsp"/>
</body>
</html>
