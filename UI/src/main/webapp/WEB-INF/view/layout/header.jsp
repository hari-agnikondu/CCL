<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.i18n.properties-min-1.0.9.js"></script>

<div class="row-one">

	<div class="container">
		<div class="col-xs-3 small-logo">
			<img width="123" height="45" src="<c:url value="/resources/images/Incomm.png" />" >
		</div>

		<div class="col-lg-5 CMS-Header-Text">Consolidated Closed Loop Host Configuration</div>

		<nav class="navbar navbar-default topmenu" role="navigation">
			<div class="container-fluid">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
				</div>
				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					<!-- <div class="col-lg-13">-->

					<div class="collapse navbar-collapse"
						id="bs-example-navbar-collapse-1">

						<ul class="nav navbar-nav" data-smartmenus-id="15174640328187446">
							<li class="home"><a href="<%=request.getContextPath()%>/" class=""><span class="show"></span><span>Home</span></a></li>

<!-- 							<li class="profile"><a href="#" class=""><span
									class="show"></span><span>Profile</span></a></li>
 -->							<li class="logout"><a href="<%=request.getContextPath()%>/logout" ><span
									class="show"></span><span>Logout</span></a></li>
						</ul>

					</div>

				</div>
				<!-- /.navbar-collapse -->
			</div>
			<!-- /.container-fluid -->
		</nav>
	</div>
</div>
<div class="container">

	<nav class="navbar navbar-default topmenu" role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->

			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<label-custom>Welcome:
				<security:authentication property="principal.username" /> (Last Login: <c:out value="${sessionScope['scopedTarget.clpSession'].userLastLoginTime}"/>)</label-custom>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
</div>

<script type="text/javascript">
function getSystemLocale() {
    var systemLocale ='<%=RequestContextUtils.getLocale(request)%>';
   
    return systemLocale;
}
function readMessage(key){
	var val="";
	 jQuery.i18n.properties({
		    name:'message', 
		    path:'${pageContext.request.contextPath}/resources/JS_Messages/', // changed here 
		    mode:'both',
		    cache:true,
		    language:getSystemLocale(),
		    callback: function() {
		    	 val=jQuery.i18n.prop(key);
		  
		   
		}});
	 return val;
}
function readValidationProp(key){
	var val="";
	 jQuery.i18n.properties({
		    name:'clientValidation', 
		    path:'${pageContext.request.contextPath}/resources/JS_Messages/', // changed here 
		    mode:'both',
		    cache:true,
		    callback: function() {
		    	 val=jQuery.i18n.prop(key);
		  
		   
		}});
	 return val;
}
		</script>

