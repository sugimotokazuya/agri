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
</head>

<body>
<form action="${f:url('insert')}" method="post">
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>勤務時間の定時登録</h1>
			<h2>適用開始日</h2>
			<input type="text" ${f:text("startYear")} class="w50 num ${f:errorClass('startYear', 'err')}"/>年
			<input type="text" ${f:text("startMonth")} class="w25 num ${f:errorClass('startMonth', 'err')}"/>月
			<input type="text" ${f:text("startDay")} class="w25 num ${f:errorClass('startDay', 'err')}"/>日
			<div>
				<span class="${f:errorClass('startYear', 'err')}">${f:h(errors.startYear)}</span>
				<span class="${f:errorClass('startMonth', 'err')}">${f:h(errors.startMonth)}</span>
				<span class="${f:errorClass('startDay', 'err')}">${f:h(errors.startDay)}</span>
			</div>

			<h2>適用終了日</h2>
			<input type="text" ${f:text("endYear")} class="w50 num ${f:errorClass('endYear', 'err')}"/>年
			<input type="text" ${f:text("endMonth")} class="w25 num ${f:errorClass('endMonth', 'err')}"/>月
			<input type="text" ${f:text("endDay")} class="w25 num ${f:errorClass('endDay', 'err')}"/>日
			<div>
				<span class="${f:errorClass('endYear', 'err')}">${f:h(errors.endYear)}</span>
				<span class="${f:errorClass('endMonth', 'err')}">${f:h(errors.endMonth)}</span>
				<span class="${f:errorClass('endDay', 'err')}">${f:h(errors.endDay)}</span>
			</div>
			
			<h2>開始時間</h2>
			<select name="startTime1">
				<option ${f:select(startTime1, 6)}>06</option>
				<option ${f:select(startTime1, 7)}>07</option>
				<option ${f:select(startTime1, 8)}>08</option>
				<option ${f:select(startTime1, 9)}>09</option>
				<option ${f:select(startTime1, 10)}>10</option>
			</select>時
			<select name="startTime2">
				<option ${f:select(startTime2, 0)}>00</option>
				<option ${f:select(startTime2, 30)}>30</option>
			</select>分
			
			<h2>終了時間</h2>
			<select name="endTime1">
				<option ${f:select(endTime1, 14)}>14</option>
				<option ${f:select(endTime1, 15)}>15</option>
				<option ${f:select(endTime1, 16)}>16</option>
				<option ${f:select(endTime1, 17)}>17</option>
				<option ${f:select(endTime1, 18)}>18</option>
			</select>時
			<select name="endTime2">
				<option ${f:select(endTime2, 0)}>00</option>
				<option ${f:select(endTime2, 30)}>30</option>
			</select>分
			
			<hr/>
			<button type="submit">定時登録</button>
			<button type="button" onclick="window.location.href='/kintai/teiji';">定時管理に戻る</button>
			<button type="button" onclick="window.location.href='/kintai';">勤怠トップへ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
