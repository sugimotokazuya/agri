<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<!DOCTYPE html>
<html lang="ja">
<script type="text/javascript">
function calc() {
	var a = Math.floor($("#sumKinmuJikan").val() * $("#jikyu").val() / 60);
	a += 100 - a % 100;
	$("#kyuyo").val(a);
}
</script>
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

<body onload="setInterval('calc()',500);">
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
					<th>勤務時間</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${dayList}" varStatus="vs">
					<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
						<c:set var="dakoku" value="${map[e]}"/>

						<c:set var="date" value="${dateMap[e]}"/>
						<td><fmt:formatDate value="${date}" pattern="dd日（E）"/></td>
						<td>
							<fmt:formatDate value="${dakoku.start}" pattern="HH時mm分"/>
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
							<div<c:if test="${dakoku.start!=null && dakoku.end==null}"> class="chikoku"</c:if></div>
								<c:if test="${dakoku.start!=null}">
									<fmt:formatDate value="${dakoku.end}" pattern="HH時mm分"/>
								</c:if>
							</div>
						</td>
						<td class="num">${f:h(kinmuJikanMap[e])}<c:if test="${!empty kinmuJikanMap[e]}">分</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br/>
		<table>
			<thead>
				<tr>
					<th>勤務時間</th><th>時給</th><th>給与</th>
				</tr>
			</thead>
				<tr>
					<td class="num">
						<input id="sumKinmuJikan" name="sumKinmuJikan" readonly type="hidden" ${f:hidden("sumKinmuJikan")} class="w50 num"/>
						<c:set var="jikanStr">${sumJikanH}時間${sumJikanM}分</c:set>
						<input name="" type="text" value="${f:h(jikanStr)}" class="w125 num"/>
					</td>
					<td class="num">
						<input id="jikyu"  type="text" class="w50 num"/><span> 円</span>
					</td>
					<td class="num"><input id="kyuyo" readonly type="text" class="w75 num"/> <span>円</span></td>
				</tr>
		</table>
	</div>
		
</body>
</html>
