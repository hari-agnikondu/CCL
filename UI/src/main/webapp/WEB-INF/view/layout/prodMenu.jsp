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
                    	
  			<li class="active"><a href="showEditProduct" class="hoverColor" >Product</a></li>
            <li id="generalTab"><a href="generalTab"  class="hoverColor" target="_self">General</a></li>
     		<c:if test="${data.PINTab != null && data.PINTab == 'Enable'}">
     		<li id="showPINtab"><a href="showPINtab"  class="hoverColor">PIN</a></li>
     		</c:if>
     		<c:if test="${data.CVVTab != null && data.CVVTab == 'Enable'}">
     		<li id="cvvTab"><a href="showCVVtab" class="hoverColor" target="_self">CVV</a></li>
     		</c:if>
     		
     		
     		<c:if test="${data.PurseTab != null && data.PurseTab == 'Enable'}">
     		<li id="purseTab"><a href="productPurse"   class="hoverColor" target="_self">Purse</a></li>
     		</c:if>
     		
     		
     		<c:if test="${data.LimitsTab != null && data.LimitsTab == 'Enable'}">
     		
     		<security:authorize access="hasRole('SEARCH_ROLE')">
     		<li id="limitsTab"><a href="productLimit"   class="hoverColor" target="_self">Limits</a></li>
     		</security:authorize>	
     		</c:if>
     		
     		<c:if test="${data.TxnFeesTab != null && data.TxnFeesTab == 'Enable'}">
     		<security:authorize access="hasRole('SEARCH_ROLE')">
     		<li id="txnFeesTab"><a href="productTransactionFee"  class="hoverColor">Transaction Fees</a></li>
     		</security:authorize>	
     		</c:if>
     		
     		<c:if test="${data.AlertsTab !=null && data.AlertsTab == 'Enable'}">
     		<security:authorize access="hasRole('EDIT_PRODUCT')">
     		<li id="alertsTab"><a href="showProductAlerts" class="hoverColor" target="_self">Alerts</a></li>   
     		</security:authorize>
     		</c:if>
     		<c:if test="${data.TxnBasedonCardStatTab != null && data.TxnBasedonCardStatTab == 'Enable'}">
     		<li id="txnCardStatTab"><a href="txnCardStatus"  class="hoverColor" target="_self"> Txn based on Card Status</a></li>
     		</c:if> 
     		<c:if test="${data.MaintenanceFeesTab != null && data.MaintenanceFeesTab == 'Enable'}">
     		<%-- <security:authorize access="hasRole('SEARCH_ROLE')"> --%>
     		 <li id="maintenanceFeeCapTab"><a href="productMaintenanceFee"  class="hoverColor">Maintenance Fees</a></li>
     		 <%-- </security:authorize> --%>	
     		</c:if>
     		<%-- <security:authorize access="hasRole('SEARCH_ROLE')"> --%>
     		<c:if test="${(data.MaintenanceFeesTab != null && data.MaintenanceFeesTab == 'Enable') || (data.TxnFeesTab != null && data.TxnFeesTab == 'Enable')}">
     		 <li id="monthlyFeeCapTab"><a href="productMonthlyFeeCap"  class="hoverColor">Monthly Fee Cap</a></li>				
     		<%-- </security:authorize> --%>				
     		</c:if>			
						
                         </ul>
                  
                             <script>
                             
                             </script>