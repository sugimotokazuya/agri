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
<title>仕訳照会</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">仕訳照会</div>
<form action="${f:url('view')}" method="post">
照会年：
<select name="year">
<c:forEach var="e" items="${yearList}">
<option ${f:select("year", f:h(e))}>${f:h(e)}</option>
</c:forEach>
</select><hr/>
科目：
<select name="kamoku">
<option value="">仕訳帳（全科目）</option>
<c:forEach var="e" items="${kamokuList}">
<c:if test="${e.kubun<5}">
<option ${f:select("kamoku", f:h(e.key))}>${f:h(e.name)}</option>
</c:if>
</c:forEach>
</select><hr/>
<input class="w50" type="submit" value="照会" onclick="this.form.target='_self'"/>
<input class="w50" type="submit" value="印刷" name="print" onclick="this.form.target='_blank'"/>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('../')}';"/>
</form>
</body>
</html>
