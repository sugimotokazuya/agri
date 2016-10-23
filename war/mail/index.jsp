<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<!DOCTYPE html>
<html lang="ja">
<head>
<jsp:include page="/head.jsp"/>

<title>受信メール一覧</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>受信メール一覧</h1>
		
			<form action="${f:url('./')}" method="post">
				<select name="year">
					<c:forEach var="e" items="${yearList}">
						<option ${f:select("year", f:h(e))}>${f:h(e)}</option>
					</c:forEach>
				</select>年
				<select name="month">
					<c:forEach var="e" items="${monthList}">
						<option ${f:select("month", f:h(e))}>${f:h(e)}</option>
					</c:forEach>
				</select>月
				<button type="submit">照会</button>
				<button type="button"  onclick="window.location.href='${f:url('./notice')}';">戻る</button>
			</form>
			<hr/>
			<table>
				<thead>
					<tr><th>日付</th><th>送信元</th><th>題名</th><th>本文</th><th></th><th></th></tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${list}" varStatus="vs">
						<tr onclick="clickRow(${vs.index});">
							<td><fmt:formatDate value="${e.date}" pattern="yyyy-MM-dd HH:mm"/></td>
							<td>${f:h(e.from)}</td>
							<td>${f:h(e.subject)}</td>
							<td>${fn:substring(e.text,0, 15)} ．．．</td>
							<c:set var="url" value="one/${f:h(e.key)}/${e.version}"/>
								<td><a href="${f:url(url)}">表示</a></td>
							<c:if test="${loginUser.authMailEdit}">
								<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
								<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
