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
			<h1>屋号登録</h1>
			<h2>屋号名</h2>
			<input type="text" ${f:text("name")} class="w300 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>
			
			<h2>オーナー</h2>
			<select name="owner">
			<c:forEach var="e" items="${userList}">
				<option ${f:select("owner", f:h(e.key))}>${f:h(e.name)}</option>
			</c:forEach>
			</select>
			
			<h2>初年度</h2>
			<input type="text" ${f:text("shonendo")} class="w50 ${f:errorClass('shonendo', 'err')}"/>
			<br/><span class="${f:errorClass('shonendo', 'err')}">${f:h(errors.shonendo)}</span>
			
			<h2>年度開始月</h2>
			<input type="text" ${f:text("startMonth")} class="w50 ${f:errorClass('startMonth', 'err')}"/>
			<br/><span class="${f:errorClass('startMonth', 'err')}">${f:h(errors.startMonth)}</span>
			
			<hr/>
			<button type="submit">屋号登録</button>
			<button type="button" onclick="window.location.href='/yago';">屋号管理へ戻る</button>
			<button type="button" onclick="window.location.href='/start';">トップへ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
