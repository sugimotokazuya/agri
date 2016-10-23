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
<title>肥料管理</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">肥料管理</div>
<table>
<thead>
<tr><th>名前</th><th>N</th><th>N係数</th><th>P</th><th>K</th><th>Ca</th><th>Mg</th><th>B</th><th>Mn</th><th>Fe</th><th>kg/袋</th><th>表示</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${hiryoList}">
<tr>
<td>${f:h(e.name)}</td>
<td class="num">${f:h(e.seibunN)}</td>
<td class="num">${f:h(e.nkeisu)}</td>
<td class="num">${f:h(e.seibunP)}</td>
<td class="num">${f:h(e.seibunK)}</td>
<td class="num">${f:h(e.seibunCa)}</td>
<td class="num">${f:h(e.seibunMg)}</td>
<td class="num">${f:h(e.seibunB)}</td>
<td class="num">${f:h(e.seibunMn)}</td>
<td class="num">${f:h(e.seibunFe)}</td>
<td class="num">${f:h(e.weight)}</td>
<td><c:if test="${e.delete==false}">表示</c:if><c:if test="${e.delete==true}">非表示</c:if></td>
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
※ 使用中の肥料の削除はできません。代わりに非表示になります。
<hr/>
<c:if test="${loginUser.authSehiEdit}">
<input class="w50" type="button" value="肥料登録" onclick="window.location.href='${f:url('create')}';"/>
</c:if>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('../')}';"/>
</body>
</html>
