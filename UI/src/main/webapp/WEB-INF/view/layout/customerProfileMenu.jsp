<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<c:set var="data" value="${sessionScope.headerData}"/>
 <div class="wrapper">
    <div class="body-container">
        	
            <div class="container">    
                <section class="content-container">

<article class="col-xs-12">
					
                    	 <p>
                    	  <ul class="nav nav-tabs" style="border-bottom:0px;">
                    	
  			<li class="active"><a href="showEditCustomerProfile" class="hoverColor" >Customer Profile</a></li>
     	
     		<c:if test="${data.LimitsTabCard != null && data.LimitsTabCard == 'Enable'}">
     		<security:authorize access="hasRole('SEARCH_ROLE')">
     		<li id="limitsTab"><a href="customerProfileLimit"   class="hoverColor" target="_self">Limits</a></li>
     		</security:authorize>	
     		</c:if>
     		
     		<c:if test="${data.TxnFeesTabCard != null && data.TxnFeesTabCard == 'Enable'}">
     		<security:authorize access="hasRole('SEARCH_ROLE')">
     		<li id="txnFeesTab"><a href="customerProfileTransactionFee"  class="hoverColor">Transaction Fees</a></li>
     		</security:authorize>	
     		</c:if>
     		
     		
     	
     		<c:if test="${data.MaintenanceFeesTabCard != null && data.MaintenanceFeesTabCard == 'Enable'}">
     		<%-- <security:authorize access="hasRole('SEARCH_ROLE')"> --%>
     		 <li id="maintenanceFeeCapTab"><a href="customerProfileMaintenanceFee"  class="hoverColor">Maintenance Fees</a></li>
     		 <%-- </security:authorize> --%>	
     		</c:if>
     		<%-- <security:authorize access="hasRole('SEARCH_ROLE')"> --%>
     		 <li id="monthlyFeeCapTab"><a href="customerProfileMonthlyFeeCap"  class="hoverColor">Monthly Fee Cap</a></li>				
     		<%-- </security:authorize> --%>				
     					
						
                         </ul>
                  
                             <script>
                             
                             </script>