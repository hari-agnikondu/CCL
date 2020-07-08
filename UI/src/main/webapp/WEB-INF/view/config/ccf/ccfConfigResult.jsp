<!doctype html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>

<div class="container">

	<div class="row-fluid">

		<div class="form-group text-center" id="error"></div>

		<table style="text-align: center width: 90%;">
			<tr>
				<td id="selectionError"></td>
			</tr>
		</table>
		<div class="aln">
			<div>
				<center>
					<h6>
						<font class="errormsg"><b>${statusMessage}</b></font>
					</h6>
				</center>
			</div>
			<div>
				<center>
					<h6>
						<font style="color: green;"><b>${status}</b></font>
					</h6>
				</center>
			</div>
		</div>
		<span id='hidemydata'> <c:if test="${successstatus !=''}">
				<h4>
					<p class="">
						<b><font size="3"><center style="color: green;">
									<strong>${successstatus}</strong>
								</center></font></b>
					</p>
				</h4>
			</c:if> <c:if test="${failstatus !=''}">
				<h4>
					<p class="">
						<b><font size="3"><center style="color: red;">
									<strong>${failstatus}</strong>
								</center></font></b>
					</p>
				</h4>
			</c:if>
		</span>
	</div>
</div>
