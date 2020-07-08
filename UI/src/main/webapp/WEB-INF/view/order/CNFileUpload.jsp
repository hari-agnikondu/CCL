 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<script
	src="${pageContext.request.contextPath}/resources/js/clpvms/common.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/clpvms/order.js"></script>

<body>
	<div class="body-container" style="min-height: 131px;">     
	<div class="container">
	<div id="feedBackTd" class="text-center" style="padding-top:4px"> 
			<c:if test="${statusFlag=='success' }">
			<p class="successMsg" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
			<c:if test="${statusFlag!='success' }">
			<p class="fieldError" style="text-align:center;font-weight: bold;"><c:out value="${statusMessage }"/></p></c:if>
	</div>
	<div>
	</div>
	<div class="group">
	<form:form action="#" method="POST" id="cnFileUploadForm" name="cnFileUploadForm">
				<%-- 							<table  style="width: 30%; border-width: 1px; border-spacing: 1px; "
						id="tableViewCNFileUpload"
						class="table table-hover table-striped table-bordered dataTable">
						<tbody>

							 <tr> <input type="hidden" id="GenerationType" name="Generationtype" value="${GenerationType}" />
								<td align="center"><button type="button" onclick="Action(this.form.id,'GENERATE')"
										class="btn btn-primary-table-button"
										name = "cnFileList" id = "cnFileList"
										style="position: relative;">
										<i class='glyphicon glyphicon-ok-sign'></i>
										<spring:message code="order.generate" text="Generate" />
									</button>&nbsp;&nbsp;
									 <button type="button" onclick="Action(this.form.id,'CNFILELIST')" class="btn btn-primary-table-button"
									name = "CCFGeneration" id = "CCFGeneration">
										<i class='glyphicon glyphicon-ok-sign'></i>
										<spring:message code="order.CNFileList" text="CN File List" />
									</button> </td>

							</tr> 
						</tbody>
						
					</table> --%>
		<div id="TableData">				
		<div id="tableDiv">
			<div id="tableErr" class="fieldError" style="text-align:center;"></div>
					<table id="tableViewOrder"
				class="table table-hover table-striped table-bordered dataTable datagridwithsearch"
				style="width: 100% !important;">
					
					<thead>
						<tr>
							<th style="width:5em;"><spring:message code="button.action"
									text="Action" /></th>
							<th style="width:11em;"><spring:message code="order.CNFileList"
									text="CN File List" /></th>
							
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${cnFileList}" var="cnFile" >
						<tr id="${cnFile}">
                        				
						<td ><input type="checkbox" name =selectedItems id="${cnFile}" value="${cnFile}"  /></td>
						<td>${cnFile}</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
		</div>
		<div class="col-lg-12">
				<div class="col-lg-5 col-lg-offset-5" style="padding-bottom:10px">
				
					<security:authorize access="hasRole('SEARCH_ORDER')">
					<button type="button" class="btn btn-primary-table-button green-btn" data-toggle="modal" data-target="#" id="GenerateBtn"
										onclick="uploadFiles(this.form.id)">
										<i class="glyphicon glyphicon-ok-sign"></i>
										<spring:message code="button.uploadBtn" text="Upload" />
					</button>
					</security:authorize>

				</div>
			</div>
			</div>

			
			</form:form>
	</div>
 </div> 
 </div>
</body>

<script>
$('input[name="selectedItems"]').on('change', function() {
	if($("input[name=selectedItems]:checked").length > 0){
		$("#tableErr").html('');
	}
	
});


function uploadFiles(formId){
/* $("#GenerationType").val(key);
	if(key == "CNFILELIST"){
		$("#"+formId).attr('action','cnFileList');
	}else
	 */
	 $("#feedBackTd").html('');
	 $("#tableErr").html(''); 
	 
	if($("input[name=selectedItems]:checked").length < 1){
		 $("#tableErr").html(readMessage("order.cnFileUpload.selectFor"));
	}
	else
	{
		$("#"+formId).attr('action','cnFileUpload');
		$("#"+formId).submit();
	}
	
	
}

$(document).ready(function(){
	 //code for submitting all page data
	   var table = $('#tableViewOrder').DataTable();
	    // Handle form submission event
	    $('#cnFileUploadForm').on('submit', function(e){
	       var form = this;
     	 // Encode a set of form elements from all pages as an array of names and values
	       var params = table.$('input').serializeArray();
	       var params1 = $('input').serializeArray();
	     // Iterate over all form elements
	       $.each(params, function(){
	          
	        	    $(form).append(
	                $('<input>')
	                   .attr('type', 'hidden')
	                   .attr('name', this.name)
	                   .attr('class', 'test')
	                   .val(this.value)
	                );
	            
	       }); 
	    
	       $.each(params1, function(){
	    	    var value=this.value;
	        $('.test').each(function(){
	  		 
		      if($(this).val()==value){
            //current page data is removed bcoz it is aready present in form cache
		       $(this).remove();
		      }
      	 });
	     
	       }); 
	    });   //form submit
	   
	});//end of doc

</script>


