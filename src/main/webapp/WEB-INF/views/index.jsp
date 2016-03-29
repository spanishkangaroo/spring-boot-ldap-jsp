<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
<spring:url value="/resources/css/jquery-bootstrap-datepicker.css"
	var="datePickerCss" />

<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${bootstrapCssTheme}" rel="stylesheet" />
<link href="${datePickerCss}" rel="stylesheet" />
<link href="${mainCss}" rel="stylesheet" />
<head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="${pageContext.request.contextPath}">Web</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="${pageContext.request.contextPath}/login?logout">Salir</a></li>
				</ul>
			</div>
		</div>
	</nav>

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