<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<html>
<head>
<script src="<c:url value="/resources/js/clpvms/common.js"/>"></script>
<script src="<c:url value="/resources/js/clpvms/groupAccessId.js"/>"></script>
<script src="<c:url value="/resources/js/multiselect.min.js"/>"></script>


</head>
<body onload="groupAccessFunc();">

	<input type="hidden" name="jsPath" id="jsPath"
		value="${pageContext.request.contextPath}/resources/JS_Messages/" />

	<div class="body-container" style="min-height: 131px;">
		<div class="container">
			<div class="modal fade" id="define-constant-edit" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">

					<div class="modal-content">
						<div class="modal-body col-lg-12">
							<div class="col-lg-12" style="display: inline-block">

								<span
									style="width: 100%; display: inline-block; word-wrap: break-word;">
									Do you want to update the Group Access "<b><span
										id="groupAccessIdForEditDip"></span></b>" ?
								</span>

							</div>
						</div>
						<div class="modal-footer">
							<button type="button" onclick="goUpdate()"
								class="btn btn-primary">
								<i class="glyphicon glyphicon-saved"></i>
								<spring:message code="button.update" />
							</button>
							<button data-dismiss="modal" class="btn btn-primary gray-btn">
								<spring:message code="button.cancel" />
							</button>

						</div>

					</div>
				</div>
			</div>

			<div class="container">
				<section class="content-container">
					<article class="col-lg-12">
						<div class="graybox col-lg-2 col-lg-offset-4 ">
							<c:choose>
								<c:when test='${addonloadFlag==true}'>
									<input type="radio" value="add" id="groupAccess_Add"
										name="groupAccess_button" onclick="groupAccessFunc();" checked />

									<label class='radiobox-line' for="groupAccess_Add"><spring:message
											code="groupAccessId.groupAccess_Add" /></label>

									<input type="radio" id="groupAccess_Edit"
										name="groupAccess_button" data-skin="square" data-color="blue"
										onclick="groupAccessFunc();" />
									<label class='radiobox-line' for="groupAccess_Edit"><spring:message
											code="groupAccessId.groupAccess_Edit" /></label>
								</c:when>

								<c:otherwise>
									<input type="radio" value="add" id="groupAccess_Add"
										name="groupAccess_button" onclick="groupAccessFunc();" />

									<label class='radiobox-line' for="groupAccess_Add"><spring:message
											code="groupAccessId.groupAccess_Add" /></label>

									<input type="radio" id="groupAccess_Edit"
										name="groupAccess_button" data-skin="square" data-color="blue"
										onclick="groupAccessFunc();" checked />
									<label class='radiobox-line' for="groupAccess_Edit"><spring:message
											code="groupAccessId.groupAccess_Edit" /></label>
								</c:otherwise>
							</c:choose>
						</div>
					</article>
				</section>
			</div>

			<div id="addGroupAccessName">
				<form:form name="addGroupAccessId" id="addGroupAccessId" action="#"
					method="POST" class='form-horizontal '
					commandName="addGroupAccessId">
					<section class="content-container">
						<article class="col-lg-12">

							<ul class="nav nav-tabs col-lg-7 col-lg-offset-2">
								<li class="active SubMenu"><a href="#addGroupAccessId"
									data-toggle="tab"> <i class="glyphicon glyphicon-tags"></i>
										<spring:message code="groupAccessId.addGroupAccessIdLabel" /></a></li>
							</ul>

							<div class="tabresp tab-content">
								<div
									class="tab-pane fade in active graybox col-lg-7 col-lg-offset-2"
									id="groupAccessIdDiv">

									<div class="text-right mandatory-red">
										<spring:message code="label.mandatory" text="*Mandatory" />
									</div>

									<article class="col-lg-12">
									<c:if test="${statusMessage!='' && statusMessage!=null}">
										<div class="text-center error-red"  id="serviceErrMsg" >
											<b>${statusMessage}</b>
										</div>
									</c:if>

									<c:if test="${status!='' && status!=null}">
										<div class="text-center success-green">
											<b>${status}</b>
										</div>
									</c:if>
									</article>

									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="EnterGroupAccessName"><spring:message
													code="groupAccessId.groupAccessName" /><font color='red'>*</font></label>
										</div>
										<div class="col-lg-9">
											<form:input path="groupAccessName" id="groupAccessName"
												autocomplete="off" type="textarea" maxlength="50"
												onkeyup="return isAlphaNumericWithSpace(this);"
												onblur="validateGroupAccessFields(this.form.id,this.id);" />
											<div>
												<form:errors path="groupAccessName" cssClass="fieldError" />
											</div>
										</div>
									</div>
									<div class="col-lg-12"></div>

									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="partnerId"><spring:message
													code="groupAccessId.partnerName" /> <font color='red'>*</font></label>
										</div>
										<!-- Multi select for card range starts -->

										<div class="col-lg-4 select-multiple">
											<div>Available</div>

											<form:select path="" name="search_partnerIdList"
												id="multiselect" size="8" multiple="multiple">

												<c:forEach items="${partnerList}" var="partnerList">
													<c:if test="${partnerList.partnerId !=null }">
														<form:option value="${partnerList.partnerId}">
						${partnerList.partnerName}</form:option>
													</c:if>
												</c:forEach>
											</form:select>
										</div>

										<div class="col-lg-1 padding-top-lg col-lg-offset-1">
											<button type="button" id="multiselect_rightAll"
												class="btn btn-block">
												<i class="glyphicon glyphicon-forward"></i>
											</button>
											<button type="button" id="multiselect_rightSelected"
												class="btn btn-block">
												<i class="glyphicon glyphicon-chevron-right"></i>
											</button>
											<button type="button" id="multiselect_leftSelected"
												class="btn btn-block">
												<i class="glyphicon glyphicon-chevron-left"></i>
											</button>
											<button type="button" id="multiselect_leftAll"
												class="btn btn-block">
												<i class="glyphicon glyphicon-backward"></i>
											</button>
										</div>


										<div class="col-lg-3 select-multiple">
											<div>Selected</div>
											<form:select name="to[]" id="multiselect_to"
												path="selectedPartnerList" size="8" multiple="multiple" onchange="multiSelectValidation(this.form.id,event)" >
												<c:forEach items="${partnerIds}" var="partnerIds">
													<c:if test="${partnerIds.partnerId !=null }">
														<form:option value="${partnerIds.partnerId}" >
								${partnerIds.partnerName}</form:option>
													</c:if>
												</c:forEach>
											</form:select>
										
								
										<div>
											<form:errors path="selectedPartnerList"
												id="selectedPartnerListErr" cssClass="fieldError" />
										</div>
										</div>
									</div>
									<!-- Multi select for card range ends -->
									<article class="col-lg-12">	
										<div class="col-lg-12  text-center">
								<button type="button" class="btn btn-primary"
									onclick="addGroupAccessSubmit(this.form.id,event);">
									<i class="glyphicon glyphicon-plus"></i>
									<spring:message code="button.add" />
								</button>

								<button type="button" onclick="goResetAddGroupAccess();"
									class="btn btn-primary gray-btn">
									<i class="glyphicon glyphicon-refresh"></i>
									<spring:message code="button.reset" />
								</button>

								<button type="button" class="btn btn-primary gray-btn"
									onclick="backToAddGroupAccess();">
									<i class="glyphicon glyphicon-backward"></i>
									<spring:message code="button.back" />
								</button>
							</div>
							</article>
								</div>
							</div>
						
						</article>
					</section>
				</form:form>
			</div>
			<div id="editGroupAccessName" style="display: none;">

				<form:form name="editGroupAccessId" id="editGroupAccessId"
					action="#" method="POST" class='form-horizontal '
					commandName="editGroupAccessId">
					<section class="content-container">
						<article class="col-lg-12">

							<ul class="nav nav-tabs col-lg-7 col-lg-offset-2">
								<li class="active SubMenu"><a href="#addGroupAccessId"
									data-toggle="tab"> <i class="glyphicon glyphicon-tags"></i>
										<spring:message code="groupAccessId.editGroupAccessIdLabel" /></a></li>
							</ul>

							<div class="tabresp tab-content">
								<div
									class="tab-pane fade in active graybox col-lg-7 col-lg-offset-2"
									id="groupAccessIdDivEdit">

									<div class="text-right mandatory-red">
										<spring:message code="label.mandatory" text="*Mandatory" />
									</div>

									<article class="col-lg-12">
									<c:if test="${statusMessageEdit!='' && statusMessageEdit!=null}">
										<div class="text-center error-red"  id="serviceErrMsg">
											<b>${statusMessageEdit}</b>
										</div>
									</c:if>

									<c:if test="${status!='' && status!=null}">
										<div class="text-center success-green">
											<b>${status}</b>
										</div>
									</c:if>
									</article>
									
									<!-- This hidden is used to send the server url to the js file starts -->
									<input type="hidden" id="srvUrl" value="${srvUrl}" />
									<!-- This hidden is used to send the server url to the js file ends -->
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="SelectGroupAccessName"><spring:message
													code="groupAccessId.groupAccessName" /><font color='red'>*</font></label>
										</div>

										<div class="col-lg-9">

											<form:select path="groupAccessId" id="groupAccessIdForEdit"
												onchange="getGroupAccessPartnersforUpdateGroupAccess()"
												onblur="validateDropDownForGroupAccessFields(this.form.id,this.id)">
												<form:option value="-1" label="- - - Select - - -" />
												<form:options items="${groupAccessMap}" />
											</form:select>

											<div>
												<form:errors path="groupAccessId" id="groupAccessIdErr"
													cssClass="fieldError" />
											</div>
										</div>
									</div>
									<div class="col-lg-12"></div>
									<form:hidden path="groupAccessName" id="groupAccessNameForEdit" />
									<div class="col-lg-12">
										<div class="col-lg-3">
											<label for="partnerId"><spring:message
													code="groupAccessId.partnerName" /> <font color='red'>*</font></label>
										</div>
										<!-- Multi select for card range starts -->

										<div class="col-lg-4 select-multiple">
										<div>Available</div>
										
											<form:select path="" name="search_partnerIdList"
												id="multiselectEdit" size="8" multiple="multiple">

												<c:forEach items="${partnerList}" var="partnerList">
													<c:if test="${partnerList.partnerId !=null }">
														<form:option value="${partnerList.partnerId}">
						${partnerList.partnerName}</form:option>
													</c:if>
												</c:forEach>
											</form:select>
										</div>



										<div class="col-lg-1 col-lg-offset-1 padding-top-lg">
											<button type="button" id="multiselectEdit_rightAll"
												class="btn btn-block">
												<i class="glyphicon glyphicon-forward"></i>
											</button>
											<button type="button" id="multiselectEdit_rightSelected"
												class="btn btn-block">
												<i class="glyphicon glyphicon-chevron-right"></i>
											</button>
											<button type="button" id="multiselectEdit_leftSelected"
												class="btn btn-block">
												<i class="glyphicon glyphicon-chevron-left"></i>
											</button>
											<button type="button" id="multiselectEdit_leftAll"
												class="btn btn-block">
												<i class="glyphicon glyphicon-backward"></i>
											</button>
										</div>

									<div class="col-lg-3">
										<c:set var="partnerIds" value="${groupAccess.partnerId}" />
										<div>Selected</div>
										
											<form:select name="to[]" id="multiselectEdit_to"
												path="selectedPartnerList" size="8" multiple="multiple">

												<c:forEach items="${partnerIds}" var="partnerIds">
													<c:if test="${partnerIds.partnerId !=null }">
														<form:option value="${partnerIds.partnerId}">
								${partnerIds.partnerName}</form:option>
													</c:if>
												</c:forEach>
											</form:select>

											<div>
												<form:errors path="partnerList" id="partnerListError"
													cssClass="fieldError" />
												<form:errors path="selectedPartnerList"
													id="selectedPartnerListErr" cssClass="fieldError" />
											</div>
										</div>
									</div>
									<!-- Multi select for card range ends -->
									<article class="col-lg-12">	
										<div class="col-lg-12  text-center">
								<button type="button" id="updateButton"
									onclick="updateGroupAccess(this.form.id,event);"
									data-toggle="modal" class="btn btn-primary">
									<i class="glyphicon glyphicon-saved"></i>
									<spring:message code="button.update" text="Update" />
								</button>

								<button type="button" class="btn btn-primary gray-btn"
									onclick="backToUpdateGroupAccess();">
									<i class="glyphicon glyphicon-backward"></i>
									<spring:message code="button.back" />
								</button>
							</div></article>
								</div>
							</div>
						
						</article>
					</section>
				</form:form>

			</div>
		</div>
	</div>
</body>

<script>
$('#multiselect').multiselect();
$('#multiselectEdit').multiselect();

</script>
</html>