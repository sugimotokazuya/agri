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
<title>科目別摘要集計-${f:h(kamoku.name)}(${year})</title>
</head>
<body>
<table border="1">
<thead>
<tr><th colspan="2">${f:h(kamoku.name)}</th><th colspan="2">${f:h(kamoku.kubunName)}</th></tr>
<tr><th>摘要</th><th>借方金額</th><th>貸方金額</th><th>計</th></tr>
</thead>
<tbody>
<c:set var="sumKari" value="0"/>
<c:set var="sumKashi" value="0"/>
<c:forEach var="e" items="${shukeiList}">
<tr>
<td>${f:h(e.tekiyo.name)}</td>
<td class="num"><fmt:formatNumber value="${e.karikata}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${e.kashikata}" pattern="###,###,##0"/></td>
<c:if test="${kamoku.kubun==1}">
<td class="num"><fmt:formatNumber value="${e.karikata-e.kashikata}" pattern="###,###,##0"/></td>
</c:if>
<c:if test="${kamoku.kubun==2 || kamoku.kubun==3}">
<td class="num"><fmt:formatNumber value="${e.kashikata-e.karikata}" pattern="###,###,##0"/></td>
</c:if>
</tr>
<c:set var="sumKari" value="${sumKari+e.karikata}"/>
<c:set var="sumKashi" value="${sumKashi+e.kashikata}"/>
</c:forEach>
<c:forEach var="e" items="${kashikata}">
<tr>
<td>${f:h(e.tekiyo.name)}</td>
<td class="num"><fmt:formatNumber value="${e.karikata}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${e.kashikata}" pattern="###,###,##0"/></td>
<c:if test="${kamoku.kubun==1}">
<td class="num"><fmt:formatNumber value="${e.karikata-e.kashikata}" pattern="###,###,##0"/></td>
</c:if>
<c:if test="${kamoku.kubun==2 || kamoku.kubun==3}">
<td class="num"><fmt:formatNumber value="${e.kashikata-e.karikata}" pattern="###,###,##0"/></td>
</c:if></tr>
<c:set var="sumKari" value="${sumKari+e.karikata}"/>
<c:set var="sumKashi" value="${sumKashi+e.kashikata}"/>
</c:forEach>
<tr>
<td>計</td>
<td class="num"><fmt:formatNumber value="${sumKari}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${sumKashi}" pattern="###,###,##0"/></td>
<c:if test="${kamoku.kubun==1}">
<td class="num"><fmt:formatNumber value="${sumKari-sumKashi}" pattern="###,###,##0"/></td>
</c:if>
<c:if test="${kamoku.kubun==2 || kamoku.kubun==3}">
<td class="num"><fmt:formatNumber value="${sumKashi-sumKari}" pattern="###,###,##0"/></td>
</c:if>
</tr>
</tbody>
</table>
</body>
</html>
