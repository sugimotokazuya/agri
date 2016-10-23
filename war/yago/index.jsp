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
	<input type="hidden" ${f:hidden("key")}/>
	<input type="hidden" ${f:hidden("version")}/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>屋号管理</h1>
			<button type="button" onclick="window.location.href='${f:url('create')}';">屋号登録</button>
			<button type="button" onclick="window.location.href='${f:url('../')}';">TOPへ戻る</button>
			
			<form action="${f:url('change')}" method="post">
				屋号：
				<select name="yago">
					<c:forEach var="e" items="${yagoList}">
						<option ${f:select("yago", f:h(e.key))}>${f:h(e.name)}</option>
					</c:forEach>
				</select>
				<button type="submit">屋号を切り替える</button>
			</form>
			
			<table>
				<thead>
					<tr>
						<th>名前</th>
						<th>オーナー</th>
						<th>初年度</th>
						<th>年度開始月</th>
						<th>状態</th><th></th><th></th><th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${yagoList}">
						<tr>
							<td>${f:h(e.name)}</td>
							<td>${f:h(e.ownerRef.model.name)}</td>
							<td>${f:h(e.shonendo)}年</td>
							<td>${f:h(e.startMonth)}月</td>
							<td><c:if test="${e.delete==false}">表示</c:if><c:if test="${e.delete==true}">非表示</c:if></td>
							<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
							<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
							<c:set var="userUrl" value="user/${f:h(e.key)}/${e.version}"/>
							<td><a href="${f:url(userUrl)}">ユーザー一覧</a></td>
							<td><a href="${f:url(editUrl)}">変更</a></td>
							<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</body>
</html>
