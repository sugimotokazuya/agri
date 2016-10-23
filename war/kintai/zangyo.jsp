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
<form action="${f:url('zangyoShinsei')}" method="post">
	<input type="hidden" ${f:hidden("userKey")}/>
	<input type="hidden" ${f:hidden("year")}/>
	<input type="hidden" ${f:hidden("month")}/>
	<input type="hidden" ${f:hidden("day")}/>
	<input type="hidden" ${f:hidden("key")}/>
	<input type="hidden" ${f:hidden("version")}/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>残業申請　-　${f:h(year)}年${f:h(month+1)}月${f:h(day)}日　${f:h(user.name)}</h1>
			
			<h2>申請理由</h2>
			<input type="text" ${f:text("riyu")} class="w300 ${f:errorClass('riyu', 'err')}"/>
			<br/>例：袋詰が終わらないため,　誘引が間に合わないため
			<br/><span class="${f:errorClass('riyu', 'err')}">${f:h(errors.riyu)}</span>
			
			<hr/>
			<button type="submit">残業を申請する</button>
			<c:if test="${!empty(key)}">
				<c:set var="url" value="/kintai/shinseiDelete/${f:h(key)}/${version}"/>
				<button type="button" onclick="if(confirm('取り消して良いですか?')) window.location.href='${f:url(url)}';">申請を取り消す</button>
			</c:if>
			<button type="button" onclick="window.location.href='${f:url('/kintai')}';">戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/')}';">トップへ戻る</button>

			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
