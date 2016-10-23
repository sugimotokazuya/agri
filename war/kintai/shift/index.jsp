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
	<style>
		table#shiftTable {
			width:100%;
		}
		
		table#shiftTable td a {
			display:inline;
		}
		table#shiftTable span.link {
			font-size:x-small;
		}
		table#shiftTable div.all {
			width:100%;
			background-color:#afd0ef;
			font-size:x-small;
		}
		table#shiftTable div.am {
			width:50%;
			background-color:#eeea55;
			font-size:x-small;
		}
		table#shiftTable div.pm {
			width:50%;
			margin-left:50%;
			background-color:#f2dae8;
			font-size:x-small;
		}
	</style>
</head>

<body>
<form action="${f:url('./')}" method="post">
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>シフト管理</h1>
			<button type="button" onclick="window.location.href='${f:url('/kintai')}';">戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/')}';">トップへ戻る</button>
			<br/>
			<c:if test="${loginUser.authKintai}">
				ユーザー：
				<select name="userRef">
					<c:forEach var="e" items="${userList}">
						<option ${f:select("userRef", f:h(e.key))}>${f:h(e.name)}</option>
					</c:forEach>
				</select>
			</c:if>	
			照会年：
			<select name="year">
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
				</c:forEach>
			</select>
			照会月：
			<select name="month">
				<option ${f:select("month", "0")}>1月</option>
				<option ${f:select("month", "1")}>2月</option>
				<option ${f:select("month", "2")}>3月</option>
				<option ${f:select("month", "3")}>4月</option>
				<option ${f:select("month", "4")}>5月</option>
				<option ${f:select("month", "5")}>6月</option>
				<option ${f:select("month", "6")}>7月</option>
				<option ${f:select("month", "7")}>8月</option>
				<option ${f:select("month", "8")}>9月</option>
				<option ${f:select("month", "9")}>10月</option>
				<option ${f:select("month", "10")}>11月</option>
				<option ${f:select("month", "11")}>12月</option>
			</select>
			<button type="submit">照会</button>
			<c:if test="${loginUser.authKintai}">
				<button type="submit" onclick="this.form.action='${f:url('./addUser')}';">ユーザーをシフトに追加</button>
			</c:if>
			
			<h2>シフト表　-　${f:h(year)}年${f:h(month+1)}月　${f:h(user.name)}</h2>
			<table id="shiftTable">
				<thead>
					<tr>
						<th>月</th>
						<th>火</th>
						<th>水</th>
						<th>木</th>
						<th>金</th>
						<th>土</th>
						<th>日</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<c:forEach var="e" items="${dayList}" varStatus="vs">
							<c:if test="${e==null}"><td></td></c:if>
							<c:if test="${e!=null}">
								<td>
									${f:h(e)}
									<c:if test="${myShiftMap[e] != null}">
										<c:set var="shift" value="${myShiftMap[e]}"/>
										<span class="link">
											<c:choose>
												<c:when test="${myShiftMap[e].yasumi==0}">
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/1</c:set>
													<a href="${f:url(url)}">全休</a>
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/2</c:set>
													/ <a href="${f:url(url)}">AM休</a>
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/3</c:set>
													/ <a href="${f:url(url)}">PM休</a>
												</c:when>
												<c:when test="${myShiftMap[e].yasumi==1}">
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/0</c:set>
													<a href="${f:url(url)}">出勤</a>
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/2</c:set>
													/ <a href="${f:url(url)}">AM休</a>
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/3</c:set>
													/ <a href="${f:url(url)}">PM休</a>
												</c:when>
												<c:when test="${myShiftMap[e].yasumi==2}">
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/0</c:set>
													<a href="${f:url(url)}">出勤</a>
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/1</c:set>
													/ <a href="${f:url(url)}">全休</a>
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/3</c:set>
													/ <a href="${f:url(url)}">PM休</a>
												</c:when>
												<c:when test="${myShiftMap[e].yasumi==3}">
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/0</c:set>
													<a href="${f:url(url)}">出勤</a>
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/1</c:set>
													/ <a href="${f:url(url)}">全休</a>
													<c:set var="url">update/${f:h(shift.key)}/${shift.version}/2</c:set>
													/ <a href="${f:url(url)}">AM休</a>
												</c:when>
											</c:choose>
										</span>
									</c:if>
									<c:forEach var="shift" items="${shiftMap[e]}">
										<c:choose>
											<c:when test="${shift.yasumi==0}">
												<div class="all">${f:h(shift.userRef.model.name)}</div>
											</c:when>
											<c:when test="${shift.yasumi==2}">
												<div class="pm">${f:h(shift.userRef.model.name)}</div>
											</c:when>
											<c:when test="${shift.yasumi==3}">
												<div class="am">${f:h(shift.userRef.model.name)}</div>
											</c:when>
										</c:choose>
										
									</c:forEach>
								</td>
							</c:if>
							<c:if test="${vs.count % 7 == 0}"></tr><tr></c:if>
					</c:forEach>
					</tr>
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
