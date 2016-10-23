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
			<h1>ユーザー登録</h1>
			<c:if test="${alreadyExistUser}">
				<span class="err">既に本システムを利用しているemailのため登録できません。</span>
			</c:if>
			<h2>ユーザー名</h2>
			<input type="text" ${f:text("name")} class="w200 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>

			<h2>email<span class="small">（ログインに使用するGoogleアカウントを入力してください）</span></h2>
			<input type="text" ${f:text("email")} class="w300 ${f:errorClass('email', 'err')}"/>
			<br/><span class="${f:errorClass('email', 'err')}">${f:h(errors.email)}</span>
			
			<h2>mobile email<span class="small">（モバイルから添付ファイルで画像を送信するのに使用します）</span></h2>
			<input type="text" ${f:text("email2")} class="w300 ${f:errorClass('email2', 'err')}"/>
			<br/><span class="${f:errorClass('email2', 'err')}">${f:h(errors.email2)}</span>
			
			<h2>社員タイプ</h2>
			<input id="employeeType0" type="radio" ${f:radio("employeeType", "0")}/><label for="employeeType0">アルバイト</label>
			<input id="employeeType1" type="radio" ${f:radio("employeeType", "1")}/><label for="employeeType1">正規社員</label>
			<br/><span class="${f:errorClass('employeeType', 'err')}">${f:h(errors.employeeType)}</span>
			
			<div class="memo">
				ユーザーへの権限の設定はユーザー登録後に変更画面から設定してください。
			</div>
			
			<hr/>
			<button type="submit">ユーザー登録</button>
			<button type="button" onclick="window.location.href='/user';">ユーザー管理へ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
