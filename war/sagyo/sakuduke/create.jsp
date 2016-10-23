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
			<h1>作付登録</h1>
			<h2>開始日</h2>
			<input type="text" ${f:text("year")} class="w50 num ${f:errorClass('year', 'err')}"/>年
			<input type="text" ${f:text("month")} class="w25 num ${f:errorClass('month', 'err')}"/>月
			<input type="text" ${f:text("day")} class="w25 num ${f:errorClass('day', 'err')}"/>日
			<br/> <span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
			<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
			<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>

			<h2>作付名</h2>
			<input type="text" ${f:text("name")} class="w300 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>

			<h2>畑</h2>
			<select name="hatake">
				<c:forEach var="e" items="${hatakeList}">
					<option ${f:select("hatake", f:h(e.key))}>${f:h(e.name)}(${f:h(e.area)}a)</option>
				</c:forEach>
			</select>
			
			<h2>面積</h2>
			<input type="text" ${f:text("area")} class="w50 num ${f:errorClass('area', 'err')}"/>a
			<br/><span class="${f:errorClass('area', 'err')}">${f:h(errors.area)}</span>
			
			<h2>生産工程責任者</h2>
			<select name="adminUser">
				<c:forEach var="e" items="${userList}">
					<option ${f:select("adminUser", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>
			
			<h2>担当</h2>
			<select name="tanto">
				<c:forEach var="e" items="${userList}">
					<option ${f:select("tanto", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>

			<h2>簡易一括入力</h2>
			<input id="e0" type="radio" ${f:radio("easyInput", "0")}/><label for="e0">利用しない</label>
			<input id="e1" type="radio" ${f:radio("easyInput", "1")}/><label for="e1">利用する</label>
			
			<hr/>
			<button type="submit">作付登録</button>
			<button type="button" onclick="window.location.href='/sagyo/sakuduke';">作付管理へ戻る</button>
			<button type="button" onclick="window.location.href='/sagyo';">作業日誌トップへ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
