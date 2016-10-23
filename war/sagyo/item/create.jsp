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
			<h1>作業項目登録</h1>
			<h2>作業名</h2>
			<input type="text" ${f:text("name")} class="w300 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>

			<h2>単位</h2>
			<input type="text" ${f:text("tanni")} class="w50 ${f:errorClass('tanni', 'err')}"/>
			<br/><span class="${f:errorClass('tanni', 'err')}">${f:h(errors.tanni)}</span>
			
			<hr/>
			<button type="submit">作業項目登録</button>
			<button type="button" onclick="window.location.href='/sagyo/item';">作業項目管理へ戻る</button>
			<button type="button" onclick="window.location.href='/sagyo';">作業日誌トップへ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
