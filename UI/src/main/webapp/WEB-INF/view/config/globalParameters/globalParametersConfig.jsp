<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>

<html>
<head>
<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/globalParameter.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/css/jqueryui.css"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/jqueryui.js"></script>

<!-- added  for autocomplete -->
<link
	href="${pageContext.request.contextPath}/resources/css/autocomp/smooth/jquery-ui.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath}/resources/css/autocomp/jquery-ui.css"
	rel="stylesheet" />
</head>
<body OnLoad="selctedDateFormat();">
	<form:form name="globalParameters" id="globalParameters" action="#"
		method="POST" class='form-horizontal' commandName="globalParameters">
		<div class="body-container">
			<div class="container">
				<section class="content-container">
					<article class="col-lg-12">
						<ul class="nav nav-tabs col-lg-6 col-lg-offset-2">
							<li class='active'><a data-toggle='tab'><i class=""></i>
									<spring:message code="globalParameters.show" /></a></li>
						</ul>
						<div class="tabresp tab-content">
							<!-- <div class="tab-pane fade in active graybox col-lg-6 col-lg-offset-2" > -->
							<div
								class="tab-pane fade in active graybox col-md-6 col-lg-offset-2">
								<div class="text-right Error-red">
									<spring:message code="label.mandatory" text="*Mandatory" />
								</div>
								<div class="col-lg-12">
									<c:if test="${statusMessage!='' && statusMessage!=null}">
										<div class="aln error-red">
											<center>
												<b><font color="red">${statusMessage}</font></b>
											</center>
										</div>
									</c:if>
									<c:if test="${status!='' && status!=null}">
										<div class="aln success-green" id="statusId">
											<center>
												<b><font color="green">${status}</font></b>
											</center>
										</div>
									</c:if>
									<div class="col-lg-12 col-lg-offset-1">

										<!-- Customer Password Length -->

										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="Customer Password Length"> <spring:message
														code="globalParameters.customerPasswordLength" /><font
													color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:input
													path="globalParameters['customerPasswordLength']"
													id="customerPasswordLengthId" name="customerPasswordLength"
													onkeyup="return isNumericPasswordLength(this)"
													onblur="return validateglobalParamwithNumericMinMaxLength(this.form.id,this.id)"
													autocomplete="off" maxlength="2" class="textbox-large" />

												<div>
													<form:errors
														path="globalParameters['customerPasswordLength']"
														cssStyle="color:red" />
												</div>



											</div>
										</div>

										<!-- customer password length ends -->

										<!--Allowable Wrong Logins starts  -->

										<%-- 	<div class="col-lg-12">
				<div class="col-lg-4 space">
						<label for="Allowable Wrong Logins"><spring:message
								code="globalParameters.allowableWrongLogins"/><font color='red'>*</font></label>
					</div>											
						 <div class="col-lg-8">
						<form:input path="globalParameters['allowableWrongLogins']"
						 onkeyup="return isNumeric(this)" onblur="return validateglobalParamwithNumericMinMaxLength(this.form.id,this.id)" 
						  id="allowableWrongLoginsId" name="allowableWrongLogins"  
					 autocomplete="off" maxlength="4" class="textbox-large" />	
					 <div>
						<form:errors path="globalParameters['allowableWrongLogins']" cssClass="error"  cssStyle="color:red" />
						</div>
					 
											
							
					</div>		 									
				</div> --%>
										<!-- Allowable Wrong Logins ends -->

										<!-- Password Change Interval  -->
										<%-- 			<div class="col-lg-12">
				<div class="col-lg-4 space">
						<label for="Password Change Interval"><spring:message
								code="globalParameters.passwordChangeInterval"/><font color='red'>*</font></label>
					</div>											
						 <div class="col-lg-8">
						<form:input path="globalParameters['passwordChangeInterval']" 
						id="passwordChangeIntervalId" name="passwordChangeInterval" onkeyup="return isNumeric(this)" onblur="return validateglobalParamwithNumericMinMaxLength(this.form.id,this.id)" 
					 autocomplete="off" maxlength="4" class="textbox-large" />	
					  	<div>
						<form:errors path="globalParameters['passwordChangeInterval']" cssClass="error"  cssStyle="color:red" />
						</div>
									
							
					</div>		 									
				</div> --%>

										<!-- ends Password Change Interval -->
										<!-- Password Length  -->
										<%-- 			<div class="col-lg-12">
				<div class="col-lg-4 space">
						<label for="Password Length"><spring:message
								code="globalParameters.passwordLength"/><font color='red'>*</font></label>
					</div>											
						 <div class="col-lg-8">
						<form:input path="globalParameters['passwordLength']" id="passwordLengthId" name="passwordLength"
					onkeyup="return isNumeric(this)" onblur="return validateglobalParamwithNumericMinMaxLength(this.form.id,this.id)"  
					 autocomplete="off" maxlength="4" class="textbox-large" />	
					 <div>
						<form:errors path="globalParameters['passwordLength']" cssClass="error"  cssStyle="color:red" />
						</div>
										
							
					</div>		 									
				</div> --%>


										<!-- ends Password Length  -->
										<!-- Previous Passwords -->

										<%-- 	<div class="col-lg-12">
				<div class="col-lg-4 space">
						<label for="Previous Passwords"><spring:message
								code="globalParameters.previousPasswords"/><font color='red'>*</font></label>
					</div>											
						 <div class="col-lg-8">
						<form:input path="globalParameters['previousPasswords']" id="previousPasswordsId"
						 name="previousPasswords" onkeyup="return isNumeric(this)" onblur="return validateglobalParamwithNumericMinMaxLength(this.form.id,this.id)"  
					 autocomplete="off" maxlength="4" class="textbox-large" />	
					 	<div>
						<form:errors path="globalParameters['previousPasswords']" cssClass="error"  cssStyle="color:red" />
						</div>
										
							
					</div>		 									
				</div> --%>
										<!-- ends Previous Passwords -->

										<!-- Masking Char Value -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="Masking Char Value"><spring:message
														code="globalParameters.maskingCharValue" /><font
													color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:input path="globalParameters['maskingCharValue']"
													id="maskingCharValueId" name="maskingCharValue"
													onblur="return validateglobalParamwithNumericMinMaxLength(this.form.id,this.id)"
													autocomplete="off" maxlength="1" class="textbox-large" />
												<div>
													<form:errors path="globalParameters['maskingCharValue']"
														cssClass="error" cssStyle="color:red" />
												</div>


											</div>
										</div>

										<!-- ends Masking Char Value -->



										<!-- hsm  -->


										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="Select HSM"><spring:message
														code="globalParameters.hsm" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:select path="globalParameters['hsm']" id="hsmId"
													onblur="return validateDropDown(this.form.id,this.id)"
													class="dropdown-medium">
													<form:option value="-1" label="- - - Select - - -" />
													<form:option value="E" label="Eracom" />
													<form:option value="R" label="Racal" />
												</form:select>
												<div>
													<form:errors path="globalParameters['hsm']"
														cssClass="error" cssStyle="color:red" />
												</div>



											</div>
										</div>

										<!-- ends hsm -->
										<!-- date format -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="Select dateFormat"><spring:message
														code="globalParameters.dateFormat" /><font color='red'>*</font></label>
											</div>
											<div class="col-lg-8">
												<form:input path="globalParameters['dateFormat']"
													id="dateFormatId" class="dropdown-medium"
													style="display:none;" />

												<TABLE Border=1 class=''>

													<tr>

														<TD><select name="sel1" id="sel1" style="width: 60px">
																<option value='dd'>dd</option>
																<option value='mm'>mm</option>
																<option value='mon'>mon</option>
																<option value='yy'>yy</option>
																<option value='yyyy'>yyyy</option>
														</select></td>
														<TD><select name="seldel1" id="seldel1"
															style="width: 60px" onChange='changeDel2();'>
																<option value='/'>/</option>
																<option value='-'>-</option>
														</select></td>
														<TD><select name="sel2" style="width: 60px" id="sel2">
																<option value='mm'>mm</option>
																<option value='dd'>dd</option>
																<option value='mon'>mon</option>
																<option value='yy'>yy</option>
																<option value='yyyy'>yyyy</option>
														</select></td>
														<TD><select name="seldel2" id="seldel2"
															style="width: 60px" onChange='changeDel1();'>
																<option value='/'>/</option>
																<option value='-'>-</option>
														</select></td>
														<TD><select name="sel3" style="width: 60px" id="sel3">
																<option value='yyyy'>yyyy</option>
																<option value='mm'>mm</option>
																<option value='mon'>mon</option>
																<option value='yy'>yy</option>
																<option value='dd'>dd</option>
														</select></td>
													</tr>
												</table>
												<span id="dateMsg" class="error_empty" style="color: red;"></span>

											</div>
										</div>

										</br> </br> <input type="hidden" id="deliveryChannelList"
											name="deliveryChannelList" value="${deliveryChannelList}" />

										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="ChannelsForBLK&IND"><spring:message
														code="globalParameters.channelsForBLK&IND" /><font
													color='red'></font></label>
											</div>


											<div class="col-lg-8">
												<form:input path="globalParameters['Channel']" id="combobox"
													name="channelsForBlkandInd"
													onkeyup="return isAlphabetsWithComma(this)"
													onblur="return validateglobalParamwithNumericMinMaxLength(this.form.id,this.id)"
													autocomplete="off" class="textbox-large" />

												<div>
													<form:errors path="globalParameters['Channel']"
														cssStyle="color:red" />
												</div>



											</div>

										</div>

										<!-- MFT Outbound -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Outbound"> <spring:message
														code="globalParameters.mftOutbound" /></label>
											</div>
											<div class="col-lg-8">
												<c:set var="mftOutbound"
													value="${globalParameters.globalParameters['mftOutbound']}"></c:set>
												<c:choose>
													<c:when test="${not empty mftOutbound}">
														<div class="col-lg-4">
															<form:radiobutton path="globalParameters['mftOutbound']"
																value="Enable" id="mftOutboundEnable"
																name="mftOutboundEnable" data-color="blue" />
															<label class='radiobox-line'>Enable</label>
														</div>
														<div class="col-lg-4">
															<form:radiobutton path="globalParameters['mftOutbound']"
																value="Disable" id="mftOutboundDisable"
																name="mftOutboundDisable" data-skin="square"
																data-color="blue" />
															<label class='radiobox-line'>Disable</label>
														</div>
													</c:when>
													<c:otherwise>
														<div class="col-lg-4">
															<form:radiobutton path="globalParameters['mftOutbound']"
																value="Enable" id="mftOutboundEnable"
																name="mftOutboundEnable" data-skin="square"
																data-color="blue" />
															<label class='radiobox-line'>Enable</label>
														</div>
														<div class="col-lg-4">
															<form:radiobutton path="globalParameters['mftOutbound']"
																value="Disable" id="mftOutboundDisable"
																name="mftOutboundDisable" data-skin="square"
																data-color="blue" checked="checked" />
															<label class='radiobox-line'>Disable</label>
														</div>
													</c:otherwise>
												</c:choose>
												<form:errors path="globalParameters['mftOutbound']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<!-- MFT Inbound -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Inbound"> <spring:message
														code="globalParameters.mftInbound" /></label>
											</div>
											<div class="col-lg-8">
												<c:set var="mftInbound"
													value="${globalParameters.globalParameters['mftInbound']}"></c:set>
												<c:choose>
													<c:when test="${not empty mftInbound}">
														<div class="col-lg-4">
															<form:radiobutton path="globalParameters['mftInbound']"
																value="Enable" id="mftInboundEnable"
																name="mftInboundEnable" data-skin="square"
																data-color="blue" />
															<label class='radiobox-line'>Enable</label>
														</div>
														<div class="col-lg-4">
															<form:radiobutton path="globalParameters['mftInbound']"
																value="Disable" id="mftInboundDisable"
																name="mftInboundDisable" data-skin="square"
																data-color="blue" />
															<label class='radiobox-line'>Disable</label>
														</div>
													</c:when>
													<c:otherwise>
														<div class="col-lg-4">
															<form:radiobutton path="globalParameters['mftInbound']"
																value="Enable" id="mftInboundEnable"
																name="mftInboundEnable" data-skin="square"
																data-color="blue" />
															<label class='radiobox-line'>Enable</label>
														</div>
														<div class="col-lg-4">
															<form:radiobutton path="globalParameters['mftInbound']"
																value="Disable" id="mftInboundDisable"
																name="mftInboundDisable" data-skin="square"
																data-color="blue" checked="checked" />
															<label class='radiobox-line'>Disable</label>
														</div>
													</c:otherwise>
												</c:choose>
												<form:errors path="globalParameters['mftInbound']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<!-- Domain -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Domain"> <spring:message
														code="globalParameters.domain" /></label>
											</div>
											<div class="col-lg-8">
												<form:select path="globalParameters['domain']"
													id="mftDomain" class="dropdown-medium" >
													<form:option value="-1" label="--- Select ---" />
													<form:options items="${domainTypes}" />
												</form:select>
												<form:errors path="globalParameters['domain']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<!-- MFT URL -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Url"> <spring:message
														code="globalParameters.mftUrl" /></label>
											</div>
											<div class="col-lg-8">
												<form:input path="globalParameters['mftUrl']" id="mftUrl"
													name="mftUrl" autocomplete="off" maxlength="100"
													class="textbox-large"
													onkeyup="return isAlphaNumericWithSpl(this)"
													onblur="validateFields(this.form.id,this.id);" />
												<form:errors path="globalParameters['mftUrl']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<!-- MFT Post Back URL -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Post Back Url"> <spring:message
														code="globalParameters.mftPostBackUrl" /></label>
											</div>
											<div class="col-lg-8">
												<form:input path="globalParameters['mftPostBackUrl']"
													id="mftPostBackUrl" name="mftPostBackUrl"
													onkeyup="return isAlphaNumericWithSpl(this)"
													autocomplete="off" maxlength="100" class="textbox-large"
													onblur="validateFields(this.form.id,this.id);" />
												<form:errors path="globalParameters['mftPostBackUrl']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<!-- MFT Post Back URL Header -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Post Back URL Header"> <spring:message
														code="globalParameters.mftPostBackUrlHeader" /></label>
											</div>
											<div class="col-lg-8">
												<form:input path="globalParameters['mftPostBackUrlHeader']"
													id="mftPostBackUrlHeader" name="mftPostBackUrlHeader"
													onkeyup="return isAlphaNumericWithSpl(this)"
													autocomplete="off" maxlength="100" class="textbox-large"
													onblur="validateFields(this.form.id,this.id);" />
												<form:errors path="globalParameters['mftPostBackUrlHeader']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<!-- MFT Encryption Enable -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Encryption Enable"> <spring:message
														code="globalParameters.mftEncryptionEnable" /></label>
											</div>
											<div class="col-lg-8">
												<div class="col-lg-4">
													<form:radiobutton
														path="globalParameters['mftEncryptionEnable']" value="Yes"
														id="mftEncryptionEnable" name="mftEncryptionEnable"
														data-skin="square" data-color="blue" />
													<label class='radiobox-line'>Yes</label>
												</div>
												<div class="col-lg-4">
													<form:radiobutton
														path="globalParameters['mftEncryptionEnable']" value="No"
														id="mftEncryptionDisable" name="mftEncryptionDisable"
														data-skin="square" data-color="blue" />
													<label class='radiobox-line'>No</label>
												</div>
												<form:errors path="globalParameters['mftEncryptionEnable']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<!-- MFT Archive Enable -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Archive Enable"> <spring:message
														code="globalParameters.mftArchiveEnable" /></label>
											</div>
											<div class="col-lg-8">
												<div class="col-lg-4">
													<form:radiobutton
														path="globalParameters['mftArchiveEnable']" value="Yes"
														id="mftArchiveEnable" name="mftArchiveEnable"
														data-skin="square" data-color="blue" />
													<label class='radiobox-line'>Yes</label>
												</div>
												<div class="col-lg-4">
													<form:radiobutton
														path="globalParameters['mftArchiveEnable']" value="No"
														id="mftArchiveDisable" name="mftArchiveDisable"
														data-skin="square" data-color="blue" />
													<label class='radiobox-line'>No</label>
												</div>
												<form:errors path="globalParameters['mftArchiveEnable']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<!-- MFT Retry Count -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Retry Count"> <spring:message
														code="globalParameters.mftRetryCount" /></label>
											</div>
											<div class="col-lg-8">
												<form:input path="globalParameters['mftRetryCount']"
													id="mftRetryCount" name="mftRetryCount"
													onkeyup="return isNumeric(this)" autocomplete="off"
													maxlength="100" class="textbox-large"
													onblur="validateFields(this.form.id,this.id);" />
												<form:errors path="globalParameters['mftRetryCount']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<!-- MFT Channel Identifier -->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="MFT Channel Identifier"> <spring:message
														code="globalParameters.mftChannelIdentifier" /></label>
											</div>
											<div class="col-lg-8">
												<form:input path="globalParameters['mftChannelIdentifier']"
													id="mftChannelIdentifier" name="mftChannelIdentifier"
													onkeyup="return isAlphaNumericWithSpl(this)"
													autocomplete="off" maxlength="100" class="textbox-large"
													onblur="validateFields(this.form.id,this.id);" />
												<form:errors path="globalParameters['mftChannelIdentifier']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>
										
												<!-- Top up Preempt number of days-->
										<div class="col-lg-12">
											<div class="col-lg-4">
												<label for="Top up Preempt number of days"> <spring:message
														code="globalParameters.topupPreemptNumberOfDays" /></label>
											</div>
											<div class="col-lg-8">
												<form:input path="globalParameters['topupPreemptNumberOfDays']"
													id="topupPreemptNumberOfDays" name="topupPreemptNumberOfDays"
												    onkeyup="return validateTopupPreemptNumberOfDays(this.form.id,this)"
													
													autocomplete="off" maxlength="3" class="textbox-large"/>
												<form:errors path="globalParameters['topupPreemptNumberOfDays']"
													cssClass="error" cssStyle="color:red"></form:errors>
											</div>
										</div>

										<input type="hidden" id="addurl" name="addurl"
											value="${addUrl}" /> &nbsp;

										<!-- space starts here -->
										<%-- <div class="col-lg-12">
