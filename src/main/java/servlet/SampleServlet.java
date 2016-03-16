package servlet;


import java.io.*;
import java.net.*;
import java.io.Console;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Set;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//import java.text.ParseException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.xml.bind.DatatypeConverter;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

import javax.servlet.annotation.WebServlet;
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

@WebServlet(name = "SampleServlet", urlPatterns = {"/SampleServlet"})

public class SampleServlet extends HttpServlet {	
//	private static ERSProxy m_ersConnection = null;	
        
	private static final int BUFFER_SIZE = 32767;
	private String m_uri = "https://erservice-impl.ng.bluemix.net";
	String reportingUserId = "2e2ed450-7ec0-45e0-89ef-5772f0b29d4b";
	String reportingPassword = "5df825c3-ce77-4525-bba4-e64a6d183ff7";
	private String m_bundleUri = "https://2549fade-55f6-4bfc-8791-f06f99e1feeb-bluemix:c288fcf5d6cfa5df0cc7f8480c2c00b298986c2b4567466ff699236d287403d7@2549fade-55f6-4bfc-8791-f06f99e1feeb-bluemix.cloudant.com";
	private String m_authenticationInfo = "2e2ed450-7ec0-45e0-89ef-5772f0b29d4b:5df825c3-ce77-4525-bba4-e64a6d183ff7";
	private String m_cookies;
	String userandpass = "2e2ed450-7ec0-45e0-89ef-5772f0b29d4b:5df825c3-ce77-4525-bba4-e64a6d183ff7";
        String passString;
	

	private int doVerb(String method, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int status = 200;
		byte[] buffer = new byte[BUFFER_SIZE];
		InputStream in = null;

		String target = m_uri + "/ers/v1/connection/";
		String queryString = request.getQueryString();
		/*if (queryString != null) {
			target += "?" + queryString;
		}*/
		
		System.out.println("Target: "+target);

		URL url = new URL(target);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);

		connection.setInstanceFollowRedirects(false);

		for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames
				.hasMoreElements();) {
			String headerName = (String) headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			if (headerName.startsWith("Cookie")) {
				headerValue = headerValue.replaceAll("ERS-", "");
			}
			
			//Do not copy the request property from the original request
			//Or it always returns 404 error
//			connection.addRequestProperty(headerName, headerValue);
//			System.out.println("Set "+headerName+" To "+headerValue);
			
		}

		// add stored-cookie from connection
		if(!m_cookies.isEmpty()) {
			connection.addRequestProperty("Cookie", m_cookies);
			System.out.println("Add Cookie: "+m_cookies);
		}
		if(!m_cookies.isEmpty()) {
			connection.addRequestProperty("Cookie", m_cookies);
			//System.out.println("Add Cookie: "+m_cookies);
		}
		
		if (m_authenticationInfo != null) {
			connection.setRequestProperty(
					"Authorization",
					"Basic "
							+ DatatypeConverter
									.printBase64Binary(m_authenticationInfo
											.getBytes()));
		}

                StringBuilder sb = new StringBuilder();
                
		if (request.getContentLength() > 0) {
			connection.setDoOutput(true);
			ServletInputStream servletIn = request.getInputStream();
			OutputStream out = connection.getOutputStream();
			int read = 0;
			while ((read = servletIn.read(buffer)) > 0) {
				out.write(buffer, 0, read);
                                sb.append(read);
			}
                        System.out.println("content length:" + sb.toString());
			out.close();
		}
               /* if (request.getContentLength() > 0) {
                InputStream inputstream = request.getInputStream();
         InputStreamReader isreader = new InputStreamReader(inputstream);
         BufferedReader inBF;
            inBF = new BufferedReader(isreader);
 
         String inputline;
         StringBuilder sb = new StringBuilder();
         while ((inputline = inBF.readLine()) != null)
         {
             sb.append(inputline);
         }
         in.close();
                String output = sb.toString();
		System.out.println(output);		
		//request.setAttribute("postConnection",sb.toString());
                }*/
                
		try {
			connection.connect();
                        boolean proxyBool = connection.usingProxy();
                        System.out.println("is proxy: " +proxyBool);
			try {
				status = connection.getResponseCode();
			} catch (Exception e) {
			}
			
			System.out.println("Response Code: "+status);
                        connection.getErrorStream();
                        String message = connection.getResponseMessage();
                        System.out.println("Response message" + message);
			response.setStatus(connection.getResponseCode());

			String name = null;
			for (int idx = 1; (name = connection.getHeaderFieldKey(idx)) != null; idx++) {
				String value = connection.getHeaderField(idx);
				if (name.equals("Set-Cookie")) {
					value = "ERS-" + value;
				}
				response.addHeader(name, value);
			}

			/*in = (status >= 400 ? connection.getErrorStream() : connection
					.getInputStream());
			if (in != null) {
				ServletOutputStream out = response.getOutputStream();
				int read = 0;
				while ((read = in.read(buffer)) > 0) {
					out.write(buffer, 0, read);
					//System.out.print(new String(buffer, 0, read));
				}
				//System.out.println("");
			}*/
		} 
                finally {
			if (in != null) {
				in.close();
			}
		}

		return status;
	}
       
	
