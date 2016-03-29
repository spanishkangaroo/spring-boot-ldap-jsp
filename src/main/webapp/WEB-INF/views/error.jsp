<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Web</title>

<spring:url value="/" var="contextPath" />
<spring:url value="/resources/css/main.css" var="mainCss" />
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss" />
<spring:url value="/resources/css/bootstrap-theme.min.css"
	var="bootstrapCssTheme" />

<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${bootstrapCssTheme}" rel="stylesheet" />
<link href="${mainCss}" rel="stylesheet" />
<head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="${contextPath}">Web</a>
			</div>
		</div>
	</nav>

	<div class="container margin-top5pc">
		<div class="panel panel-danger">
			<div class="panel-heading">Error</div>
			<div class="panel-body">
				<spring:message code="error.contactitdepartment" />
			</div>
			<!-- 	
				Failed URL: ${url}
   			 	Exception:  ${exception.message}
       		 	<c:forEach items="${exception.stackTrace}" var="ste">
       		 		${ste}
   				</c:forEach>
    		-->
		</div>	
	</div>

	<!-- Javascript -->
	<spring:url value="/resources/js/main.js" var="mainJs" />
	<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs" />
	<spring:url value="/resources/js/jquery-1.11.3.min.js" var="jqueryJs" />
	<spring:url value="/resources/js/jquery-ui-1.11.4.min.js"
		var="jqueryUiJs" />

	<script src="${jqueryJs}"></script>
	<script src="${jqueryUiJs}"></script>
	<script src="${bootstrapJs}"></script>
	<script src="${mainJs}"></script>
</body>
</html>