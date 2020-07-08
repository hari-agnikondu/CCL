<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<%@ page language="java"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta charset="utf-8"/>

 	

</head>
<body class="inner">
    <div class="wrapper">
    	
    	
        <div class="body-container">
        
            <div class="container">
                <section class="content-container">
                    <div class="cols4">
                        <tiles:insertAttribute name="body" />
                    </div>    
                </section>
            </div>
            
        </div>
       
    </div>
   
  </body>

<!-- JSP -->


</html>
