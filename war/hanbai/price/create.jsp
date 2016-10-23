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
<title>価格登録</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>価格登録</h1>
		
		<form action="${f:url('insert')}" method="post">
			<h2>取引先</h2>
			<select name="torihikisakiRef" class="w60">
				<c:forEach var="e" items="${tList}">
					<option ${f:select("torihikisakiRef", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>
			<br/>
			<span class="${f:errorClass('torihikisakiRef', 'err')}">${f:h(errors.torihikisakiRef)}</span>
			
			<h2>品目</h2>
			<select name="hinmokuRef" class="w60">
				<c:forEach var="e" items="${hList}">
					<option ${f:select("hinmokuRef", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>
			
			<h2>価格</h2>
			<input type="text" ${f:text("price")} class="w50 num ${f:errorClass('price', 'err')}"/> 円
			<br/>
			<span class="${f:errorClass('price', 'err')}">${f:h(errors.price)}</span>
			<hr/>
			<button type="submit">価格登録</button>
			<button type="button" onclick="window.location.href='${f:url('./')}';">戻る</button>
		</form>
		
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
