<%@page pageEncoding="UTF-8" isELIgnored="false" session="true"%>
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
<title>仕訳照会-仕訳帳</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">仕訳照会-仕訳帳(${sessionScope.shiwakeViewYear}年)</div>
<input class="w100" type="button" value="戻る" onclick="window.location.href='${f:url('menu')}';"/><hr/>
<table>
<thead>
<tr><th>日付</th><th>借方科目</th><th>貸方科目</th><th>金額</th><th>摘要</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${shiwakeList}">
<tr>
<td><fmt:formatDate value="${e.hiduke}" pattern="yyyy/MM/dd"/></td>
<td>${f:h(e.karikataKamokuRef.model.name)}</td>
<td>${f:h(e.kashikataKamokuRef.model.name)}</td>
<td class="num"><fmt:formatNumber value="${e.karikataKingaku}" pattern="###,###,##0"/></td>
<td>${f:h(e.tekiyoRef.model.name)}：${f:h(e.tekiyoText)}</td>
<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
</tr>
</c:forEach>
</tbody>
</table>
<hr/>
<input class="w100" type="button" value="戻る" onclick="window.location.href='${f:url('menu')}';"/>
</body>
</html>
