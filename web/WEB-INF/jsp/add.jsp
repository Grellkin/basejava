<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %><%--
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <style>
        header {
            background: none repeat scroll 0 0 #3de2aa;
            color: #ebdbb2;
            font-size: 20px;
            padding: 5px 20px;
        }

        footer {
            background: none repeat scroll 0 0 #3de2aa;
            color: #669E73;
            font-size: 20px;
            padding: 5px 20px;
            margin: 20px 0;
        }

        dl {
            background: none repeat scroll 0 0 #FAFAFA;
            margin: 8px 0;
            padding: 0;
        }

        dt {
            display: inline-block;
            width: 170px;
        }

        dd {
            display: inline-block;
            margin-left: 8px;
            vertical-align: top;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/snippets/header.jsp"></jsp:include>
<h1>Create resume</h1>
<form action="/resume" method="post">
    <dl>
        <dt>Full Name</dt>
        <dd><input type="text" name="fullName" id="fullName"></dd>
    </dl>
    <c:forEach items="${ContactType.values()}" var="cont">
        <dl>
            <dt>${cont.title}</dt>
            <dd><input type="text" name="${cont}"></dd>
        </dl>
    </c:forEach>

    <h3>Персональная информация</h3>
    <c:forEach items="${SectionType.values()}" var="sectionName">
        <h4>${sectionName.title}</h4>
        <jsp:useBean id="sectionName" type="ru.javawebinar.basejava.model.SectionType"/>
        <c:choose>
            <c:when test="${sectionName.equals(SectionType.PERSONAL) || sectionName.equals(SectionType.OBJECTIVE)}">
                <p><textarea rows="3" cols="40" name="${sectionName}"></textarea></p>
            </c:when>
            <c:when test="${sectionName.equals(SectionType.QUALIFICATIONS) || sectionName.equals(SectionType.ACHIEVEMENT)}">
                <p><textarea rows="3" cols="40" name="${sectionName}"></textarea></p>
            </c:when>
        </c:choose>
    </c:forEach>
    <input type="submit" value="Submit">
    <input type="reset" value="Reset">
</form>

<jsp:include page="/WEB-INF/jsp/snippets/footer.jsp"></jsp:include>
</body>
</html>
