<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
<link rel="stylesheet" type="text/css" href="/css/global.css"/>
<title>施肥設計</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">施肥設計</div>
<c:set var="url" value="./sokutei/index?year=${year}"/>
<input class="w50" type="button" value="施肥設計" onclick="window.location.href='${f:url(url)}';"/>
<input class="w50" type="button" value="肥料管理" onclick="window.location.href='${f:url('./hiryo/')}';"/>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('../start')}';"/>
</body>
</html>