<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/redemptionDelay.js"></script>

<body class="dashboard" onload="onloadRedeemDiv()">
					

	<div class="body-container" style="min-height: 131px;">

		<div class="container">
			<div class="wrapper">
				<div class="text-center">
					<font class="errormsg"><b>${statusMessage}</b></font>
				</div>
							<c:if test="${successstatus !=''}">
										<div class="text-center  success-green" id="statusMerchant">
											<b>${successstatus}</b>
										</div>
									</c:if>
									<c:if test="${failstatus !=''}">

										<div class="text-center  error-red col-lg-12 padding"
											id="formErrorId">
											<b>${failstatus}</b>

										</div>

									</c:if>
			</div>

			<form:form name="frmredemption" id="frmredemption" method="POST"
				class='form-horizontal' commandName="redemptionDelayForm">

				<div id="ErrorStatus" class="text-center strong">
					<font color='red'>${ErrorStatus}</font>
				</div>

				<section class="content-container">
					<article class="col-lg-12">

						<ul class="nav nav-tabs col-lg-6 col-lg-offset-3">
							<li class='active SubMenu'><a data-toggle='tab'> <i
									class=""></i> <spring:message
										code="redemptionDelay.Config" /></a></li>
						</ul>

						<div class="tabresp tab-content">
							<div
								class="tab-pane fade in active graybox col-lg-6 col-lg-offset-3"
								id="product">
								<div class="text-right mandatory-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<div class="">
										<div class="col-lg-4">
											<label for="Product Name"> <spring:message
													code="redemption.productName" /> <font color='red'>
													*</font></label>
										</div>
										<div class="col-lg-8">
											<form:select path="productName" id="productRedeemId"
												class="dropdown-medium"
												onblur="return validateDropDownRedeemMerchantProduct(this.form.id,this.id);">
												<form:option value="-1" label="- - - Select - - -" />

												<c:forEach items="${productDropDown}" var="product">
													<option value="${product.productId}~${product.productName}"
														<c:if test="${product.productName eq redemptionDelayForm.productName}">selected="true"</c:if>>${product.productId}:${product.productName}</option>
												</c:forEach>

											</form:select>


											<div>
												<form:errors path="productName" id="productRedeemId"
													maxlength="100" cssStyle="color:red" />
											</div>
										</div>
									</div>

									<div class="col-lg-12">
										<div class="col-lg-4">
											<label for="Merchant Name"> <spring:message
													code="redemption.merchantName" /><font color='red'>
													*</font></label>
										</div>
										<div class="col-lg-8">
											<form:select path="merchantName" id="merchantDelayId"
												class="dropdown-medium"
												onblur="return validateDropDownRedeemMerchantProduct(this.form.id,this.id);"
												onChange="getfillDelayTime(this.form.id);">
												<form:option value="-1" label="- - - Select - - -" />
												<%-- <form:options items="${merchantDropDown.merchantName}"/> --%>
												<c:forEach items="${merchantDropDown}" var="merchant">
													<option
														value="${merchant.merchantId}~${merchant.merchantName}"
														<c:if test="${merchant.merchantName eq redemptionDelayForm.merchantName}">selected="true"</c:if>>${merchant.merchantName}</option>
												</c:forEach>
											</form:select>
											<div>
												<form:errors path="merchantName" id="merchantDelayId"
													cssStyle="color:red" />
											</div>
										</div>

									</div>


								</div>





								<br>&nbsp;
								
				<div class="col-lg-12 text-right">
			
					<button type="button" class="btn btn-primary"
						style="bottom: 15px; position: relative;"
						onclick="clickAddMerchantRedemptionDelay()">
						<i class='glyphicon glyphicon-plus'></i>
						<spring:message code="merchant.button.addMerchant" />
					</button>
				
				</div>


							<!-- 	<table border="1" class='form_bg' id="dataTable"
									name="dataTable">
									<tr>
										<td class='label2'><input type="button" name="delete"
											onclick="deleteRow();" value="Delete"></td>
										<TD class='label2' id="startTime">Start Time(HH:MM:SS)</TD>
										<Td class='label2' id="endTime">End Time(HH:MM:SS)</Td>
										<TD class='label2' id="delayinMin">Redemption Delay(In
											Minutes)</TD>
										<td><input type='button' name='addCardpackage'
											value="Add" onClick="addRow(this.form.id)"></td>
									</tr>

								</table> -->
								
								
								
								
								
								
								
								
								
								
								<!-- <input type="hidden" name="merchantName" id="merchantName"
									value="">  --><input type="hidden" name="operationList"
									id="operationList" value=""> <!--  <input type="hidden"
									name="productName" id="productName" value="">  -->
									<input type="hidden" id="addurl" value="${addurl}" />
									
									<!-- <input type="hidden" id="addurl" name="addurl" value=""/> -->
									<input type="hidden" id="overlapId" name="overlapId" value=""/>
									
									

								
								
								
								
							</div>
							
							
<!-- table starts here -->							
							
							


		<h3>
			<i class="icon-table"></i>
		</h3>

		<div id="tableDiv">
			<!-- <table id="tableView" class="table table-hover table-striped table-bordered" style="width: 100% !important;"> -->
			<table id="dataTable" name="dataTable"
				class="table-hover table-striped table-bordered dataTable"
				style="width: 100% !important;">
				


				<thead  class="table-head">
					<tr>
						
 						<th style="width:5%;"><input type="button" class="btn btn-primary" style="width:0em;" name="delete" onclick="deleteRow();" value="Delete"></th>
						<th bgcolor="#337ab7" style="text-align:center; width:20%"><font color="#fff"><spring:message code="redemption.startTime" /></font></th>
						<th bgcolor="#337ab7" style="text-align:center; width:20%"><font color="#fff"><spring:message code="redemption.endTime" /></font></th>
						<th bgcolor="#337ab7" style="text-align:center; width:20%"><font color="#fff"><spring:message code="redemption.delay" /></font></th>
						<th style="width:5%;"><input type='button' class="btn btn-primary" style="width:0em;" name='addCardpackage' value="Add" onClick="addRow(this.form.id)"></th>
 
					</tr>
				</thead>
				<tbody class="row">

			
				</tbody>
			</table>
		</div>

		<div id="tableViewUsersStatus">
			<br>
		</div>
		
		<div class="col-lg-12 text-center">
									<button type="button" class="btn btn-primary"
										onclick="sendvalue(event,this.form.id);">
										<i class='glyphicon glyphicon-save'></i>
										<spring:message code="button.submit" />
									</button>

								</div>

	</div>

							
							
							
							
							
							
						</div>
					</article>
				</section>
			</form:form>


		</div>
	</div>
</body>

<script>


</script>