<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<%@ page language="java"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
     <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>    
    <title>CCLP-VMS</title>
 	
  


	
	 <!--[if IE 8]>
      <script src="js/html5.js"></script>
      <script src="js/respond.min.js"></script>
    <![endif]-->
    
    <!--[if IE 7]>
      <script src="js/respond.min.js"></script>
    <![endif]-->
    <script src="<c:url value="/resources/js/jquery.min.js" />"></script>
     <link href="<c:url value="/resources/css/bootstrap.css" />" rel="stylesheet">
    
    <script src="<c:url value="/resources/js/jquery-ui-1.8.16.custom.min.js" />"></script>
     <script src="<c:url value="/resources/js/jquery.mousewheel.js" />"></script>
     
     <script src="<c:url value="/resources/js/bootstrap.js" />"></script>
       <script src="<c:url value="/resources/js/jquery.easing.1.3.js" />"></script>
       <script src="<c:url value="/resources/js/jquery.mCustomScrollbar.js" />"></script>
   
    <!-- flexslider --> 
     <link href="<c:url value="/resources/css/flexslider.css" />" rel="stylesheet" media="screen"/>
       <script src="<c:url value="/resources/js/jquery.flexslider.js" />"></script>
    
     <link href="<c:url value="/resources/css/jquery.mCustomScrollbar.css" />" rel="stylesheet" />
    
    <!-- Bootstrap Addon CSS -->
     <link href="<c:url value="/resources/css/jquery.smartmenus.bootstrap.css" />" rel="stylesheet" />
      <script src="<c:url value="/resources/js/jquery.smartmenus.js" />"></script>
     <script src="<c:url value="/resources/js/jquery.smartmenus.bootstrap.js" />"></script>
     
     <script src="<c:url value="/resources/js/bootstrap-tabcollapse.js" />"></script>
     
    
    <!-- Combo, Check box and Radio button -->
     <link href="<c:url value="/resources/css/combobox.css" />" rel="stylesheet" />
     <link href="<c:url value="/resources/css/checkboxradio.css" />" rel="stylesheet" />
    <script src="<c:url value="/resources/js/combobox.js" />"></script>
    <script src="<c:url value="/resources/js/checkboxradio.js" />"></script>
    
    <!-- Date and time picker -->
     <script src="<c:url value="/resources/js/datetimepicker.js" />"></script>
    <!-- Color Picker --->
     <link href="<c:url value="/resources/css/colorpicker.css" />" rel="stylesheet" />
    <script src="<c:url value="/resources/js/colorpicker.js" />"></script>
    <!-- Range Slider -->
     <link href="<c:url value="/resources/css/range-slider.css" />" rel="stylesheet" />
       <script src="<c:url value="/resources/js/RangeSliders-min.js" />"></script>
    <!-- Data Grid -->
      <link href="<c:url value="/resources/css/jquery.dataTables.min.css" />" rel="stylesheet" />
          <link href="<c:url value="/resources/css/dataTables.responsive.css" />" rel="stylesheet" />
            <script src="<c:url value="/resources/js/dataTables.bootstrap.js" />"></script>
                <script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
                <script src="<c:url value="/resources/js/dataTables.colReorder.js" />"></script>
                <script src="<c:url value="/resources/js/dataTables.colResize.js" />"></script>
                 <script src="<c:url value="/resources/js/dataTables.responsive.min.js" />"></script>
    
        <script src="<c:url value="/resources/js/bootstrap3-typeahead.js" />"></script>
    
    <!-- default CSS -->
        <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet" />
    <!-- Theme CSS -->
       <link href="<c:url value="/resources/css/blue-theme.css" />" rel="stylesheet" />
    
    <!--[if IE 7]>
         <link href="<c:url value="/resources/css/bootstrap-ie7.css" />" rel="stylesheet" />
            <link href="<c:url value="/resources/css/ie7.css" />" rel="stylesheet" />
    <![endif]-->
    
    <!-- default JS -->
        <script src="<c:url value="/resources/js/action.js" />"></script>
    
    <!-- Responsive --> 
           <link href="<c:url value="/resources/css/responsive.css" />" rel="stylesheet"  type="text/css" media="all"/>
           
           <!-- Custom imports starts -->
           <script src="<c:url value="/resources/js/clpvms/common.js" />"></script>
           <!-- Custom imports ends -->
           
           <!-- i18n Message validations starts -->
           <script src="<c:url value="/resources/js/jquery.i18n.properties-min-1.0.9.js" />"></script>
           <!-- i18n Message validations ends -->

</head>
<body class="inner">
    <div class="wrapper">
    	
    	<header>
    		<tiles:insertAttribute name="header" />
    		<tiles:insertAttribute name="menu" />
        </header>
        <div class="body-container">
        
            <div class="container">
                <section class="content-container">
                    <div class="cols4">
                    	<%-- <tiles:insertAttribute name="prodHeader" /> --%>
    					<tiles:insertAttribute name="viewProdMenu" />
                        <tiles:insertAttribute name="body" />
                    </div>    
                </section>
            </div>
            
        </div>
        <footer class="CMS-Footer-Text">	
        	<div >
            	<div class="container">
            		<tiles:insertAttribute name="footer" />
                </div>    
            </div>
        </footer>
    </div>
     <!-- Modal -->
    <div class="modal modal-wide fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
               
            </div>
        </div>
    </div>
  </body>

<!-- JSP -->


</html>
