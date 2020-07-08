<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script src="${pageContext.request.contextPath}/resources/js/clpvms/product.js"></script>


<div class="container">


	<div class="row-fluid">

		<!-- <div class="form-group text-center" id="error"></div>

		<table style="text-align: center" width="90%">
			<tr>
				<td id="selectionError"></td>
			</tr>
		</table> -->

<%-- 		<div class="prodaln">
			<div>
				<center>
					<h6>
						<font class="errormsg"><b>${statusMessage}</b></font>
					</h6>
				</center>
			</div>
			<div>
				<center>
					<h6>
						<font style="color: green;"><b>${status}</b></font>
					</h6>
				</center>
			</div>
		</div> --%>
		<%-- <div class="aln_iss">
		<span id='hidemydata'> <c:if test="${successstatus !=''}">
				<h4>
					<p class="">
						<b><font size="3"><center style="color: green;">
									<strong>${successstatus}</strong>
								</center></font></b>
					</p>
				</h4>
			</c:if> <c:if test="${failstatus !=''}">
				<h4>
					<p class="">
						<b><font size="3"><center style="color: red;">
									<strong>${failstatus}</strong>
								</center></font></b>
					</p>
				</h4>
			</c:if>
		</span>
</div> --%>

 <c:if test="${successstatus !=''}">
						<div class="col-lg-12 success-green text-center" id="message" >
						<b>${successstatus}</b>
					</div>
			
			</c:if> 
			<c:if test="${failstatus !=''}">
			
					
					<div id="errmessage" class="error-red col-lg-12 text-center">
						<b>${failstatus}</b>
					</div>
				
			</c:if>
			
		<!-- graybox -->
		<!-- <div class="graybox col-lg-5 form-col-6 sb3"> -->
<article class="col-lg-12"> 
		<div class="graybox col-lg-5 col-lg-offset-3">



			<form:form id="productFormId" method="post" commandName="productForm">

				<div class="box-title">
					<h3>
						<spring:message code="product.searchProduct" />
					</h3>
				</div>

				<div class="form">

					<div class="col-lg-12">

						<div class="col-lg-4  ">
							<label for="search_requesterName"> <spring:message
									code="product.enterProductName" /><font color='red'></font></label>
						</div>



						<div class="col-lg-6">

							<form:input title="Allowed Special Characters are .,;'_- " path="productName" id="productNameId"
								class="notEmpty isAlphaNum textbox xlarge4label"
								name='search_productName'
								onkeyup="return isAlphaNumericWithNewSpecialCharsProdName(this)"
										
								type="textarea" maxlength="100" />
							<font color="red"><span id="productNameIdError"> </span></font>
							<div>
								<form:errors path="productName" id="productNameId"
									class="textbox xlarge4label" name='search_productName'
									cssStyle="color:red" />
							</div>


						</div>


						<div class="col-lg-2">
							
								<button type="submit" onclick="searchProduct(event)"
									class="btn btn-primary" id="search_submit">
									<i class="glyphicon glyphicon-search"></i>
									<spring:message code="button.search" />
								</button>
							
							<!-- 		<p class="text-center">
								<button type="submit" onclick="searchAll()"
									class="btn btn-primary" id="search_submit2">
									<i class="glyphicon glyphicon-search"></i> Search All
								</button>
							</p> -->
						</div>
						<!-- 
						<div class="sb2">
						
						</div> -->
						<div class="">
							<div class="col-lg-6 col-lg-offset-4">
								<label> <spring:message code="product.hint" /></label>
							</div>
						</div>
						<input id="productId" name="productId" type="hidden" value=""/>
					<!-- 	<input id="issid" name="issid" type="hidden" value="" /> -->
						 <input
							id="searchType" name="searchType" type="hidden"
							value="${SearchType}" /> <input id="searchedName"
							name="searchedName" type="hidden" value="" />
							 <input
							id="retrievedName" name="retrievedName" type="hidden"
							value="${SearchedName}" /> 
							<input type="hidden" name="jsPath"
							id="jsPath"
							value="${pageContext.request.contextPath}/resources/JS_Messages/" />
					</div>
					<br />

				</div>


				
		</div>
		</article>
		</form:form>
	
	</div>
	
</div>
<article class="col-lg-12">
		<div class="pull-right">
				<security:authorize access="hasRole('ADD_PRODUCT')">
					<button type="button" class="btn btn-primary btn-add"
						style=""
						onclick="clickAddProduct()">
						<i class='glyphicon glyphicon-plus'></i>
						<spring:message code="product.addProduct" />
					</button>
				</security:authorize>

				</div>
</article>
<c:if test="${showGrid =='true' }">




	<div class="group">

		<h3>
			<i class="icon-table"></i>
		</h3>

		<div id="tableDiv">
			<table id=""
				class="table dataTable table-hover table-striped table-bordered datagridwithsearch "
				style="width: 100% !important;">


				<thead class="table-head">
					<tr>
						<th><spring:message code="product.productName" /></th>
						<th><spring:message code="product.partnerName" /></th>
						<%-- <th><spring:message code="product.parentProductName" /></th> --%>
						<th><spring:message code="product.issuerName" /></th>

						<th><spring:message code="product.action" /></th>

					</tr>
				</thead>
				<tbody class="row">

					<c:forEach items="${productTableList}" var="requesterDetails"
						varStatus="status">
						<tr>
							<td>${requesterDetails.productId}:${requesterDetails.productName}</td>
							<td id="${requesterDetails.partnerName}">${requesterDetails.partnerName}</td>
							<%-- <td id="${requesterDetails.parentProductName}">${requesterDetails.parentProductName}</td> --%>
							<td id="${requesterDetails.issuerName}">${requesterDetails.issuerName}</td>
							<td id="${requesterDetails.action}"><input
								id="id${status.index}" name="id" type="hidden"
								value="${requesterDetails.productId}" />
								<security:authorize access="hasRole('EDIT_PRODUCT')">
								<button type="submit" class="btn btn-primary-table-button" id="search_submit"
									onclick="showEditProduct(${requesterDetails.productId});">
									<i class='glyphicon glyphicon-edit'></i>
									<spring:message code="button.edit" />
								</button>
								</security:authorize>
								<security:authorize access="hasRole('VIEW_PRODUCT')">
								<button type="submit" class="btn btn-primary-table-button" id="search_submit"
									onclick="showViewProduct(${requesterDetails.productId});">
									<i class='glyphicon glyphicon-list'></i>
									<spring:message code="button.view" />
								</button>
								</security:authorize>
								
					
                               <security:authorize
                                    access="hasRole('DOWNLOAD_PRODUCT')">
                                    <button type="submit" class="btn btn-primary-table-button"
                                    id="download_pdf"
                                    onclick="downloadPdf(${requesterDetails.productId});">
                                    <i class='glyphicon glyphicon-download-alt'></i>
                                    <spring:message code="button.download" />
                 
                                </button>
                                    </security:authorize>
								</td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</c:if>

</div>

</div>

<!-- <script>
function showEditProduct()
{
$("#productFormId").attr('action','showEditProduct');
$("#productFormId").submit();
}
</script> -->