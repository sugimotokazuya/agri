<%@page pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<div class="header">あぐりクラウドβ
	<c:if test="${sessionScope.userName != null}">
		【${sessionScope.yagoName} / ${sessionScope.userName}】
		<a style="color:white;font-weight:bold;" href="${f:url('/logout')}">ログアウト</a>
	</c:if>
</div>