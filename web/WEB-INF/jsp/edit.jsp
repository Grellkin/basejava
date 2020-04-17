<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %><%--
  Created by IntelliJ IDEA.
  User: varga
  Date: 12.04.2020
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All resumes</title>
    <link rel="stylesheet" href="css/tableStyle.css">
</head>
<body>
<jsp:include page="snippets/header.jsp"></jsp:include>


<h1>View of resume</h1>

<jsp:useBean id="resume" scope="request" type="ru.javawebinar.basejava.model.Resume"/>
<form action="resume" method="post">
    <input type="hidden" name="uuid" value="${resume.uuid}">
    <dl>
        <dt>Full Name</dt>
        <dd><input type="text" name="fullName" id="fullName" value="${resume.fullName}"></dd>
    </dl>
    <c:forEach items="${ContactType.values()}" var="cont">
        <dl>
            <dt>${cont.title}</dt>
            <dd><input type="text" name="${cont}" value="${resume.contacts.get(cont)}"></dd>
        </dl>
    </c:forEach>
    <h3>Персональная информация</h3>
    <c:forEach items="${SectionType.values()}" var="sect">
        <c:set var="resumeSect" value="${resume.sections.get(sect)}"/>
        <h4>${sect.title}</h4>
        <c:choose>
            <c:when test="${sect.equals(SectionType.PERSONAL) || sect.equals(SectionType.OBJECTIVE)}">
                <p><textarea rows="3" cols="40" name="${sect}"><c:if
                        test="${resumeSect != null}">${resumeSect}</c:if></textarea></p>
            </c:when>
            <c:when test="${sect.equals(SectionType.QUALIFICATIONS) || sect.equals(SectionType.ACHIEVEMENT)}">
                <p><textarea rows="3" cols="40" name="${sect}"> <c:if test="${resumeSect != null}"><c:set var="listSec"
                                                                                                          value="${resume.sections.get(sect)}"/><jsp:useBean id="listSec" type="ru.javawebinar.basejava.model.ListSection"/><c:forEach items="${listSec.content}" var="item">${item}</c:forEach></c:if></textarea></p>
            </c:when>
        </c:choose>
    </c:forEach>
    <input type="submit" value="Submit">
    <input type="reset" value="Reset">
</form>

<jsp:include page="snippets/footer.jsp"></jsp:include>
</body>
</html>
