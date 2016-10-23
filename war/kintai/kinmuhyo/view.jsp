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
	</style>
	<script type="text/javascript">
		
		$(function (){
			<c:if test="${kinmuhyo.shonin==1}">
				setInterval("calc()",500);
			</c:if>
		});
		function calc() {
			$("#yasumiZan").val(Number($("#yasumiZengetsuZan").val()) + Number($("#yasumiPlus").val()) - Number($("#yasumiHour").val()));
		}
	</script>
</head>

<body>
<form action="${f:url('kyakka')}" method="post">
	<input type="hidden" ${f:hidden("key")}/>
	<input type="hidden" ${f:hidden("version")}/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>勤怠</h1>
			
			<h2>
				勤務表承認　-　<fmt:formatDate value="${kinmuhyo.date}" pattern="yyyy年MM月"/>
				　${f:h(kinmuhyo.userRef.model.name)}
				（${f:h(kinmuhyo.shoninStr)}）
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
						<th>お昼休み</th>
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
							<td>
								<c:if test="${dakoku!=null && dakoku.ohiru}">お昼休み有</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br/>
			<table>
				<thead>
					<tr>
						<th>勤務日数</th><th>勤務時間</th><th>残業時間</th><th>休み前月残</th><th>休み追加</th><th>休み消費</th><th>休み残</th>
					</tr>
				</thead>
					<tr>
						<td class="num"><input id="kinmuDays" name="kinmuDays" readonly type="text" ${f:text("kinmuDays")} class="w50 num ${f:errorClass('kinmuDays', 'err')}"/> 日</td>
						<td class="num"><input id="kinmuHours" name="kinmuHours" readonly type="text" ${f:text("kinmuHours")} class="w50 num ${f:errorClass('kinmuHours', 'err')}"/> 時間</td>
						<td class="num"><input id="zangyoMinutes" name="zangyoMinutes" readonly type="text" ${f:text("zangyoMinutes")} class="w50 num ${f:errorClass('zangyoMinutes', 'err')}"/> 分</td>
						<td class="num"><input id="yasumiZengetsuZan" name="yasumiZengetsuZan" readonly type="text" ${f:text("yasumiZengetsuZan")} class="w50 num ${f:errorClass('yasumiZengetsuZan', 'err')}"/> 時間</td>
						<td class="num">
							<input id="yasumiPlus" <c:if test="${kinmuhyo.shonin!=1}">readonly</c:if> name="yasumiPlus" type="text" ${f:text("yasumiPlus")} class="w50 num ${f:errorClass('yasumiPlus', 'err')}"/> 時間
						</td>
						<td class="num"><input id="yasumiHour" name="yasumiHour" readonly type="text" ${f:text("yasumiHour")} class="w50 num ${f:errorClass('yasumiHour', 'err')}"/> 時間</td>
						<td class="num"><input id="yasumiZan" name="yasumiZan" readonly type="text" ${f:text("yasumiZan")} class="w50 num ${f:errorClass('yasumiZan', 'err')}"/> 時間</td>
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
			<hr/>
			<c:if test="${loginUser.authKintai}">
				<c:choose>
					<c:when test="${kinmuhyo.shonin==1}">
						<button type="submit" onclick="this.form.action='${f:url('shonin')}';">承認する</button>
						
						<br/>
						<button type="submit">却下する</button>
						　却下理由：<input type="text" ${f:text("riyu")} class="w300 ${f:errorClass('riyu', 'err')}"/>
						<span class="${f:errorClass('riyu', 'err')}">${f:h(errors.riyu)}</span><br/>
					</c:when>
					<c:when test="${kinmuhyo.shonin==2}">
						<c:set var="url" value="torikeshi/${f:h(kinmuhyo.key)}/${kinmuhyo.version}"/>
						<button type="button" onclick="if(confirm('承認を取り消して良いですか?')) window.location.href='${f:url(url)}';">承認を取り消す</button>
					</c:when>
				</c:choose>
				
				<button type="submit" onclick="this.form.action='${f:url('save')}';">保存する</button><br/>
			</c:if>
			<button type="button" onclick="window.location.href='${f:url('/kintai/kinmuhyo')}';">戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/kintai')}';">勤怠のTOPへ戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/')}';">トップへ戻る</button>
			
			
			
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
