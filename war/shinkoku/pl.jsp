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
<title>損益計算表</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">損益計算表</div>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('./')}';"/><hr/>
<table>
<thead>
<tr><th colspan="2">　</th>
<c:forEach var="y" items="${years}">
<th><c:out value="${y}"/>年</th>
</c:forEach>
</tr>
</thead>
<tbody>
<c:forEach var="dto" items="${shisanList}" varStatus="s">
<tr>
<c:if test="${s.index==0}"><td rowspan="${fn:length(shisanList)}">資産</td></c:if>
<td><c:out value="${dto.kamoku.name}"/></td>
<c:forEach var="e" items="${dto.values}">
<td class="num"><fmt:formatNumber value="${e}" pattern="###,###,##0"/></td>
</c:forEach>
</tr>
</c:forEach>

<c:forEach var="dto" items="${fusaiList}" varStatus="s">
<tr>
<c:if test="${s.index==0}"><td rowspan="${fn:length(fusaiList)}">負債</td></c:if>
<td><c:out value="${dto.kamoku.name}"/></td>
<c:forEach var="e" items="${dto.values}">
<td class="num"><fmt:formatNumber value="${e}" pattern="###,###,##0"/></td>
</c:forEach>
</tr>
</c:forEach>

<c:forEach var="dto" items="${shunyuList}" varStatus="s">
<tr>
<c:if test="${s.index==0}"><td rowspan="${fn:length(shunyuList)}">収入</td></c:if>
<td><c:out value="${dto.kamoku.name}"/></td>
<c:forEach var="e" items="${dto.values}">
<td class="num"><fmt:formatNumber value="${e}" pattern="###,###,##0"/></td>
</c:forEach>
</tr>
</c:forEach>

<c:forEach var="dto" items="${shishutsuList}" varStatus="s">
<tr>
<c:if test="${s.index==0}"><td rowspan="${fn:length(shishutsuList)}">支出</td></c:if>
<td><c:out value="${dto.kamoku.name}"/></td>
<c:forEach var="e" items="${dto.values}">
<td class="num"><fmt:formatNumber value="${e}" pattern="###,###,##0"/></td>
</c:forEach>
</tr>
</c:forEach>

</tbody>
</table>

</body>
</html>
