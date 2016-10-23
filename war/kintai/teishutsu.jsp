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
	<style type="text/css">
		div.chikoku {
			background-color:#f2dae8;
		}
	</style>
</head>

<body>
<form action="${f:url('teishutsuUpdate')}" method="post">
	<input type="hidden" ${f:hidden("key")}/>
	<input type="hidden" ${f:hidden("version")}/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>勤怠</h1>
			
			<c:if test="${loginUser.authKintai}">
				ユーザー：
				<select name="userKey">
					<c:forEach var="e" items="${userList}">
						<option ${f:select("userKey", f:h(e.key))}>${f:h(e.name)}</option>
					</c:forEach>
				</select>
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
				<button type="submit" onclick="this.form.action='${f:url('teishutsu')}';">照会</button>
			</c:if>
			
			<h2>勤務表提出　-　${f:h(year)}年${f:h(month+1)}月　${f:h(user.name)}</h2>
			
			<c:if test="${!empty(errorMessage)}"><span class="err">${f:h(errorMessage)}</span><hr/></c:if>
			
			<table id="kinmu">
				<thead>
					<tr>
						<th>日付</th>
						<th>出勤</th>
						<th>外出</th>
						<th>戻り</th>
						<th>退勤</th>
						<th>残業</th>
						<th>休み</th>
						<th>お昼休み</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${dayList}" varStatus="vs">
						<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
							<c:set var="dakoku" value="${map[e]}"/>
							<c:set var="teiji" value="${teijiMap[e]}"/>
							<c:set var="date" value="${dateMap[e]}"/>
							<td><fmt:formatDate value="${date}" pattern="dd日（E）"/></td>
							<td>
								<div<c:if test="${dakoku.start!=null && teiji.startTimeInt<dakoku.startInt}"> class="chikoku"</c:if>>
									<fmt:formatDate value="${dakoku.start}" pattern="HH時mm分"/>
								</div>
							</td>
							<td>
								<c:if test="${dakoku.start!=null}">
									<fmt:formatDate value="${dakoku.out}" pattern="HH時mm分"/>
								</c:if>
							</td>
							<td>
								<div<c:if test="${dakoku.out!=null && dakoku.in==null}"> class="chikoku"</c:if></div>
									<c:if test="${dakoku.out!=null}">
										<fmt:formatDate value="${dakoku.in}" pattern="HH時mm分"/>
									</c:if>
								</div>
							</td>
							<td>
								<div<c:if test="${(dakoku.end!=null && dakoku.endInt<teiji.endTimeInt)
										|| (dakoku.start!=null && dakoku.end==null)}"> class="chikoku"</c:if></div>
									<c:if test="${dakoku.start!=null}">
										<fmt:formatDate value="${dakoku.end}" pattern="HH時mm分"/>
									</c:if>
								</div>
							</td>
							<td>
								<c:if test="${dakoku!=null && dakoku.zangyo}">残業</c:if>
							</td>
							<td>
								<c:if test="${dakoku!=null}">
									<c:set var="name">yasumi${e}</c:set>
									<c:if test="${empty(errorMessage)}">
										<input type="checkbox" id="yasumi${e}" ${f:checkbox(name)}/><label for="yasumi${e}">時間休を取る</label>
									</c:if>
									<c:if test="${!empty(errorMessage) && dakoku.yasumi}">時間給を取る</c:if>
								</c:if>
							</td>
							<td>
								<c:if test="${dakoku!=null}">
									<c:set var="name">ohiru${e}</c:set>
									<c:if test="${empty(errorMessage)}">
										<input type="checkbox" id="ohiru${e}" ${f:checkbox(name)}/><label for="ohiru${e}">お昼休み有</label>
									</c:if>
									<c:if test="${!empty(errorMessage) && dakoku.ohiru}">お昼休み有</c:if>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<hr/>
			<c:if test="${empty(errorMessage)}">
				<button type="submit">勤務表を提出する</button>
			</c:if>
			<button type="button" onclick="window.location.href='${f:url('/kintai')}';">戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/')}';">トップへ戻る</button>
			
			
			
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
