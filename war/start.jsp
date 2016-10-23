<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<!DOCTYPE html>
<html lang="ja">

<head>
<jsp:include page="head.jsp"/>
</head>
<body>
<jsp:include page="header2.jsp"/>
<jsp:include page="menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>トップ</h1>
		<div><button id="authorize-button" style="display: none">Gmailと連携する</button></div>
		<c:if test="${loginUser.authSakudukeView}">
			<h2><a href="${f:url('sagyo')}">作業日誌</a></h2>
			<p>
				作付ごとに作業の記録が出来ます。作業者、作業時間、使用資材、使用機械、写真等を記録できます。
				将来的には有機JASの栽培記録用の帳票出力に対応する予定です。
			</p>
		</c:if>
		<c:if test="${loginUser.authShukkaView}">
			<h2><a href="${f:url('hanbai')}">販売管理</a></h2>
			<p>
				出荷情報の登録、納品書の作成、出荷情報から請求書の作成を行えます。
				サンシャインの直売コーナーへの店舗ごとへの振り分け機能も備えています。
				将来的には納品書、請求書のPDF出力に対応予定です。
			</p>
		</c:if>
		<c:if test="${loginUser.authMailView}">
			<h2><a href="${f:url('mail/notice')}">売上速報</a></h2>
			<p>
				直売所の売上メールを受信、表示することが出来ます。現時点ではサンシャインの直売コーナーの売上メールに対応しています。
				将来的には商品毎、店舗毎の集計機能を実装する予定です。
			</p>
		</c:if>
		<c:if test="${loginUser.authSehiView}">
			<h2><a href="${f:url('sehi')}">施肥設計</a></h2>
			<p>
				畑ごとに土壌分析値の登録と施肥設計を行えます。
			</p>
		</c:if>
		<c:if test="${loginUser.emailStr=='sugimotokazuya@gmail.com'}">
			<c:if test="${loginUser.authShinkoku}">
				<h2><a href="${f:url('shinkoku')}">会計</a></h2>
				<p>
					複式簿記での登録が行えます。
				</p>
			</c:if>
		</c:if>
		<c:if test="${loginUser.authUserView}">
			<h2><a href="${f:url('user')}">ユーザー管理</a></h2>
			<p>
				複数のユーザーでの使用に対応しています。各ユーザー毎に使用できる機能を制限することが出来ます。
			</p>
		</c:if>
		<c:if test="${loginUser.authHatakeView}">
			<h2><a href="${f:url('hatake')}">畑管理</a></h2>
			<p>
				畑の情報を登録することが出来ます。畑の情報は作業日誌や施肥設計で使用します。
			</p>
		</c:if>
		<c:if test="${loginUser.authTorihikisakiView}">
			<h2><a href="${f:url('torihikisaki')}">取引先管理</a></h2>
			<p>
				取引先や直売店を登録することが出来ます。取引先・直売店は販売管理、売上速報で使用します。
			</p>
		</c:if>
		<c:if test="${loginUser.authHinmokuView}">
			<h2><a href="${f:url('hinmoku')}">品目管理</a></h2>
			<p>
				品目の情報の登録をすることが出来ます。品目の情報は販売管理、売上速報で使用します。
			</p>
		</c:if>
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="side.jsp"/>
</div>

<jsp:include page="footer.jsp"/>

</body>
</html>
