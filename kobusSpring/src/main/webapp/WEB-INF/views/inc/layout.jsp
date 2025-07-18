<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!DOCTYPE html>
<html>
<head>

<tiles:insertAttribute name="script" />

<tiles:insertAttribute name="pageScript" />

</head>
<body class="KO">
<tiles:insertAttribute name="header" />

<tiles:insertAttribute name="content" />

<tiles:insertAttribute name="footer" />
		</body>
</html>