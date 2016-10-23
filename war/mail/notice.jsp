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

<title>売上速報リスト</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>売上速報</h1>

		<form action="${f:url('notice')}" method="post">
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
			<button type="button" onclick="window.location.href='${f:url('./')}';">受信メール確認</button>
			<button type="button" onclick="window.location.href='${f:url('/start')}';">TOPへ戻る</button>
		</form>
		<hr/>
		<table>
			<thead>
				<tr>
					<th>売上速報日時</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${onList}">
					<tr>
						<td>
							<c:set var="url">sokuho/${f:h(e.key)}/${e.version}</c:set>
							<a href="${f:url(url)}">
								<fmt:formatDate value="${e.date}" pattern="yyyy年MM月dd日　HH時mm分"/>
							</a>
						</td>
						<c:if test="${loginUser.authMailEdit}">
							<c:set var="deleteUrl" value="onDelete/${f:h(e.key)}/${e.version}"/>
							<td style="text-align:center;"><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
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
