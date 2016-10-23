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
<link rel="stylesheet" type="text/css" href="/css/print.css"/>
<title>科目別整理帳(${sessionScope.shiwakeViewYear}年)-${f:h(kamoku.kubunName)}：${f:h(kamoku.name)}</title>
</head>
<body>
<table border="1">
<thead>
<tr><th>日付</th><th>科目</th><th>摘要</th><th>借方金額</th><th>貸方金額</th>
<th><c:if test="${kamoku.kubun==1 || kamoku.kubun==2}">残高</c:if>
<c:if test="${kamoku.kubun==3 || kamoku.kubun==4}">累計</c:if></th></tr>
</thead>
<tbody>
<c:set var="ruikei" value="0"/>
<c:forEach var="e" items="${shiwakeList}">
<c:if test="${kamoku.key==e.karikataKamokuRef.model.key || kamoku.key==e.kashikataKamokuRef.model.key}">
<c:if test="${kamoku.key==e.karikataKamokuRef.model.key}">
	<c:if test="${kamoku.kubun==1}"><c:set var="ruikei" value="${ruikei+e.kashikataKingaku}"/></c:if>
	<c:if test="${kamoku.kubun==2}"><c:set var="ruikei" value="${ruikei-e.kashikataKingaku}"/></c:if>
</c:if>
<c:if test="${kamoku.key==e.kashikataKamokuRef.model.key}">
	<c:if test="${kamoku.kubun==1}"><c:set var="ruikei" value="${ruikei-e.karikataKingaku}"/></c:if>
	<c:if test="${kamoku.kubun==2}"><c:set var="ruikei" value="${ruikei+e.karikataKingaku}"/></c:if>
</c:if>
<c:if test="${kamoku.kubun==3 && e.kashikataKamokuRef.model.kubun==3}"><c:set var="ruikei" value="${ruikei+e.kashikataKingaku}"/></c:if>
<c:if test="${kamoku.kubun==4 && e.karikataKamokuRef.model.kubun==4}"><c:set var="ruikei" value="${ruikei+e.karikataKingaku}"/></c:if>
<tr>
<td><fmt:formatDate value="${e.hiduke}" pattern="yyyy/MM/dd"/></td>
<td><c:if test="${kamoku.key==e.karikataKamokuRef.model.key}">${f:h(e.kashikataKamokuRef.model.name)}</c:if>
<c:if test="${kamoku.key==e.kashikataKamokuRef.model.key}">${f:h(e.karikataKamokuRef.model.name)}</c:if></td>
<td>${f:h(e.tekiyoRef.model.name)}：${f:h(e.tekiyoText)}</td>
<td class="num"><c:if test="${kamoku.key==e.karikataKamokuRef.model.key}">
<fmt:formatNumber value="${e.karikataKingaku}" pattern="###,###,##0"/></c:if></td>
<td class="num">
	<c:if test="${kamoku.key==e.kashikataKamokuRef.model.key}">
		<fmt:formatNumber value="${e.kashikataKingaku}" pattern="###,###,##0"/>
		<c:if test="${kamoku.kubun==4}">
			<c:set var="ruikei" value="${ruikei-e.kashikataKingaku}"/>
		</c:if>
	</c:if>
</td>
<td class="num"><fmt:formatNumber value="${ruikei}" pattern="###,###,##0"/></td>
<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
</tr>
</c:if>
</c:forEach>
</tbody>
</table>
</body>
</html>
