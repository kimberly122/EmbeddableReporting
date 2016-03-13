<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Embeddable Reporting</title>
    </head>
    <body>
        <h3>Add Data to Cloudant</h3>
		<form action="CloudantUpload" method="POST" enctype="multipart/form-data">
            <input type="file" name="file" /><br>
       
            <input type="submit" class="btn" value="Upload" />
		</form>
		</br>
		<% if (request.getAttribute("msg") != null) { %>
       	<div><%= request.getAttribute("msg") %></div>
    <% } %> 
		<h3> View Reports</h3>
    </body>
</html>