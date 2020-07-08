<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<div class="navbar navbar-inverse mainmenu" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".mm-resp">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse mm-resp">
			<ul class="nav navbar-nav">
			
			<security:authorize access="hasRole('MENU_SETUP')">
				<li><a class="active">Setup</a>
					
					<ul class="dropdown-menu">
					<security:authorize access="hasRole('EDIT_GLOBALPARAMETERS')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/globalParameters/globalParametersConfig">Global Parameters</a>
					</li>	
					</security:authorize>
					
					 <security:authorize access="hasRole('EDIT_SCHEDULER')"> 
					<li><a class="active hoverColor" 
						href="<%=request.getContextPath()%>/jobScheduler/jobSchedulerConfig">Schedule Job</a>
					</li>
					 </security:authorize>
					<!-- below two submodules Commented for R05 -->
					<%-- <security:authorize access="hasRole('EDIT_SCHEDULER')">
					<li><a class="active hoverColor" 
						href="<%=request.getContextPath()%>/jobScheduler/jobSchedulerServiceJobs">Scheduler Service</a>
					</li>
					</security:authorize> --%>
					
					<%-- <security:authorize access="hasRole('EDIT_SCHEDULER')">
					<li><a class="active hoverColor" 
						href="<%=request.getContextPath()%>/jobScheduler/switchOverSchedulerConfig">Switch Over Scheduler</a>
					</li>
					</security:authorize> --%>
					<security:authorize access="hasRole('SEARCH_GROUPACCESS_PRODUCT')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/groupAccessId/groupAccessIdConfig">Group Access ID</a>
					</li>	
					</security:authorize>
					<security:authorize access="hasRole('EDIT_PAN_EXPIRY_EXEMPTION')">
					<li><a class="active hoverColor"  
					href="<%=request.getContextPath()%>/config/panExpiryExemption/panExpiryDateConfig">PAN Expiry Date Exemption</a>
					</li>	
					</security:authorize>
								
							
					</ul>
					
					
					
					</li>
					</security:authorize>

				<security:authorize access="hasRole('MENU_PRODUCT')">
					<li><a class="active">Product</a>
					<ul class="dropdown-menu">
					<security:authorize access="hasRole('SEARCH_ISSUER')">
						<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/issuer/issuerConfig">Issuer</a>
					</li>
					</security:authorize>
					<security:authorize access="hasRole('SEARCH_CARDRANGE')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/cardrange/cardRangeConfig">Card Range</a>
					</li>
					</security:authorize>
					
					<security:authorize access="hasRole('SEARCH_PURSE')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/purse/purseConfig">Purse</a>
					</li>
					</security:authorize>
					
					<security:authorize access="hasRole('SEARCH_PARTNER')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/partner/partnerConfig">Partner</a>
					</li>
					</security:authorize>
					
					<security:authorize access="hasRole('MENU_PROGRAMID')">	
							<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/programId/programIdConfig">Program ID</a>
					</li>
					</security:authorize>
					
					<security:authorize access="hasRole('SEARCH_FULFILLMENT_VENDOR')">	
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/fulfillment/fulFillmentConfig">Fulfillment Vendor</a>
					</li>	
					</security:authorize>
					
					<security:authorize access="hasRole('SEARCH_PACKAGEID')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/package/packageIDConfig">Package ID</a>
					</li>
					</security:authorize>
					<security:authorize access="hasRole('EDIT_CCF_CONFIG')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/ccf/ccfConfig">CCF Config</a>
					</li>						
					</security:authorize>
							<security:authorize access="hasRole('SEARCH_PRODUCT')">
								<li><a class="active hoverColor"
									href="<%=request.getContextPath()%>/config/product/productConfig">Product</a>
								</li>
							</security:authorize>
					<security:authorize access="hasRole('EDIT_TRANS_FLEX_DESCRIPTION')">		
					 <li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/TransactionFlex/TransactionFlexConfig">Transaction Flex Description</a>
					</li>	
					</security:authorize>
						<li>
						<a class="active hoverColor"  href="<%=request.getContextPath()%>/cardNumber/cardNumberInventory">Generate Card Number Inventory</a>
					</li>
					
						<li>
						<a class="active hoverColor"  href="<%=request.getContextPath()%>/customerProfile/customerProfileConfig">Customer Profile</a>
					</li>
										
						
					</ul>
					</li>
					</security:authorize>
					
					<security:authorize access="hasRole('MENU_FRAUD')">
					<li><a class="active">Fraud</a>
					<ul class="dropdown-menu">
					<security:authorize access="hasRole('SEARCH_BLOCKLIST')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/fraud/blockList">Block List</a>
					</li>
					</security:authorize>
					
					<security:authorize access="hasRole('EDIT_PRM_ENABLE_DISABLE')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/fraud/prmConfig">Enable/Disable PRM</a>
					</li>
					</security:authorize>	
					
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/fraud/redemptionDelayConfig">Redemption Delay Configuration</a>
					</li>
					</ul></li>
					</security:authorize>
				
				<security:authorize access="hasRole('MENU_RULE')">
					<li><a class="active">Rule Filter</a>
						<ul class="dropdown-menu">
						<security:authorize access="hasRole('SEARCH_RULE')">
							<li><a class="active hoverColor" 
							href="<%=request.getContextPath()%>/config/rule/ruleConfig">Transaction Filter</a>
							</li>
							<li><a class="active hoverColor" 
							href="<%=request.getContextPath()%>/config/ruleSet/ruleSetConfig">RuleSet</a>
							</li>							
						</security:authorize>
						</ul>				
					</li>
				</security:authorize>
				
				<security:authorize access="hasRole('MENU_MERCHANT')">
				<li><a class="active">Merchant</a>
					<ul class="dropdown-menu">
							<security:authorize access="hasRole('SEARCH_MERCHANT')">
								<li><a class="active hoverColor"
									href="<%=request.getContextPath()%>/config/merchant/merchantConfig">Merchant</a>
								</li>
							</security:authorize>
							<security:authorize
								access="hasRole('SEARCH_MERCHANT_PRODUCT_LINK')">
								<li><a class="active hoverColor"
									href="<%=request.getContextPath()%>/config/merchant/assignMerchantConfig">Assign
										Product To Merchant</a></li>
							</security:authorize>
					<security:authorize
								access="hasRole('SEARCH_LOCATION')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/location/locationConfig">Location</a>
					</li>	
					</security:authorize>
				
					<security:authorize access="hasRole('SEARCH_STOCK')"> 
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/stocks/stockConfig">Stock</a>
					</li>
					 </security:authorize>
					
					</ul></li>
					</security:authorize>

					<security:authorize access="hasRole('MENU_ADMINISTRATION')">

					<li><a class="active">Administration</a>
					<ul class="dropdown-menu">
					
					<security:authorize access="hasRole('SEARCH_ROLE')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/admin/roleConfig">Role</a>
					</li>
					</security:authorize>		
					
					<security:authorize access="hasRole('SEARCH_GROUP')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/admin/group/groupConfig">Group</a>
					</li>
					</security:authorize>
					
					<security:authorize access="hasRole('SEARCH_USER')">
					<li><a class="active hoverColor" 
					href="<%=request.getContextPath()%>/config/user/userConfig">User</a>
					</li>
					</security:authorize>		
					
					
					</ul></li> 
					</security:authorize>
					 
					<security:authorize access="hasRole('MENU_CARDORDER')">
					 <li><a class="active">Order</a>
						<ul class="dropdown-menu">
							<security:authorize access="hasRole('ADD_ORDER')">
							<li><a class="active hoverColor"  href="<%=request.getContextPath()%>/order/placeOrder">Place Order</a></li>
							</security:authorize>
							<security:authorize access="hasRole('APPROVE_ORDER')">
							<li><a class="active hoverColor"  href="<%=request.getContextPath()%>/order/showApproveOrder">Approve Order</a></li>
							</security:authorize>
							<security:authorize access="hasRole('SEARCH_ORDER')">
							<li><a class="active hoverColor"  href="<%=request.getContextPath()%>/order/showOrderAndCCFGeneration">Order Processing/CCF Generation</a></li>
							</security:authorize>
							<security:authorize access="hasRole('SEARCH_ORDER')">
							<li><a class="active hoverColor"  href="<%=request.getContextPath()%>/order/showCNFileUpload">CN File Upload</a></li>
							</security:authorize>
							<security:authorize access="hasRole('SEARCH_ORDER')">
							<li><a class="active hoverColor"  href="<%=request.getContextPath()%>/order/showOrderStatus">Check Order Status</a></li>
							</security:authorize>
							<security:authorize access="hasRole('SEARCH_ORDER')">
							<li><a class="active hoverColor"  href="<%=request.getContextPath()%>/order/showCNFileStatus">Check CN File Status</a></li>
							</security:authorize>
						</ul>
					</li> 
				   </security:authorize>
					
			</ul>
		</div>
	</div>
</div>