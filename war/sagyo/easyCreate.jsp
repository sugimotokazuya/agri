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
<script type="text/javascript">

</script>
</head>
<body>

<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>
			簡易一括作業登録[<span class="strong">${f:h(sagyoItem.name)}</span>]
		</h1>
		<form action="${f:url('easyInsert')}" method="post">
			<input type="hidden" ${f:hidden("sagyoItemKey")}/>

			<h2>日付</h2>
			<input type="text" ${f:text("year")} class="w50 num ${f:errorClass('year', 'err')}"/>年
			<input type="text" ${f:text("month")} class="w25 num ${f:errorClass('month', 'err')}"/>月
			<input type="text" ${f:text("day")} class="w25 num ${f:errorClass('day', 'err')}"/>日
			<div>
				<span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
				<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
				<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>
			</div>
			
			<table>
				<thead>
					<tr>
						<th>畑名</th><th>作付名</th><th>量</th>
						<c:forEach var="e" items="${userList}">
							<th>${e.name}</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${list}" varStatus="s">
						<input type="hidden" name="key${s.index}" value="${f:h(e.key)}" />
						<tr>
							<td>${f:h(e.hatakeRef.model.name)}</td>
							<td>${f:h(e.name)}</td>
							<td>
								<c:set var="amount">amount${s.index}</c:set>
								<input type="text" ${f:text(amount)} class="w50 num ${f:errorClass(amount, 'err')}"/>
								${f:h(sagyoItem.tanni)}
							</td>
							<c:forEach var="e2" items="${userList}" varStatus="s2">
								<td>
									<input type="hidden" name="user${s.index}_${s2.index}" value="${f:h(e2.key)}"/>
									<c:set var="minutes">minutes${s.index}_${s2.index}</c:set>
									<input type="text" ${f:text(minutes)} class="w50 num ${f:errorClass(minutes, 'err')}"/>
									分
								</td>
						</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<hr/>
			<button type="submit">登録</button>
			<button type="button" onclick="window.location.href='/sagyo';">作業日誌トップへ</button>
		</form>
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
