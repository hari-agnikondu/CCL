<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>

<link href="<c:url value="/resources/css/query-builder.default.css" />" rel="stylesheet" />
<script src="<c:url value="/resources/js/query-builder.standalone.js" />"></script>
<script src="<c:url value="/resources/js/querybuilder/bootstrap-datepicker.min.js" />"></script>
 
<script>
$(document).ready(function() { // OnLoad
	
	$('#rule-builder').queryBuilder({
		plugins: ['bt-tooltip-errors'],
  
		filters: JSON.parse($('#ruleFilters').val()) 
	});
	
	$('#rule-builder').queryBuilder('setRules', JSON.parse($('#jsonReq').val() ));
});

</script>

<body>

	<div class="body-container">
		<div class="container">
		
			<br>&nbsp;	
			<div id="feedBackTd" class="form-group text-center" > 
				<c:if test="${statusFlag=='success' }">
					<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
				<c:if test="${statusFlag!='success' }">
					<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			</div>			

			<form:form name="ruleForm" id="ruleForm" class='form-horizontal' commandName="ruleForm">
		
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
								<form:input title="Allowed Special Characters are .,;'_- " path="ruleName" id="ruleName"	readonly="true"					
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
										name="txnTypeId" value="${txnType.key}" /><spring:message code="rule.transactionType.${txnType.key}" />
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
							<div id="rule-builder"></div>
						</div>
										
						</div>
						</div>
						</div>
				</article>
			</section>		
			<form:hidden name="jsonReq" path="jsonReq" id="jsonReq" />
			<form:hidden name="filters" path="filters" id="ruleFilters" />
			</form:form>
		</div>
	</div>
	
<script src="<c:url value="/resources/js/clpvms/rule.js" />"></script>
	
</body> 
