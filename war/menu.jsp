<%@page pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<div class="menu" id="menu">
	<ul>
		
		<li><a href="/start">トップ</a></li>
		
		<c:if test="${loginUser.authSakudukeView}">
			<li><a href="${f:url('/sagyo')}">作業日誌</a></li>
		</c:if>
		<c:if test="${loginUser.authShukkaView}">
			<li><a href="${f:url('/hanbai')}">販売管理</a></li>
		</c:if>
		<c:if test="${loginUser.authMailView}">
			<li><a href="${f:url('/mail/notice')}">売上速報</a></li>
		</c:if>
		<c:if test="${loginUser.authSehiView}">
			<li><a href="${f:url('/sehi')}">施肥設計</a></li>
		</c:if>
		<c:if test="${loginUser.useDakoku || loginUser.authKintai}">
			<li><a href="${f:url('/kintai')}">勤怠</a></li>
		</c:if>
		<c:if test="${loginUser.emailStr=='sugimotokazuya@gmail.com' || loginUser.emailStr=='kazuya@haruhibatake.jp'}">
			<c:if test="${loginUser.authShinkoku}">
				<li><a href="${f:url('/shinkoku')}">会計</a></li>
			</c:if>
		</c:if>
		<c:if test="${loginUser.authUserView}">
			<li><a href="${f:url('/user')}">ユーザー管理</a></li>
		</c:if>
		<c:if test="${loginUser.authHatakeView}">
			<li><a href="${f:url('/hatake')}">畑管理</a></li>
		</c:if>
		<c:if test="${loginUser.authTorihikisakiView}">
			<li><a href="${f:url('/torihikisaki')}">取引先管理</a></li>
		</c:if>
		<c:if test="${loginUser.authHinmokuView}">
			<li><a href="${f:url('/hinmoku')}">品目管理</a></li>
		</c:if>
		<c:if test="${isOwner}">
			<li><a href="${f:url('/settei/edit')}">基本設定</a></li>
		</c:if>
		<c:if test="${loginUser.emailStr=='sugimotokazuya@gmail.com'}">
			<li><a href="${f:url('/yago')}">屋号管理</a></li>
			<li><a href="${f:url('/oshirase')}">お知らせ管理</a></li>
			<li><a href="${f:url('/init')}">データ移行</a></li>
		</c:if>
		<li><a href="https://mail.google.com/mail/u/0/#inbox" target="_blank"><i class="fa fa-envelope-o"></i> <span id="midoku"></span></a></li>
		
	</ul>
</div>