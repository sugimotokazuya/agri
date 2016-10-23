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
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>基本設定</h1>
			<h2>屋号</h2>
			<input type="text" ${f:text("name")} class="w300 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>
			
			<h2>クラウド勤怠に使用するアカウント</h2>
			<select name="kintai">
				<option value="">なし</option>
				<c:forEach var="e" items="${userList}">
					<option ${f:select('kintai', f:h(e.key))}>${f:h(e.name)}:${f:h(e.emailStr)}</option>
				</c:forEach>
			</select>

			<h2>請求書・納品書で使用する情報</h2>
			<table>
				<tr>
					<th>お問い合わせ先</th>
					<td>
						<textarea name="toiawase" class="area500x70 ${f:errorClass('toiawase', 'err')}">${f:h(toiawase)}</textarea>
						<br/><span class="${f:errorClass('toiawase', 'err')}">${f:h(errors.toiawase)}</span>
					</td>
				</tr>
				<tr>
					<th>振込先１</th>
					<td>
						<textarea name="furikomisaki1" class="area500x70 ${f:errorClass('furikomisaki1', 'err')}">${f:h(furikomisaki1)}</textarea>
						<br/><span class="${f:errorClass('furikomisaki1', 'err')}">${f:h(errors.furikomisaki1)}</span>
					</td>
				</tr>
				<tr>
					<th>振込先２</th>
					<td>
						<textarea name="furikomisaki2" class="area500x70 ${f:errorClass('furikomisaki2', 'err')}">${f:h(furikomisaki2)}</textarea>
						<br/><span class="${f:errorClass('furikomisaki2', 'err')}">${f:h(errors.furikomisaki2)}</span>
					</td>
				</tr>
				<tr>
					<th>振込先３</th>
					<td>
						<textarea name="furikomisaki3" class="area500x70 ${f:errorClass('furikomisaki3', 'err')}">${f:h(furikomisaki3)}</textarea>
						<br/><span class="${f:errorClass('furikomisaki3', 'err')}">${f:h(errors.furikomisaki3)}</span>
					</td>
				</tr>
			</table>
						
			<hr/>
			<button type="submit">基本設定変更</button>
			<button type="button" onclick="window.location.href='/';">TOPへ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
