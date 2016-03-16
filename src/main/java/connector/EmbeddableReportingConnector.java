package connector;

import java.io.Console;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Set;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
//import java.text.ParseException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.annotation.Resource;

/*import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
*/
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.apache.sling.commons.json.JSONException;
import org.json.simple.parser.ParseException;



public class EmbeddableReportingConnector {
	private static ERSProxy m_ersConnection = null;		
	private static String m_schemaName = null;	
	private String repUserID;
    private String repPassword;
	private String ersURL;
	private String cloudantURL;
	
 public EmbeddableReportingConnector() {
	
        setConnection();
    }
	
    private void setConnection() {
		Map<String, String> env = System.getenv();
			String vcapCheck = env.get("VCAP_SERVICES");
			if (vcapCheck == null) {
				System.out.println("No VCAP_SERVICES found");
				return;
			}
			
    	if (vcapCheck != null) {
			String bundleUri = null;
			String reportingUri = null;
			String reportingUserId = null;
			String reportingPassword = null;
			String jdbcUri = null;
            String dsUserId = null;
            String dsPassword = null;
            boolean isAnalyticsWarehouse = false;

			try {
            String envServices = System.getenv("VCAP_SERVICES");
            
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(envServices);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray vcapArray = (JSONArray) jsonObject.get("erservice");
            JSONObject vcap = (JSONObject) vcapArray.get(0);
            JSONObject credentials = (JSONObject) vcap.get("credentials");
			reportingUri = credentials.get("url").toString();
			reportingPassword = credentials.get("password").toString();
			reportingUserId = credentials.get("userid").toString();
            this.repUserID = reportingUserId;
            this.repPassword = reportingPassword;
			this.ersURL = reportingUri;
					
        } catch (ParseException ex) {
        }
	/*
			try {
				//JSONObject services = new JSONObject(vcap);
				
				@SuppressWarnings("unchecked")
				
				JSONParser parser = new JSONParser();
				JSONObject services = (JSONObject) parser.parse(env.get("VCAP_SERVICES"));
			
				Set<String> serviceList =  services.keySet();
				
				for (String service : serviceList) {
					String name = service.toUpperCase();
					JSONObject credentials = (JSONObject) ((JSONObject) ((JSONArray) services.get(service)).get(0)).get("credentials");
					if (name.indexOf("erservice") != -1) {
						reportingUri = (String) credentials.get("url");
						reportingUserId = (String) credentials.get("userid");
						reportingPassword = (String) credentials.get("password");
					} else if ((name.indexOf("cloudantNoSQLDB") != -1) && (bundleUri == null)) {
						bundleUri = (String) credentials.get("url");
					}  else if ((name.indexOf("ANALYTICSWAREHOUSE") != -1 || name.indexOf("dashDB") != -1) && (jdbcUri == null)) {
						jdbcUri = (String) credentials.get("jdbcurl");
						dsUserId = (String) credentials.get("username");
                        dsPassword = (String) credentials.get("password");
                        isAnalyticsWarehouse = true;
					}
					this.repUserID = reportingUserId;
                    this.repPassword = reportingPassword;
					this.ersURL = reportingUri;
					this.cloudantURL = bundleUri;
				}

				if (reportingUri == null) {
					System.err.println("No reporting service found");
					return;
				}

				if (bundleUri == null) {
					System.err.println("No bundle storage service found");
					return;
				}
				
				if (jdbcUri == null) {
                    System.err.println("No SQL datasource service found");
                    return;
                } 


				synchronized (this) {
					System.out.println("ERSConnection "+ reportingUri+","+reportingUserId+","+reportingPassword+","+bundleUri);
					m_ersConnection = new ERSProxy(reportingUri, reportingUserId, reportingPassword, bundleUri);
					try {
						m_ersConnection.connect();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
                  
                    if(isAnalyticsWarehouse) {
                    	m_schemaName = dsUserId.toUpperCase();
                    } else {
                    	m_schemaName = "DB2INST1";
                    }
				}

				
			} /*catch (JSONException e) {
			}*/
		/*	catch(ParseException e){
				
			}*/
		}
		}
	
		public String getERSUserID() {
			return repUserID;
		}

		public String getERSPassword() {
			return repPassword;
		}
	
		public String getERSUrl() {
			return ersURL;
		}
		
		public String getCloudantUrl() {
			return cloudantURL;
		}
    }

 
          

