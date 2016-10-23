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
<form action="${f:url('update')}" method="post">
	<input type="hidden" ${f:hidden("key")}/>
	<input type="hidden" ${f:hidden("version")}/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>お知らせ変更</h1>
			<h2>日付</h2>
			<input type="text" ${f:text("year")} class="w50 num ${f:errorClass('year', 'err')}"/>年
			<input type="text" ${f:text("month")} class="w25 num ${f:errorClass('month', 'err')}"/>月
			<input type="text" ${f:text("day")} class="w25 num ${f:errorClass('day', 'err')}"/>日
			<br/> <span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
			<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
			<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>

			<h2>お知らせタイトル</h2>
			<input type="text" ${f:text("header")} class="w300 ${f:errorClass('header', 'err')}"/>
			<br/><span class="${f:errorClass('header', 'err')}">${f:h(errors.header)}</span>

			<h2>お知らせ</h2>
			<textarea name="body" class="area500x70 ${f:errorClass('body', 'err')}">${f:h(body)}</textarea>
			<br/><span class="${f:errorClass('body', 'err')}">${f:h(errors.body)}</span>
			
			<hr/>
			<button type="submit">お知らせ変更</button>
			<button type="button" onclick="window.location.href='/oshirase';">お知らせ管理へ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
