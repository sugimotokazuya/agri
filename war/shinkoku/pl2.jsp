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
<title>損益計算表(科目・月別）</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">損益計算表(科目・月別）</div>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('./')}';"/><hr/>
<form action="${f:url('pl2')}" method="post">
照会年月：
			<select name="year">
				<option ${f:select("year", "2015")}>2015年</option>
				<option ${f:select("year", "2016")}>2016年</option>
			</select>
			
			<select name="month">
				<option ${f:select("month", "0")}>1月</option>
				<option ${f:select("month", "1")}>2月</option>
				<option ${f:select("month", "2")}>3月</option>
				<option ${f:select("month", "3")}>4月</option>
				<option ${f:select("month", "4")}>5月</option>
				<option ${f:select("month", "5")}>6月</option>
				<option ${f:select("month", "6")}>7月</option>
				<option ${f:select("month", "7")}>8月</option>
				<option ${f:select("month", "8")}>9月</option>
				<option ${f:select("month", "9")}>10月</option>
				<option ${f:select("month", "10")}>11月</option>
				<option ${f:select("month", "11")}>12月</option>
			</select>
<button type="submit">照会</button>
</form>
<table>

<tbody>
<c:forEach var="dto" items="${shisanList}" varStatus="s">
<tr>
<c:if test="${s.index==0}"><td rowspan="${fn:length(shisanList)}">資産</td></c:if>
<td><c:out value="${dto.kamoku.name}"/></td>
<td class="num"><fmt:formatNumber value="${dto.value}" pattern="###,###,##0"/></td>
</tr>
</c:forEach>

<c:forEach var="dto" items="${fusaiList}" varStatus="s">
<tr>
<c:if test="${s.index==0}"><td rowspan="${fn:length(fusaiList)}">負債</td></c:if>
<td><c:out value="${dto.kamoku.name}"/></td>
<td class="num"><fmt:formatNumber value="${dto.value}" pattern="###,###,##0"/></td>
</tr>
</c:forEach>

<c:forEach var="dto" items="${shunyuList}" varStatus="s">
<tr>
<c:if test="${s.index==0}"><td rowspan="${fn:length(shunyuList)}">収入</td></c:if>
<td><c:out value="${dto.kamoku.name}"/></td>
<td class="num"><fmt:formatNumber value="${dto.value}" pattern="###,###,##0"/></td>
</tr>
</c:forEach>

<c:forEach var="dto" items="${shishutsuList}" varStatus="s">
<tr>
<c:if test="${s.index==0}"><td rowspan="${fn:length(shishutsuList)}">支出</td></c:if>
<td><c:out value="${dto.kamoku.name}"/></td>
<td class="num"><fmt:formatNumber value="${dto.value}" pattern="###,###,##0"/></td>
</tr>
</c:forEach>

</tbody>
</table>

</body>
</html>
