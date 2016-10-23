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
<form action="${f:url('pdf')}" method="post">
	<input type="hidden" ${f:hidden("key")}/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>生産工程管理記録（${f:h(sakuduke.name)}：<fmt:formatDate value="${sakuduke.startDate}" pattern="yyyy-MM-dd"/>開始）</h1>
			右で指定した日付以降の作業記録を表示：
			<input type="text" ${f:text("year")} class="w50 num ${f:errorClass('year', 'err')}"/>年
			<input type="text" ${f:text("month")} class="w25 num ${f:errorClass('month', 'err')}"/>月
			<input type="text" ${f:text("day")} class="w25 num ${f:errorClass('day', 'err')}"/>日
			<br/> <span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
			<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
			<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>
			<button type="submit">PDF作成</button>
			<button type="button" onclick="window.location.href='${f:url('/sagyo/sakuduke')}';">戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/sagyo')}';">作業日誌トップへ</button>
			
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
