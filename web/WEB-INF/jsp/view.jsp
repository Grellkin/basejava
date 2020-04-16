<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.String" %>
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
<h2>${resume.fullName}</h2>
<c:forEach items="${resume.contacts}" var="cont">
    ${cont.key.title} : ${cont.value}<br>
</c:forEach>
<h3>Персональная информация</h3>
<c:forEach items="${resume.sections}" var="sect">
    <c:set var="sectionType" value="${sect.key}"/>
    <h4>${sectionType.title}</h4>
    <c:choose>
        <c:when test="${sectionType.equals(SectionType.PERSONAL) || sectionType.equals(SectionType.OBJECTIVE)}">
            ${sect.value}
        </c:when>
        <c:when test="${sectionType.equals(SectionType.QUALIFICATIONS) || sectionType.equals(SectionType.ACHIEVEMENT)}">
            <c:set var="listSec" value="${sect.value}"/>
            <jsp:useBean id="listSec" type="ru.javawebinar.basejava.model.ListSection"/>
            <c:forEach items="${listSec.content}" var="item">
                <li>${item}</li>
            </c:forEach>
        </c:when>
    </c:choose>
</c:forEach>
<jsp:include page="snippets/footer.jsp"></jsp:include>
</body>
</html>
