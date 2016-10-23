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
	div.content {
		margin-left:10px;
	}
	div.content button.dakoku {
		width:300px;
		height:180px;
		font-size:35px;
		margin-top:20px;
		margin-left:10px;
		background-color:#f2dae8;
		border-radius: 5px; 
	}
	
	div.content button.s1 {
		background-color:#c9d744;
	}
	
	div.content button.s2 {
		background-color:#cccccc;
	}
	
	div#dakokuBox {
		display:none;
	}
	div#dakokuBox button.dakoku {
		background-color:#ebc061;
	}
	
	h1#userName {
		font-size:40px;
	}
	span.dakokuIcon {
		color:#b3ce5b;
	}
	
	button#shiftButton {
		background-color:#b2d6d4;
	}
	table#shiftTable {
		width:100%;
	}
	span#dayNum {
		font-size:x-large;
	}
	table#shiftTable div.all {
			width:100%;
			background-color:#afd0ef;
		}
		table#shiftTable div.am {
			width:50%;
			background-color:#eeea55;
		}
		table#shiftTable div.pm {
			width:50%;
			margin-left:50%;
			background-color:#f2dae8;
		}
		td.beforeDay {
			opacity:0.5;
		}
	</style>
	
</head>

<body>
<form action="${f:url('./dakokuShift')}" method="post">
	<jsp:include page="/header2.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>
				クラウド打刻-シフト <span class="dakokuIcon"><i class="fa fa-hand-o-up"></i></span>　
				<i class="fa fa-clock-o"></i> <fmt:formatDate value="${today}" pattern="yyyy-MM-dd（E）"/>
				<span id="time"></span>
			</h1>
			
			<button type="button" onclick="window.location.href='/kintai/dakoku';">戻る</button>
			
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
							<td <c:if test="${e<today}">class="beforeDay"</c:if>>
								<span id="dayNum"><fmt:formatDate value="${e}" pattern="d日"/></span>
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
						<c:if test="${vs.count % 7 == 0}"></tr><tr></c:if>
					</c:forEach>
					</tr>
				</tbody>
			</table>
		</div>
		
	</div>
</form>
</body>
</html>
