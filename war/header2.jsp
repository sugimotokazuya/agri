<%@page pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<div class="header">
	<div class="toggle">
		<button type="button" id="toggleButton">
			<i class="fa fa-bars"></i>
		</button>
	</div>
	<div class="header-title">
		<h1>
			<i class="fa fa-sun-o"></i>
			<i class="fa fa-cloud"></i>
			あぐりクラウドβ
		</h1>
	</div>
	<c:if test="${sessionScope.userName != null}">
		<div class="header-name">
			【${sessionScope.yagoName} / ${sessionScope.userName}】
			<c:if test="${!empty(sessionScope.superYago) && loginUser.yagoRef.model != sessionScope.superYago}">
			（${sessionScope.superYago.name}）
			</c:if>
		</div>
	</c:if>
</div>