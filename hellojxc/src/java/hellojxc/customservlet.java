/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellojxc;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;
import java.net.URLDecoder.*;

//import org.codehaus.jackson.map.ObjectMapper;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
/**
 *
 * @author liqiang
 */
@WebServlet(name = "customservlet", urlPatterns = {"/listcustomers","/addcustomer"})
public class customservlet extends HttpServlet {
    private List<Customer> customers = new ArrayList<Customer>();   
    
    @Override
    public void init() throws ServletException {
        Customer customer1 = new Customer();
        customer1.setId(1);
        customer1.setName("Donald D.");
        customers.add(customer1);
        
        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setName("Mickey M.");
        customers.add(customer2);       
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet customservlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet customservlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void addcustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
        String location = new String(request.getParameter("location").getBytes("ISO-8859-1"), "UTF-8");
        String mobile = new String(request.getParameter("mobile").getBytes("ISO-8859-1"), "UTF-8");
        String type = new String(request.getParameter("type").getBytes("ISO-8859-1"), "UTF-8");
        
        String sql = "insert into jxc_customer (name,mobile,location,type) values(" 
                + "'" +  name + "'," 
                + "'"+ mobile + "',"
                + "'"+ location + "',"
                +  type 
                + ")";

        DBHelper.executeNonQuery(sql);

        
        //request.getRequestDispatcher("/index_1.html").forward(request,response);
        response.sendRedirect("index.html");
        
    }
    private void sendCustomerList_ajax(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        /*
        {
            "data": 
            [
               ["1","Liqiang","Shanghai"],
               ["2","ZLY","FuJian"]
            ]
        }
        */
        String json = "{\"data\":[";
     
        String sql = "select name, mobile,location,type from jxc_customer where del_flag=0";
        ResultSet result = DBHelper.executeQuery(sql);
        int index = 0;
        try {
            while(result.next())
            {
                String name = result.getString("name");
                String mobile = result.getString("mobile");
                String location = result.getString("location");
                String type = result.getString("type");
                
                JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                jsonBuilder.add("name", name);
                jsonBuilder.add("mobile", mobile);
                jsonBuilder.add("location", location);
                jsonBuilder.add("type", type);
                JsonObject empObj = jsonBuilder.build();
                
                StringWriter strWtr = new StringWriter();
                JsonWriter jsonWtr = Json.createWriter(strWtr);
                
                jsonWtr.writeObject(empObj);
                jsonWtr.close();
                
                if(index !=0)
                    json+=",";
                
                json += strWtr.toString();
                            
                index++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(customservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if(result != null)
                result.close();
        } catch (SQLException ex) {
            Logger.getLogger(customservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        json += "]}";
        //String json = "{\"data\": [[\"1\",\"Liqiang\",\"Shanghai\"],[\"2\",\"ZLY\",\"FuJian\"]]}";
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
 
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.endsWith("/listcustomers")) {
            //return JSON
            sendCustomerList_ajax(request,response);
        }
        else
            processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.endsWith("/addcustomer")) {
            addcustomer(request,response);
        }
        else
            processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
