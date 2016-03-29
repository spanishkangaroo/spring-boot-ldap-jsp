<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<body onload='document.f.j_username.focus();'>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="${pageContext.request.contextPath}">Web</a>
			</div>
		</div>
	</nav>

	<div class="container margin-top5pc">
		<div class="panel panel-danger">
			<div class="panel-heading">
				<spring:message code="access"></spring:message>
			</div>
			<div class="panel-body">
				<form name='f' action='${pageContext.request.contextPath}/login' method='POST'>

					<c:if test="${not empty error}">
						<div class="form-group">
							<div class="label label-warning" role="alert">${error}</div>
						</div>
					</c:if>

					<div class="form-group">
						<label for="j_username"><spring:message code="user" /></label> <input
							name="username" id="j_username" type="text" class="form-control" />
					</div>

					<div class="form-group">
						<label for="j_password"><spring:message code="password" /></label>
						<input name="password" id="j_password" type="password"
							class="form-control" />
					</div>

					<div class="form-group">
						<button name="submit" type="submit" class="btn btn-primary"
							role="button">
							<spring:message code="access" />
						</button>
					</div>

					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />

				</form>
			</div>
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