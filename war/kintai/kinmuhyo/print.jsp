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
	<style type="text/css">
		div.chikoku {
			background-color:#f2dae8;
		}
		div.content table td {
			padding:3px;
		}
	</style>
</head>

<body>
	<div class="content">
		<h1>
		<h2>
			勤務表　-　<fmt:formatDate value="${kinmuhyo.date}" pattern="yyyy年MM月"/>
			　${f:h(kinmuhyo.userRef.model.yagoRef.model.name)} ${f:h(kinmuhyo.userRef.model.name)}
		</h2>
		<table id="kinmu">
			<thead>
				<tr>
					<th>日付</th>
					<th>出勤</th>
					<th>外出</th>
					<th>戻り</th>
					<th>退勤</th>
					<th>残業</th>
					<th>休み</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${dayList}" varStatus="vs">
					<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
						<c:set var="dakoku" value="${map[e]}"/>
						<c:set var="teiji" value="${teijiMap[e]}"/>
						<c:set var="date" value="${dateMap[e]}"/>
						<td><fmt:formatDate value="${date}" pattern="dd日（E）"/></td>
						<td>
							<div<c:if test="${dakoku.start!=null && teiji.startTimeInt<dakoku.startInt}"> class="chikoku"</c:if>>
								<fmt:formatDate value="${dakoku.start}" pattern="HH時mm分"/>
							</div>
						</td>
						<td>
							<c:if test="${dakoku.start!=null}">
								<fmt:formatDate value="${dakoku.out}" pattern="HH時mm分"/>
							</c:if>
						</td>
						<td>
							<div<c:if test="${dakoku.out!=null && dakoku.in==null}"> class="chikoku"</c:if></div>
								<c:if test="${dakoku.out!=null}">
									<fmt:formatDate value="${dakoku.in}" pattern="HH時mm分"/>
								</c:if>
							</div>
						</td>
						<td>
							<div<c:if test="${(dakoku.end!=null && dakoku.endInt<teiji.endTimeInt)
									|| (dakoku.start!=null && dakoku.end==null)}"> class="chikoku"</c:if></div>
								<c:if test="${dakoku.start!=null}">
									<fmt:formatDate value="${dakoku.end}" pattern="HH時mm分"/>
								</c:if>
							</div>
						</td>
						<td>
							<c:if test="${dakoku!=null && dakoku.zangyo}">残業</c:if>
						</td>
						<td>
							<c:if test="${dakoku!=null && dakoku.yasumi}">時間休</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br/>
		<table>
			<thead>
				<tr>
					<th>休み前月残</th><th>休み追加</th><th>休み消費</th><th>休み残</th><th>残業時間</th>
				</tr>
			</thead>
				<tr>
					<td class="num"><input id="yasumiZengetsuZan" name="yasumiZengetsuZan" readonly type="text" ${f:text("yasumiZengetsuZan")} class="w50 num ${f:errorClass('yasumiZengetsuZan', 'err')}"/> h</td>
					<td class="num">
						<input id="yasumiPlus" name="yasumiPlus" readonly type="text" ${f:text("yasumiPlus")} class="w50 num ${f:errorClass('yasumiPlus', 'err')}"/> h
					</td>
					<td class="num"><input id="yasumiHour" name="yasumiHour" readonly type="text" ${f:text("yasumiHour")} class="w50 num ${f:errorClass('yasumiHour', 'err')}"/> h</td>
					<td class="num"><input id="yasumiZan" name="yasumiZan" readonly type="text" ${f:text("yasumiZan")} class="w50 num ${f:errorClass('yasumiZan', 'err')}"/> h</td>
					<td class="num"><input id="zangyoMinutes" name="zangyoMinutes" readonly type="text" ${f:text("zangyoMinutes")} class="w50 num ${f:errorClass('zangyoMinutes', 'err')}"/> 分</td>
				</tr>
		</table>
		休み追加予定表（時間）
		<table>
			<thead>
				<tr>
					<th>4月-10月</th><th>11月</th><th>12月</th><th>1月</th><th>2月</th><th>3月</th>
				</tr>
			</thead>
			<tr>
				<td class="num">各72h</td><td class="num">64h</td><td class="num">56h</td><td class="num">77h</td>
				<td class="num">56h</td><td class="num">64h</td>
			</tr>
		</table>
	</div>
		
</body>
</html>
