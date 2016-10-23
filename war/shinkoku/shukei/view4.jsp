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
<jsp:include page="/header.jsp"/>
<div class="title">科目別摘要集計-${f:h(kamoku.name)}(${year})</div>
<input class="w100" type="button" value="戻る" onclick="window.location.href='${f:url('./')}';"/><hr/>
<table>
<thead>
<tr><th colspan="2">${f:h(kamoku.name)}</th><th colspan="5">${f:h(kamoku.kubunName)}</th></tr>
<tr><th>摘要</th><th>借方金額</th><th>貸方金額</th><th>計</th><th>事業<br/>割合(%)</th><th>算入金額</th><th>控分差額</th></tr>
</thead>
<tbody>
<c:set var="sumKari" value="0"/>
<c:set var="sumKashi" value="0"/>
<c:set var="sumSannyu" value="0"/>
<c:set var="sumKobunsagaku" value="0"/>
<c:forEach var="e" items="${shukeiList}">
<tr>
<td>${f:h(e.tekiyo.name)}</td>
<td class="num"><fmt:formatNumber value="${e.karikata}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${e.kashikata}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${e.karikata-e.kashikata}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${e.tekiyo.wariai}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${e.sannyu}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${e.karikata-e.sannyu}" pattern="###,###,##0"/></td>
</tr>
<c:set var="sumKari" value="${sumKari+e.karikata}"/>
<c:set var="sumKashi" value="${sumKashi+e.kashikata}"/>
<c:set var="sumSannyu" value="${sumSannyu+e.sannyu}"/>
<c:set var="sumKobunsagaku" value="${sumKobunsagaku+e.karikata-e.sannyu}"/>
</c:forEach>
<tr>
<td>計</td>
<td class="num"><fmt:formatNumber value="${sumKari}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${sumKashi}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${sumKari-sumKashi}" pattern="###,###,##0"/></td>
<td class="num">-</td>
<td class="num"><fmt:formatNumber value="${sumSannyu}" pattern="###,###,##0"/></td>
<td class="num"><fmt:formatNumber value="${sumKobunsagaku}" pattern="###,###,##0"/></td>
</tr>
</tbody>
</table>

<hr/>
<input class="w100" type="button" value="戻る" onclick="window.location.href='${f:url('./')}';"/>
</body>
</html>
