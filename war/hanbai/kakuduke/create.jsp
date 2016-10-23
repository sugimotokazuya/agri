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
<title>格付登録</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>格付登録</h1>
		
		<form action="${f:url('insert')}" method="post">
			<h2>格付担当者</h2>
			<select name="userRef" class="w60">
				<c:forEach var="e" items="${list}">
					<option ${f:select("userRef", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>
			<br/>
			<span class="${f:errorClass('userRef', 'err')}">${f:h(errors.userRef)}</span>
			
			<h2>日付</h2>
			<input type="text" ${f:text("year")} class="w50 num ${f:errorClass('year', 'err')}"/><span>年</span>
			<input type="text" ${f:text("month")} class="w25 num ${f:errorClass('month', 'err')}"/><span>月</span>
			<input type="text" ${f:text("day")} class="w25 num ${f:errorClass('day', 'err')}"/><span>日</span>
			<div>
				<span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
				<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
				<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>
			</div>
			
			<h2>枚数</h2>
			<input id="s0" type="radio" ${f:radio("kind", "0")}/><label for="s0">受け入れ</label>
			<input id="s1" type="radio" ${f:radio("kind", "1")}/><label for="s1">使用</label>
			<span class="${f:errorClass('kind', 'err')}">${f:h(errors.kind)}</span>
			&nbsp;&nbsp;<input type="text" ${f:text("plus")} class="w50 num ${f:errorClass('plus', 'err')}"/>枚
			<br/>
			<span class="${f:errorClass('plus', 'err')}">${f:h(errors.plus)}</span>
			
			<h2>備考</h2>
			<input type="text" ${f:text("biko")} class="w100 ${f:errorClass('biko', 'err')}"/>（例：購入、破損）
			<br/>
			<span class="${f:errorClass('biko', 'err')}">${f:h(errors.biko)}</span>
			
			<hr/>
			<button type="submit">格付登録</button>
			<button type="button" onclick="window.location.href='${f:url('./')}';">戻る</button>
		</form>
		
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
