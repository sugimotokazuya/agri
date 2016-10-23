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
<title>測定値管理</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">測定値管理</div>
<c:if test="${!empty(errorStr)}">
<span class="err">${f:h(errorStr)}</span>
<hr/>
</c:if>
<form action="${f:url('./')}" method="post">
照会年：
<select name="year" onchange="submit();">
<option value="">すべての年</option>
<c:forEach var="e" items="${years}">
<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
</c:forEach>
</select>
　　畑：
<select name="hatake" onchange="submit();">
<option value="">すべての畑</option>
<c:forEach var="e" items="${hatakeList}">
<option ${f:select("hatake", f:h(e.key))}>${f:h(e.name)}</option>
</c:forEach>
</select>
</form>
<table>
<thead>
<tr><th>日付</th><th>畑</th><th>品目</th><th>CEC</th><th>pH<br/>(H2O)</th><th>pH<br/>(KCl)</th>
<th>NH4N</th><th>NO3N</th><th>P2O</th><th>CaO</th><th>MgO</th><th>K2O</th><th>Fe</th>
<th>Mn</th><th>NaCl</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${sokuteiList}">
<tr>
<td><fmt:formatDate value="${e.date}" pattern="yyyy/MM/dd"/></td>
<td>${f:h(e.hatakeRef.model.name)}</td>
<td>${f:h(e.hinmoku)}</td>
<td class="num"><fmt:formatNumber value="${f:h((e.seibunCa+e.seibunMg+e.seibunK)/(-30.87+7.63*e.phH2o))}" pattern="##0.0" /></td>
<td class="num">${f:h(e.phH2o)}</td>
<td class="num">${f:h(e.phKcl)}</td>
<td class="num">${f:h(e.seibunNh4n)}</td>
<td class="num">${f:h(e.seibunNo3n)}</td>
<td class="num">${f:h(e.seibunP)}</td>
<td class="num">${f:h(e.seibunCa)}</td>
<td class="num">${f:h(e.seibunMg)}</td>
<td class="num">${f:h(e.seibunK)}</td>
<td class="num">${f:h(e.seibunFe)}</td>
<td class="num">${f:h(e.seibunMn)}</td>
<td class="num">${f:h(e.seibunEnbun)}</td>
<c:set var="sekkeiUrl" value="../sekkei/${f:h(e.key)}/${e.version}"/>
<td><a href="${f:url(sekkeiUrl)}">施肥設計</a></td>
<c:if test="${loginUser.authSehiEdit}">
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
<c:if test="${loginUser.authSehiEdit}">
<input class="w50" type="button" value="測定値登録" onclick="window.location.href='${f:url('create')}';"/>
</c:if>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('../')}';"/>
</body>
</html>
