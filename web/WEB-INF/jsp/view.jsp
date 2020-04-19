<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.String" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="java.time.LocalDate" %><%--
  Created by IntelliJ IDEA.
  User: varga
  Date: 12.04.2020
  Time: 15:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>All resumes</title>
    <link rel="stylesheet" href="css/tableStyle.css">
</head>
<body>
<jsp:include page="snippets/header.jsp"/>
<jsp:useBean id="resume" scope="request" type="ru.javawebinar.basejava.model.Resume"/>
<h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="images/edit.png" alt="Create new resume" width="20dp" height="20dp"></a></h2>
<address>
<c:forEach items="${resume.contacts}" var="cont">
    ${cont.key.title} : ${cont.value}<br>
</c:forEach>
</address>
<hr>
<h3>Персональная информация</h3>
<c:forEach items="${resume.sections}" var="sect">
    <c:set var="sectionType" value="${sect.key}"/>
    <h4>${sectionType.title}</h4>
    <p><c:choose>
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

        <c:when test="${sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)}">
            <c:set var="orgSec" value="${sect.value}"/>
            <jsp:useBean id="orgSec" type="ru.javawebinar.basejava.model.OrganizationSection"/>
            <c:forEach items="${orgSec.content}" var="organizaton">
              <p><b>${organizaton.organizationName.name}&nbsp&nbsp <a href="${organizaton.organizationName.url}">${organizaton.organizationName.url}</a> </b>
                  <c:forEach items="${organizaton.positions}" var="positon">
                    <br>${positon.position} : ${positon.dateOfStart} - <c:choose>
                       <c:when test="${positon.dateOfEnd.equals(LocalDate.of(3000, 1, 1))}">
                           <i>still work</i>
                       </c:when>
                        <c:when test="${!positon.dateOfEnd.equals(LocalDate.of(3000, 1, 1))}">
                            ${positon.dateOfEnd}
                        </c:when></c:choose><br>
                        ${positon.info}
                    <br>
                </c:forEach>
                
            </c:forEach>
        </c:when>
    </c:choose>

</c:forEach>

<jsp:include page="snippets/footer.jsp"/>
</body>
</html>
