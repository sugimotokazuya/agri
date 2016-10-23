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
<title>出荷形態統合</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>出荷形態統合</h1>
		
		<form action="${f:url('togoUpdate')}" method="post">
			<input type="hidden" ${f:hidden("key")}/>
			<input type="hidden" ${f:hidden("version")}/>
			<h2>取引先：${f:h(torihikisakiRef.model.name)}</h2>
			<h2>品目：${f:h(hinmokuRef.model.name)}</h2>
			<h2>出荷形態：${f:h(hoso)}</h2>
			<h2>重量：<fmt:formatNumber value="${juryo}" pattern="###,###,##0"/>g</h2>
			<h2>価格：<fmt:formatNumber value="${price}" pattern="###,###,##0"/>円</h2>
			
			<h2>統合する出荷形態</h2>
			<select name="togoKey">
				<c:set var="togoFlg" value="${false}"/>
				<c:forEach var="sk" items="${list}">
					<c:if test="${key!=sk.key}">
						<c:set var="togoFlg" value="${true}"/>
						<option style="" ${f:select(togoKey, f:h(sk.key))}>${f:h(sk.hoso)}</option>
					</c:if>
				</c:forEach>
			</select>
			<br/>
			※ 選択した出荷形態が統合され削除されます。
			<hr/>
			<c:if test="${togoFlg}"><button type="submit">出荷形態統合</button></c:if>
			<button type="button" onclick="window.location.href='${f:url('./')}';">
				戻る<c:if test="${!togoFlg}">（統合対象がありません）</c:if>
			</button>
		</form>

		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