private int doConnect(String method, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		m_cookies="";
		int read = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		InputStream in = null;

		/*if (m_uri == null) {
			return null;
		}*/

		String target = m_uri + "/ers/v1/connection/";

		URL url = new URL(target);
	//connecting to the ers connection
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");

		connection.setInstanceFollowRedirects(false);
//authentication
		if (m_authenticationInfo != null) {
			connection.setRequestProperty(
					"Authorization",
					"Basic "
							+ DatatypeConverter
									.printBase64Binary(m_authenticationInfo
											.getBytes()));

		}
                System.out.println("Authorization: "+"Basic "
							+ DatatypeConverter
									.printBase64Binary(m_authenticationInfo
											.getBytes()));

//bundleUri - cloudant
		String requestString = "{\"bundleUri\": \"" + m_bundleUri + "\"}";

		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", Integer.toString(requestString.length()));

		connection.setDoOutput(true);
		ByteArrayInputStream requestStream = new ByteArrayInputStream(
				requestString.getBytes());
		OutputStream out = connection.getOutputStream();
		read = 0;
		while ((read = requestStream.read(buffer)) > 0) {
			out.write(buffer, 0, read);
                        System.out.println("read read:" +read);
		}
		out.close();

		try {
			connection.connect();

			int status = 200;

			try {
				status = connection.getResponseCode();
				System.out.println("Connect Response Code: "+status);
			} catch (Exception e) {
				e.printStackTrace();
			}

			in = (status >= 400 ? connection.getErrorStream() : connection
					.getInputStream());
			if (in != null) {
				ByteArrayOutputStream responseByte = new ByteArrayOutputStream();
                                StringBuilder sb = new StringBuilder();
				read = 0;
				while ((read = in.read(buffer)) > 0) {
                                        sb.append(in.read());
					responseByte.write(buffer, 0, read);
					System.out.print(new String(buffer, 0, read));
                                        passString = new String(buffer, 0, read);
                                        System.out.println("passString: " +passString);
				}
				System.out.println("");
                                System.out.println("Final passString:" + passString);
			
				// look in the response for any Set-Cookies
				String name = null;
				for (int idx = 1; (name = connection.getHeaderFieldKey(idx)) != null; idx++) {
					String value = connection.getHeaderField(idx);
					if (name.equals("Set-Cookie")) {
						String cookie = value.substring(0, value.indexOf(";")+1); // +1 to include semi-colon
						m_cookies += cookie;
					}
					if (name.equals("Cookie")) {
						String cookie = value.substring(0, value.indexOf(";")+1); // +1 to include semi-colon
						m_cookies += cookie;
					}
				}
				System.out.println("Cookie: "+m_cookies);
				System.out.println("repoonse code: " +status);
                                return status;
			}
			
		} finally {
			if (in != null) {
				in.close();
			}
		}
          return 1;
	}
	
/*private getPackageReport(){
    
}*/

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	//https://erservice-impl.ng.bluemix.net/ers/v1/definitions/1b5ed4dccc65e78bc6510cf95555e27c/reports/phtml	
            int status = doConnect("POST", request, response);
            System.out.println("doConnect: " +status);
           status = doVerb("GET", request, response);
           System.out.println("doVerb: " +status);
            request.setAttribute("postConnection", status);
           // StringBuilder sb2 = new StringBuilder();
		
		//String input_query = (String) request.getParameter("tQuery");
		//String input_size = (String) request.getParameter("tSize");
		
	/*	String urlstring = "https://erservice-impl.ng.bluemix.net/ers/v1/definitions/";
		
		String userandpass = "0b37065b-9d31-4dbc-933a-5907933d96af:a6999eae-8a9f-4d66-8a11-a3fbc124d830";
		
		String basicAuthorization = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userandpass.getBytes());
		
		
		 URL finalurl = new URL(urlstring);
         URLConnection urlconnection = finalurl.openConnection();
 
         urlconnection.setRequestProperty ("Authorization", basicAuthorization);
 
         InputStream inputstream = urlconnection.getInputStream();
         InputStreamReader isreader = new InputStreamReader(inputstream);
         BufferedReader in = new BufferedReader(isreader);
 
         String inputline;
 
         while ((inputline = in.readLine()) != null)
         {
             sb2.append(inputline);
         }
         in.close();
		
				
		request.setAttribute("postConnection",sb2.toString());*/
          //  https://erservice-impl.ng.bluemix.net/ers/swagger-ui/#!/connection/addConnection
            
         /*   if(status == 200){
                 response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {*/
            /* TODO output your page here. You may use following sample code. */
          /*          out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet NewServlet</title>");            
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<div>"); 
                    out.println("<a href src=\"https://erservice-impl.ng.bluemix.net/ers/swagger-ui/#!/connection/addConnection\">> </a>" ); 
                    out.println("</div>"); 
                    out.println("</body>");
                    out.println("</html>");
                 }
            }*/
         
        response.setContentType("text/html");
        response.setStatus(200);
        request.getRequestDispatcher("index.jsp").forward(request, response);
            
	/*	int status = doConnect("POST", request, response);
		//int status = doVerb("POST", request, response);
		request.setAttribute("postConnection", status);
		
                
                
        response.setContentType("text/html");
        response.setStatus(200);
        request.getRequestDispatcher("index.jsp").forward(request, response);
*/
	}
	
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String inputDefinitions = (String) request.getParameter("definitions");
		String link = m_uri + inputDefinitions + "/reports/html";
                
       request.setAttribute("postConnection",link);
		
        response.setContentType("text/html");
        response.setStatus(200);
        request.getRequestDispatcher("index.jsp").forward(request, response);
                
    }

 /*   @Override
    protected void doDelete(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo.startsWith("/ers/v1")) {
            if (m_ersConnection != null) {
                m_ersConnection.doDelete(request, response);
            } else {
                response.setStatus(404);
            }
        }
    }

    @Override
    public void destroy() {
        if (m_ersConnection != null) {
            try {
                m_ersConnection.disconnect();
            } catch (Exception e) {
            }
        }
    }
          */
}
