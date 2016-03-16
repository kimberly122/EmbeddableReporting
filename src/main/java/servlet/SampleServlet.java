package servlet;

import connector.EmbeddableReportingConnector;
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
	EmbeddableReportingConnector ersConnector = new EmbeddableReportingConnector();
        
	private static final int BUFFER_SIZE = 32767;
	private String m_uri = "https://erservice-impl.ng.bluemix.net";
	
	
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

}
