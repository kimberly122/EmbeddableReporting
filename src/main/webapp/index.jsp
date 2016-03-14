<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Embeddable Reporting</title>
    </head>
	<script type="text/javascript">
	var urlRoot = "/ers/v1/";
	function getXmlHttp() {
                if (window.XMLHttpRequest) {
                    return new XMLHttpRequest();
                } else {
                    return new ActiveXObject("Microsoft.XMLHTTP");
                }
            }
			
			var appURL = "";
		    var appName = "";
		    var xRunLocation = '';
		
		    function changeApp() {
		    	var e = document.getElementById("appList");
		        appURL = e.options[e.selectedIndex].value;
		        appName = e.options[e.selectedIndex].text;
		        document.getElementById("banner").innerHTML = "<div style='width:100%'>" + appName + "</div>";
		        getReportList();
		    }
		    
			/**
			 * retrieve the list of applications.
			 */
			function getApplicationList() {
				var app = getXmlHttp();
				app.open("GET", urlRoot + "packages/", false);
				app.onreadystatechange = function() {
					if (app.readyState == 4 && app.status == 200) {
					    var txt = "<select id='appList' onchange='changeApp()'>";
						var apps = JSON.parse(app.responseText);
						apps.sort(function(a,b) { return a["name"].toLowerCase() > b["name"].toLowerCase() });
						for (var i = 0; i < apps.length; i++) {
						    var appObject = apps[i];
						    txt += "<option value=\"" + appObject["url"] + "\"";
						    if (i == 0) {
						    	txt += " selected";
						    } 
						    txt += ">" + appObject["name"] + "</option>";
						}
						txt += "</select>";
						document.getElementById('apps').innerHTML = txt;
					}
				}
				app.send();
			}
			
			/**
			 * retrieve the list of reports in the application
			 */
		    function getReportList() {
		        var reportList = getXmlHttp();
		        document.getElementById('reportList').innerHTML = '';
		        reportList.open("GET", appURL + "/definitions", true);
		        reportList.onreadystatechange = function() {
		            if (reportList.readyState == 4 && reportList.status == 200) {
		                var txt = "<table style='width:100%'><tr><th>Available Reports<hr/></th><th/></tr>";
		                var reports = JSON.parse(reportList.responseText);
		                reports.sort(function(a,b) { return a["name"].toLowerCase() > b["name"].toLowerCase() });
		                for ( var i = 0; i < reports.length; i++) {
		                    var report = reports[i];
		                    txt += "<tr><td><a style='color: white' href='#' onclick='getReport(\"" + report['id'] + "\", \"" + report['name'] + "\", \"" + report['url'] + "\", \"" + report['type'] + "\")'>";
		                    txt += report['name'];
		                    txt += "</a></td></tr>";
		                }
		                document.getElementById('reportList').innerHTML = txt;
		            }
		        }
		        reportList.send();
	    		document.getElementById('reportOutput').innerHTML = '';
		    }
		
			/**
			 * Run the selected report. use the "phtml" format so that the report is returned as an html fragment that
			 * can be inserted directly into this page.
			 */
		    function getReport(reportID, reportName, url, type) {
                var myDiv = document.getElementById('reportOutput');
                //myDiv.innerHTML = "<div style='width:100%; text-align:center'></div>"
		    	if (xRunLocation != '') {
		    		deleteRunInstance();
	                xRunLocation = '';
		    	}
                if (type.indexOf("activereport") == -1) {
    		        var report = getXmlHttp();
    		        report.open("GET", url + "/reports/phtml", true); // phtml generates an HTML fragment
    		        report.onreadystatechange = function() {
    		            if (report.readyState == 4) {
    		                myDiv.innerHTML = "<div style='background-color: #3B4B54; color: white'><div style='width:100%; text-align:center'><strong>" + reportName + "</strong></div><hr/></div>" + 
    		                	report.responseText.replace(/\.\.\/\.\.\//g, urlRoot); // TODO: temporary workaround to fix img links
    		                if (report.status == 200) {
    			                xRunLocation = report.getResponseHeader("X-RunLocation");
    		                }
    		            }
    		        }
    		        report.send();
                } else {
    				var ifrm = document.createElement("iframe");
    				ifrm.src = url + "/reports/phtml";
    				ifrm.width = "100%";
    				ifrm.height = "540px";
	                myDiv.innerHTML = "<div style='background-color: #3B4B54; color: white'><div style='width:100%; text-align:center'><strong>" + reportName + "</strong></div><hr/></div>";
	                myDiv.appendChild(ifrm); 
                }
		    }
		    
			/**
			 * Always delete the run instance when you're "finished" with a report run.
			 */
		    function deleteRunInstance() {
		    	var del = getXmlHttp();
		    	del.open("DELETE", xRunLocation, true);
		    	del.send();
		    }
		    
		    function init() {
	    		getApplicationList();
	    		changeApp();
		    }
	</script>
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
		<form action="SampleServlet" method="GET" enctype="multipart/form-data">
			<input type="submit" value="view created report"/>
		</form>
		<!--<div class="reports"></div>-->
    </body>
</html>