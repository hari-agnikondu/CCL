<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div class="body-container" style="min-height: 131px;">
	<div class="container">
		<article class="col-lg-12">
			<div class="group">


				<table id="tableViewCard"
					class="table table-hover table-striped table-bordered dataTable"
					style="width: 100% !important;">
					<thead class="table-head" title="View Cards">
						<tr>
							<th style="width: 100px"><spring:message
									code="customerProfile.panNumber" text="PAN NUMBER" /></th>
							<th style="width: 100px"><spring:message
									code="customerProfile.cardStatus" text="Card Status" /></th>

						</tr>
					</thead>
					<tbody>

						<c:forEach items="${cardList}" var="card">
							<tr id="${card[0] }">
								<td>${card[0]}</td>
								<td>${card[1] }</td>
							</tr>
						</c:forEach>
					</tbody>

				</table>

			</div>
		</article>

	</div>
</div>