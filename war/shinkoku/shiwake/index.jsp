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
<title>仕訳入力</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">仕訳入力</div>
<form action="${f:url('create')}" method="post">
借方科目：
<select name="karikataKamoku" class="w60">
<c:forEach var="e" items="${kamokuList}">
<option ${f:select("karikataKamoku", f:h(e.key))}>${f:h(e.name)}</option>
</c:forEach>
</select><hr/>
貸方科目：
<select name="kashikataKamoku" class="w60">
<c:forEach var="e" items="${kamokuList}">
<option ${f:select("kashikataKamoku", f:h(e.key))}>${f:h(e.name)}</option>
</c:forEach>
</select><hr/>
<input class="w50" type="submit" value="仕訳入力"/>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('../')}';"/>
</form><hr/>
最新の１００件分を表示します。
<table>
<thead>
<tr><th>年度</th><th>日付</th><th>借方科目</th><th>貸方科目</th><th>金額</th><th>摘要</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${shiwakeList}">
<tr>
<td>${f:h(e.nendo)}年度</td>
<td><fmt:formatDate value="${e.hiduke}" pattern="yyyy-MM-dd"/></td>
<td>${f:h(e.karikataKamokuRef.model.name)}</td>
<td>${f:h(e.kashikataKamokuRef.model.name)}</td>
<td><fmt:formatNumber value="${e.karikataKingaku}" pattern="###,###,##0"/></td>
<td>${f:h(e.tekiyoRef.model.name)}：${f:h(e.tekiyoText)}</td>
<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
</tr>
</c:forEach>
</tbody>
</table>
</body>
</html>
