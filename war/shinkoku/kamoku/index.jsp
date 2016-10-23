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
<title>科目管理</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">科目管理</div>
<table>
<thead>
<tr><th>区分</th><th>科目名</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${kamokuList}">
<tr>
<td>${f:h(e.kubunName)}</td>
<td>${f:h(e.name)}</td>
<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
<td><a href="${f:url(editUrl)}">変更</a></td>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
<td><a href="../tekiyo/index?key=${f:h(e.key)}&version=${e.version}">摘要</a></td>
</tr>
</c:forEach>
</tbody>
</table>
<hr/>
<input class="w50" type="button" value="科目登録" onclick="window.location.href='${f:url('create')}';"/>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('../')}';"/>
</body>
</html>
