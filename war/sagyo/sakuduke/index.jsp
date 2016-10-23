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
<form action="${f:url('./')}" method="post">
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>作付管理</h1>
			照会年：
			<select name="year">
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
				</c:forEach>
			</select>
			<button type="submit">照会</button>
			<button type="button" onclick="window.location.href='${f:url('create')}';">作付を登録</button>
			<button type="button" onclick="window.location.href='${f:url('/sagyo')}';">作業日誌トップへ</button>
			<h2>作付一覧（${f:h(year)}年）</h2>
			<c:if test="${usedAlready}">
				<div><span class="err">既に作業が入力されている作付は削除できません。</span></div>
			</c:if>
	
			<table>
				<thead>
					<tr>
						<th>開始日</th><th>作付名</th><th>担当者</th><th>畑</th><th>面積</th><th>生産工程責任者</th><th>状態</th>
						<th>簡易一括入力</th><th></th>
						<c:if test="${loginUser.authSakudukeEdit}"><th></th><th></th><th></th></c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${list}" varStatus="vs">
						<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
							<td><fmt:formatDate value="${e.startDate}" pattern="yyyy-MM-dd"/></td>
							<td>${f:h(e.name)}</td>
							<td>${f:h(e.tantoRef.model.name)}</td>
							<td>${f:h(e.hatakeRef.model.name)}</td>
							<td class="num">${f:h(e.area)}a</td>
							<td>${f:h(e.adminUserRef.model.name)}</td>
							<td>${f:h(e.statusStr)}</td>
							<td>${f:h(e.easyInputStr)}</td>
							<c:set var="sagyoUrl" value="/sagyo/view/${f:h(e.key)}"/>
							<td><a href="${f:url(sagyoUrl)}">作業一覧</a></td>
							<c:set var="sagyoUrl" value="kiroku/${f:h(e.key)}"/>
							<td><a href="${f:url(sagyoUrl)}">生産工程管理記録</a></td>
							<c:if test="${loginUser.authSakudukeEdit}">
								<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
								<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
								<td><a href="${f:url(editUrl)}">変更</a></td>
								<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