<center>
<TABLE Border=1 class=''>

	<TR>
		<TD colspan=5 class='TR3'>
		<center>Set Date Format
		</center>
		</td>
	</tr>
	<tr>

		<TD><select name="sel1" id="sel1">
			<option value='dd'>dd</option>
			<option value='mm'>mm</option>
			<option value='mon'>mon</option>
			<option value='yy'>yy</option>
			<option value='yyyy'>yyyy</option>
		</select></td>
		<TD><select name="seldel1" id="seldel1" onChange='changeDel2();'>
			<option value='/'>/</option>
			<option value='-'>-</option>
		</select></td>
		<TD><select name="sel2" id="sel2">
			<option value='mm'>mm</option>
			<option value='dd'>dd</option>
			<option value='mon'>mon</option>
			<option value='yy'>yy</option>
			<option value='yyyy'>yyyy</option>
		</select></td>
		<TD><select name="seldel2" id="seldel2" onChange='changeDel1();'>
			<option value='/'>/</option>
			<option value='-'>-</option>
		</select></td>
		<TD><select name="sel3" id="sel3">
			<option value='yyyy'>yyyy</option>
			<option value='mm'>mm</option>
			<option value='mon'>mon</option>
			<option value='yy'>yy</option>
			<option value='dd'>dd</option>
		</select></td>
	</tr>


	<tr>
		<td colspan=5>
		<center><INPUT type="button" name="Button" id="Button"
			value="Submit" onClick="savedata();" class=submit
			Onmouseover='SubmitOver(this)' Onmouseout='SubmitOut(this)'>
		<INPUT TYPE="hidden" name="hMode" id="hMode" value="A"></center>
		</td>
	</tr>
</table>


</center>
</div>
 --%>










										<!-- extra space ends here -->
										<!-- ends date format -->

										<!-- buttons -->
										<br>&nbsp;



										<!--  ends buttons -->
									</div>
									<!-- <div> -->
									<div class="col-lg-12 text-center">

										<!-- 	<div class="col-lg-4 space"></div> -->
										<div class="col-lg-12">
											<button type="submit" id="globalParametersSubmit"
												class="btn btn-primary"
												onclick="goSaveGlobalParameters(this.form.id,event);">
												<i class="glyphicon glyphicon-saved"></i>
												<spring:message code="globalParameters.submit" />
											</button>

											<button type="button" onclick="resetGlobalParameters();"
												id="globalParametersReset" class="btn btn-primary gray-btn">
												<i class='glyphicon glyphicon-refresh'></i>
												<spring:message code="button.reset" />
											</button>


										</div>
									</div>

									<!-- </div> -->

								</div>
								<!-- form-inline -->
							</div>


						</div>



					</article>
				</section>

			</div>
		</div>


	</form:form>


</body>
</html>



<script>
	
</script>