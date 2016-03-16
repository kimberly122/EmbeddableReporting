

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Embeddable Reporting</title>
    </head>
   
    
        <h3>Add Data to Cloudant</h3>
		<form action="CloudantUpload" method="POST" enctype="multipart/form-data">
            <input type="submit" class="btn" value="Upload" />
		</form>
		</br>
		<h3> View Reports</h3>
		<!--<form action="SampleServlet" method="POST" enctype="multipart/form-data">
			<input type="submit" value="view created report"/>
		</form>-->
		<form action="SampleServlet" method="GET" enctype="multipart/form-data">
                    <input type="text" name="definitions">
            <input type="submit" class="btn" value="View report" />
		</form>
                
                <% if (request.getAttribute("postConnection") != null) { %>
                    <a href=<%= request.getAttribute("postConnection") %>>access report</a>
	<% } %> 
        
		<% if (request.getAttribute("postConnection") != null) { %>
       	<div><%= request.getAttribute("postConnection") %></div>
    <% } %> 
    <!--<div><iframe src="https://erservice-impl.ng.bluemix.net/ers/v1/definitions/1b5ed4dccc65e78bc6510cf95555e27c/reports/phtml"></div>
    -->
    <!--<a href="https://erservice-impl.ng.bluemix.net/ers/v1/definitions/1b5ed4dccc65e78bc6510cf95555e27c/reports/phtml" type="application/vnd.ibm.ba.phtml+html"></a>
    -->
    
    
</html>
