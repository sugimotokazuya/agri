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
<title>直売店舗登録</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">直売店舗登録</div>
<form action="${f:url('insert')}" method="post">
取引先：<select id="torihikisakiRef" name="torihikisakiRef" style="">
<c:forEach var="e" items="${tList}">
	<option ${f:select("torihikisakiRef", f:h(e.key))}>${f:h(e.name)}</option>
</c:forEach>
</select>
<hr/>
直売店舗名：
<input type="text" ${f:text("name")} class="${f:errorClass('name', 'err')}"/>
<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>
<hr/>
並び順：
<input type="text" ${f:text("order")} class="${f:errorClass('order', 'err')}"/>
<br/><span class="${f:errorClass('order', 'err')}">${f:h(errors.order)}</span>
<hr/>
<input class="w50" type="submit" value="直売店登録"/>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('./')}';"/>
</form>
</body>
</html>
