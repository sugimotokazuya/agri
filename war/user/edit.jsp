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
			<h1>ユーザー変更</h1>
			<c:if test="${alreadyExistUser}">
				<span class="err">既に本システムを利用しているemailのため登録できません。</span>
			</c:if>
			<h2>ユーザー名</h2>
			<input type="text" ${f:text("name")} class="w200 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>

			<h2>email<span class="smallStr">（ログインに使用するGoogleアカウントを入力してください）</span></h2>
			<input type="text" ${f:text("emailStr")} class="w300 ${f:errorClass('emailStr', 'err')}"/>
			<br/><span class="${f:errorClass('emailStr', 'err')}">${f:h(errors.emailStr)}</span>
			
			<h2>mobile email<span class="small">（モバイルから添付ファイルで画像を送信するのに使用します）</span></h2>
			<input type="text" ${f:text("email2Str")} class="w300 ${f:errorClass('email2Str', 'err')}"/>
			<br/><span class="${f:errorClass('email2Str', 'err')}">${f:h(errors.email2Str)}</span>
			
			<h2>クラウド勤怠</h2>
			<input id="useDakoku0" type="radio" ${f:radio("useDakoku", "false")}/><label for="useDakoku0">利用しない</label>
			<input id="useDakoku1" type="radio" ${f:radio("useDakoku", "true")}/><label for="useDakoku1">利用する</label>
			
			<h2>社員タイプ</h2>
			<input id="employeeType0" type="radio" ${f:radio("employeeType", "0")}/><label for="employeeType0">アルバイト</label>
			<input id="employeeType1" type="radio" ${f:radio("employeeType", "1")}/><label for="employeeType1">正規社員</label>
			
			
			<h2>格付担当者</h2>
			<input id="kakudukeTanto0" type="radio" ${f:radio("kakudukeTanto", "false")}/><label for="kakudukeTanto0">格付担当者ではない</label>
			<input id="kakudukeTanto1" type="radio" ${f:radio("kakudukeTanto", "true")}/><label for="kakudukeTanto1">格付担当者である</label>
			
			<h2>表示</h2>
			<input id="status0" type="radio" ${f:radio("delete", "false")}/><label for="status0">表示</label>
			<input id="status1" type="radio" ${f:radio("delete", "true")}/><label for="status1">非表示</label>
				
			<h2>権限</h2>
			<table>
			<thead>
				<tr><th>機能</th><th>項目</th><th>設定</th></tr>
			</thead>
			
			<tr><td rowspan="2">ユーザー管理</td><td>閲覧</td>
			<td>
			<input id="a0" type="radio" ${f:radio("authUserView", "true")}/><label for="a0">権限あり</label>
			<input id="a1" type="radio" ${f:radio("authUserView", "false")}/><label for="a1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>追加・変更</td>
			<td>
			<input id="b0" type="radio" ${f:radio("authUserEdit", "true")}/><label for="b0">権限あり</label>
			<input id="b1" type="radio" ${f:radio("authUserEdit", "false")}/><label for="b1">権限なし</label>
			</td>
			</tr>
			
			<tr><td rowspan="2">作業項目</td><td>閲覧</td>
			<td>
			<input id="c0" type="radio" ${f:radio("authSagyoItemView", "true")}/><label for="c0">権限あり</label>
			<input id="c1" type="radio" ${f:radio("authSagyoItemView", "false")}/><label for="c1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>追加・変更</td>
			<td>
			<input id="d0" type="radio" ${f:radio("authSagyoItemEdit", "true")}/><label for="d0">権限あり</label>
			<input id="d1" type="radio" ${f:radio("authSagyoItemEdit", "false")}/><label for="d1">権限なし</label>
			</td>
			</tr>
			
			<tr><td rowspan="3">作付・作業</td><td>閲覧</td>
			<td>
			<input id="e0" type="radio" ${f:radio("authSakudukeView", "true")}/><label for="e0">権限あり</label>
			<input id="e1" type="radio" ${f:radio("authSakudukeView", "false")}/><label for="e1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>作付追加・変更</td>
			<td>
			<input id="f0" type="radio" ${f:radio("authSakudukeEdit", "true")}/><label for="f0">権限あり</label>
			<input id="f1" type="radio" ${f:radio("authSakudukeEdit", "false")}/><label for="f1">権限なし</label>
			</td>
			</tr>
			<td>作業入力・変更</td>
			<td>
			<input id="g0" type="radio" ${f:radio("authSagyoEdit", "true")}/><label for="g0">権限あり</label>
			<input id="g1" type="radio" ${f:radio("authSagyoEdit", "false")}/><label for="g1">権限なし</label>
			</td>
			</tr>
			
			<tr><td rowspan="2">施肥設計</td><td>閲覧</td>
			<td>
			<input id="h0" type="radio" ${f:radio("authSehiView", "true")}/><label for="h0">権限あり</label>
			<input id="h1" type="radio" ${f:radio("authSehiView", "false")}/><label for="h1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>追加・変更</td>
			<td>
			<input id="i0" type="radio" ${f:radio("authSehiEdit", "true")}/><label for="i0">権限あり</label>
			<input id="i1" type="radio" ${f:radio("authSehiEdit", "false")}/><label for="i1">権限なし</label>
			</td>
			</tr>
			
			<tr><td rowspan="2">畑管理</td><td>閲覧</td>
			<td>
			<input id="j0" type="radio" ${f:radio("authHatakeView", "true")}/><label for="j0">権限あり</label>
			<input id="j1" type="radio" ${f:radio("authHatakeView", "false")}/><label for="j1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>追加・変更</td>
			<td>
			<input id="k0" type="radio" ${f:radio("authHatakeEdit", "true")}/><label for="k0">権限あり</label>
			<input id="k1" type="radio" ${f:radio("authHatakeEdit", "false")}/><label for="k1">権限なし</label>
			</td>
			</tr>
			
			<tr><td rowspan="2">売上速報</td><td>閲覧</td>
			<td>
			<input id="l0" type="radio" ${f:radio("authMailView", "true")}/><label for="l0">権限あり</label>
			<input id="l1" type="radio" ${f:radio("authMailView", "false")}/><label for="l1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>追加・変更</td>
			<td>
			<input id="m0" type="radio" ${f:radio("authMailEdit", "true")}/><label for="m0">権限あり</label>
			<input id="m1" type="radio" ${f:radio("authMailEdit", "false")}/><label for="m1">権限なし</label>
			</td>
			</tr>
			
			<tr><td rowspan="2">取引先管理</td><td>閲覧</td>
			<td>
			<input id="n0" type="radio" ${f:radio("authTorihikisakiView", "true")}/><label for="n0">権限あり</label>
			<input id="n1" type="radio" ${f:radio("authTorihikisakiView", "false")}/><label for="n1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>追加・変更</td>
			<td>
			<input id="o0" type="radio" ${f:radio("authTorihikisakiEdit", "true")}/><label for="o0">権限あり</label>
			<input id="o1" type="radio" ${f:radio("authTorihikisakiEdit", "false")}/><label for="o1">権限なし</label>
			</td>
			</tr>
			
			<tr><td rowspan="2">出荷管理</td><td>閲覧</td>
			<td>
			<input id="p0" type="radio" ${f:radio("authShukkaView", "true")}/><label for="p0">権限あり</label>
			<input id="p1" type="radio" ${f:radio("authShukkaView", "false")}/><label for="p1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>追加・変更</td>
			<td>
			<input id="q0" type="radio" ${f:radio("authShukkaEdit", "true")}/><label for="q0">権限あり</label>
			<input id="q1" type="radio" ${f:radio("authShukkaEdit", "false")}/><label for="q1">権限なし</label>
			</td>
			</tr>
			
			<tr><td rowspan="2">請求管理</td><td>閲覧</td>
			<td>
			<input id="r0" type="radio" ${f:radio("authSeikyuView", "true")}/><label for="r0">権限あり</label>
			<input id="r1" type="radio" ${f:radio("authSeikyuView", "false")}/><label for="r1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>追加・変更</td>
			<td>
			<input id="s0" type="radio" ${f:radio("authSeikyuEdit", "true")}/><label for="s0">権限あり</label>
			<input id="s1" type="radio" ${f:radio("authSeikyuEdit", "false")}/><label for="s1">権限なし</label>
			</td>
			</tr>
			
			<tr><td rowspan="2">品目管理</td><td>閲覧</td>
			<td>
			<input id="t0" type="radio" ${f:radio("authHinmokuView", "true")}/><label for="t0">権限あり</label>
			<input id="t1" type="radio" ${f:radio("authHinmokuView", "false")}/><label for="t1">権限なし</label>
			</td>
			</tr>
			<tr>
			<td>追加・変更</td>
			<td>
			<input id="u0" type="radio" ${f:radio("authHinmokuEdit", "true")}/><label for="u0">権限あり</label>
			<input id="u1" type="radio" ${f:radio("authHinmokuEdit", "false")}/><label for="u1">権限なし</label>
			</td>
			</tr>
			
			<tr><td>会計機能</td><td>閲覧・追加・変更</td>
			<td>
			<input id="v0" type="radio" ${f:radio("authShinkoku", "true")}/><label for="v0">権限あり</label>
			<input id="v1" type="radio" ${f:radio("authShinkoku", "false")}/><label for="v1">権限なし</label>
			</td>
			</tr>
			
			<tr><td>勤怠管理機能</td><td>閲覧・追加・変更</td>
			<td>
			<input id="w0" type="radio" ${f:radio("authKintai", "true")}/><label for="w0">権限あり</label>
			<input id="w1" type="radio" ${f:radio("authKintai", "false")}/><label for="w1">権限なし</label>
			</td>
			</tr>
			
			</table>
				
				
			<hr/>
			<button type="submit">ユーザー変更</button>
			<button type="button" onclick="window.location.href='/user';">ユーザー管理へ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
