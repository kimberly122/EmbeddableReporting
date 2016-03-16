package servlet;

import connector.EmbeddableReportingConnector;
import java.io.*;
import java.net.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.PrintWriter;
import java.util.Enumeration;
import javax.xml.bind.DatatypeConverter;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

@WebServlet(name = "ErsServlet", urlPatterns = {"/ErsServlet"})

public class ErsServlet extends HttpServlet {	
	private static final int BUFFER_SIZE = 32767;
	private String m_uri = "https://erservice-impl.ng.bluemix.net";
	private String m_bundleUri = "https://2549fade-55f6-4bfc-8791-f06f99e1feeb-bluemix:c288fcf5d6cfa5df0cc7f8480c2c00b298986c2b4567466ff699236d287403d7@2549fade-55f6-4bfc-8791-f06f99e1feeb-bluemix.cloudant.com";
	private String m_authenticationInfo = "2e2ed450-7ec0-45e0-89ef-5772f0b29d4b:5df825c3-ce77-4525-bba4-e64a6d183ff7";
	int status;
	private String m_cookies;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	/*		EmbeddableReportingConnector connector = new EmbeddableReportingConnector();
		
		StringBuilder sb = new StringBuilder();
		
		//String input_query = (String) request.getParameter("tQuery");
		
		String urlstring = m_uri + "/ers/v1/connection/";
		URL url = new URL(urlstring);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		//connection.setRequestMethod("POST");
		if (m_authenticationInfo != null) {
			connection.setRequestProperty(
					"Authorization",
					"Basic "
							+ DatatypeConverter
									.printBase64Binary(m_authenticationInfo
											.getBytes()));

		}
		//String basicAuthorization = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(m_authenticationInfo.getBytes());
		
		 //URL finalurl = new URL(urlstring);
        // URLConnection urlconnection = finalurl.openConnection();
 
         //urlconnection.setRequestProperty ("Authorization", basicAuthorization);
		
		String bundleURI = "{\"bundleUri\": \"" + m_bundleUri + "\"}";
		
        connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", Integer.toString(bundleURI.length()));

		connection.setDoOutput(true);
		
		 
		connection.connect();

			try {
				status = connection.getResponseCode();
			} catch (Exception e) {
			}
			
			//System.out.println("Response Code: "+status);

			response.setStatus(connection.getResponseCode());
	
*/	
		doConnect();
		int status = doVerb("POST", request, response);
		
		request.setAttribute("postConnection",status);
		
        response.setContentType("text/html");
        //response.setStatus(200);
        request.getRequestDispatcher("index.jsp").forward(request, response);

	}
	
	private void doConnect() throws IOException {
		m_cookies="";
		int read = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		InputStream in = null;

		if (m_uri == null) {
			return;
		}

		String target = m_uri + "/ers/v1/connection/";

		URL url = new URL(target);
	//connecting to the ers connection
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");

		connection.setInstanceFollowRedirects(false);

		if (m_authenticationInfo != null) {
			connection.setRequestProperty(
					"Authorization",
					"Basic "
							+ DatatypeConverter
									.printBase64Binary(m_authenticationInfo
											.getBytes()));

		}


		String request = "{\"bundleUri\": \"" + m_bundleUri + "\"}";

		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", Integer.toString(request.length()));

		connection.setDoOutput(true);
		ByteArrayInputStream requestStream = new ByteArrayInputStream(
				request.getBytes());
		OutputStream out = connection.getOutputStream();
		read = 0;
		while ((read = requestStream.read(buffer)) > 0) {
			out.write(buffer, 0, read);
		}
		out.close();

		try {
			connection.connect();

			int status = 200;

			try {
				status = connection.getResponseCode();
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			in = (status >= 400 ? connection.getErrorStream() : connection
					.getInputStream());
			if (in != null) {
				ByteArrayOutputStream response = new ByteArrayOutputStream();

				read = 0;
				while ((read = in.read(buffer)) > 0) {
					response.write(buffer, 0, read);
					//System.out.print(new String(buffer, 0, read));
				}
				System.out.println("");

			
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
				
			}
			
		} finally {
			if (in != null) {
				in.close();
			}
		}

	}
 
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

		if (request.getContentLength() > 0) {
			connection.setDoOutput(true);
			ServletInputStream servletIn = request.getInputStream();
			OutputStream out = connection.getOutputStream();
			int read = 0;
			while ((read = servletIn.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
			out.close();
		}

		try {
			connection.connect();

			try {
				status = connection.getResponseCode();
			} catch (Exception e) {
			}
			
			System.out.println("Response Code: "+status);

			response.setStatus(connection.getResponseCode());

			String name = null;
			/*for (int idx = 1; (name = connection.getHeaderFieldKey(idx)) != null; idx++) {
				String value = connection.getHeaderField(idx);
				if (name.equals("Set-Cookie")) {
					value = "ERS-" + value;
				}
				response.addHeader(name, value);
			}*/

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
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return status;
	}
          
}
