<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>

<link href="<c:url value="/resources/css/query-builder.default.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/js/querybuilder/bootstrap-datepicker3.min.css" />" rel="stylesheet" />
<script src="<c:url value="/resources/js/querybuilder/moment.min.js" />"></script> 
<script src="<c:url value="/resources/js/querybuilder/bootstrap-datepicker.min.js" />"></script>
<script src="<c:url value="/resources/js/query-builder.standalone.js" />"></script>





<style>
  a:link {
    color: black;
}
  </style>

  <div class="modal fade" id="define-constant-viewTransaction" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				
					<div class="modal-content">
						<div class="modal-body col-lg-12" id="load1">
					
							
						</div>
						<div class="modal-footer">
							<button data-dismiss="modal" class="btn btn-primary gray-btn"><spring:message
									code="button.back" /></button>
						</div>

					</div>
				
			</div>
		</div>	
	<div class="body-container">
		<div class="container">
		
			<br>&nbsp;	
			<div id="feedBackTd" class="form-group text-center" > 
				<c:if test="${statusFlag=='success' }">
					<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
				<c:if test="${statusFlag!='success' }">
					<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			</div>			

			<form:form name="ruleForm" id="ruleForm" method="post" action="saveRule"  
				class='form-horizontal' commandName="ruleForm">
		
			<section class="content-container">	

				<article class="col-lg-12">	

					<ul class="nav nav-tabs col-lg-11 col-lg-offset-0.5">
						<li class='active SubMenu'><a data-toggle='tab'><i class="glyphicon glyphicon-tags"></i> 
							<spring:message code="rule.transactionFilter" /></a></li>
					</ul>
								
					<div class="tabresp tab-content">
						<div class="tab-pane fade in active  graybox col-lg-11 col-lg-offset-0.5" id="rule">
						<div class="text-right mandatory-red"><spring:message code="label.mandatory" text="*Mandatory" />
					</div>	  

					<div class="col-lg-12">
						<div class="col-lg-12">

							<div class="col-lg-2">
								<label for="ruleName"><spring:message code="rule.ruleName"/><font
									color='red'>*</font></label>
							</div>
							
							<div class="col-lg-4">
								<form:select path="ruleId" id="ruleId" class="dropdown-medium" onchange="fillRuleName(this);getRule();">
									<form:option value="-1" label="--- NEW ---" />
									<form:options items="${rules}" />
								</form:select>
							</div>
					
							<div class="col-lg-4">
								<form:input title="Allowed Special Characters are .,;'_- " path="ruleName" id="ruleName"						
										type="textarea" minlength="2" maxlength="50"  onkeyup="return isAlphaNumericWithSpecialChars(this)"
										 />						
							</div>					
						</div>
						<br>&nbsp;	
				
						<div class="col-lg-12">

							<div class="col-lg-2">
								<label for="TransactionType">
								<spring:message code="rule.transactionType"/><font
									color='red'>*</font></label>
							</div>
							<div class="col-lg-8">
								<div id="txnTypes">
								<c:forEach var="txnType" items="${txnTypes}">
									<form:checkbox path="txnTypeId" id="txnTypeId"
										name="txnTypeId" value="${txnType.key}"/> 
										<a href="#" onclick="viewTransaction('${txnType.key}','${pageContext.request.contextPath}');"><spring:message  code="rule.transactionType.${txnType.key}" /></a>
									</c:forEach>																																		
								</div>
							</div>
						</div>
						<br>&nbsp;
						
						<div class="col-lg-12">
						
							<div class="col-lg-2">
								<label for="action">
									<spring:message code="rule.action"/><font
										color='red'>*</font></label>
							</div>

							<div class="col-lg-8">

								<form:select path="action" name="action" id="action" class="dropdown-medium">
									<form:option value="DECLINE_IF_FALSE"><spring:message code="rule.action.DECLINE_IF_FALSE"/></form:option>
									<form:option value="DECLINE_IF_TRUE"><spring:message code="rule.action.DECLINE_IF_TRUE"/></form:option>
									<form:option value="CONDITIONAL_IF_FALSE"><spring:message code="rule.action.CONDITIONAL_IF_FALSE"/></form:option>
									<form:option value="CONDITIONAL_IF_TRUE"><spring:message code="rule.action.CONDITIONAL_IF_TRUE"/></form:option>
								</form:select>
							</div>	
						</div>
						
						<div class="col-lg-12">
							<div class="col-lg-2">
								<label for="definedFilter">
									<spring:message code="rule.definedFilter"/><font
										color='red'>*</font></label>
							</div>
								
						</div>
						
						<div class="col-lg-12">
							<div id="builder-basic"></div>
						</div>
						
						<div class="col-lg-12 text-center">
								<security:authorize access="hasRole('ADD_RULE')">
									<button type="button" id="Button"
										class="btn btn-primary" onclick="saveRule();">
										<i class='glyphicon glyphicon-plus'></i><spring:message code="button.save" />
									</button>
								</security:authorize>
									<button type="button" onclick="resetRule()" class="btn btn-primary gray-btn">
										<i class='glyphicon glyphicon-refresh'></i><spring:message code="button.reset" />
									</button>
								</div>								
						</div>
						</div>
						</div>
				</article>
			</section>		
			<form:hidden name="jsonReq" path="jsonReq" id="jsonReq" />
			<form:hidden name="filters" path="filters" id="ruleFilters" />
			<input type="hidden" name="operators_list"  id="operators_list"  value="${operators_list}"/>
			<input type="hidden" name="OPERATORS_CONFIG"  id="OPERATORS_CONFIG" value="${OPERATORS_CONFIG}" />
			<input type="hidden" name="operators"  id="operators" value="${operators}" />
			
			

			</form:form>
		</div>
	</div>
<%-- <script src="<c:url value="/resources/js/querybuilder/demo-basic.js" />"></script> --%>	
<script src="<c:url value="/resources/js/clpvms/rule.js" />"></script>
	
 
