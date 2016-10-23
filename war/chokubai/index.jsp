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
<title>直売店舗</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">直売店舗管理</div>
取引先：<select onchange="submit();" id="torihikisakiRef" name="torihikisakiRef" style="">
<c:forEach var="e" items="${tList}">
	<option ${f:select("torihikisakiRef", f:h(e.key))}>${f:h(e.name)}</option>
</c:forEach>
</select>

<table>
<thead>
<tr><th>直売店舗名</th><th>並び順</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${list}">
<tr>
<td>${f:h(e.name)}</td>
<td>${f:h(e.order)}</td>
<c:if test="${loginUser.authTorihikisakiEdit}">
<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
<td><a href="${f:url(editUrl)}">変更</a></td>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
</c:if>
</tr>
</c:forEach>
</tbody>
</table>

<hr/>
<c:if test="${loginUser.authTorihikisakiEdit}">
<input class="w50" type="button" value="直売店登録" onclick="window.location.href='${f:url('create')}';"/>
</c:if>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('../torihikisaki')}';"/>
</body>
</html>
