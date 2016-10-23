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

<title>受信メール表示</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>受信メール表示</h1>
		
			<button type="button"  onclick="window.location.href='${f:url('/mail')}';">戻る</button>
			<button type="button"  onclick="window.location.href='${f:url('/start')}';">TOPへ戻る</button>
	
			<hr/>
			<h2>日付：<fmt:formatDate value="${rm.date}" pattern="yyyy-MM-dd HH:mm"/></h2>
			<h2>送信元：${f:h(rm.from)}</h2>
			<h2>題名：${f:h(rm.subject)}</h2>
			<h2>本文：</h2>
			${f:br(rm.text)}

		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
