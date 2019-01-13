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
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonArrayBuilder;
/**
 *
 * @author liqiang
 */
@WebServlet(name = "customerservlet", urlPatterns = {"/listcustomer","/getcustomer","/updatecustomer","/delcustomer","/addcustomer"})
public class customerservlet extends HttpServlet {
    /*
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
    */
    
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
    private void delcustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] names = request.getParameterValues("id[]");
        if(names.length > 0)
        {
            String sql = "update jxc_next_customer set del_flag = 1 where id in (";
            int index = 0;
            for(String obj:names)
            {
                 String name = new String(obj.getBytes("ISO-8859-1"), "UTF-8");
                 if(index!=0)
                     sql += ",";
                 sql += name;
                 index++;

            }
            sql += ")";

            try{
             DBHelper.getDbHelper().executeUpdate(sql);
            }catch(Exception err)
            {
                Utility.getLogger().log(Level.SEVERE, "客户删除 error = " + err.getMessage());
                err.printStackTrace();
            }      
        }
       response.sendRedirect("index.html?function=listcustomer");
    }    
     private void getcustomer_AJAX(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
            
        String[] ids = request.getParameterValues("id");
        String input;
        //if(ids.length > 0)
        //{
            input = ids[0];
        //}
        String sql = "select id, name, mobile,province,city from jxc_next_customer where id= " + input;
        Utility.getLogger().log(Level.INFO, "获取指定客户: id= " + input);
        Utility.getLogger().log(Level.CONFIG, "获取指定客户 sql: " + sql);       
        String json = "{\"data\":[";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            int index = 0;
            while(result.next())
            {
                String id = result.getString("id");
                String name = result.getString("name");
                String mobile = result.getString("mobile");
                String province = result.getString("province");
                String city = result.getString("city");
                
                //name-value json
                JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                jsonBuilder.add("id", id);
                jsonBuilder.add("name", name==null?"":name);
                jsonBuilder.add("mobile", mobile==null?"":mobile);
                jsonBuilder.add("province", province==null?"":province);
                jsonBuilder.add("city", city==null?"":city);
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
            if(result != null)
                result.close();
        }catch(Exception err)
        {
            Utility.getLogger().log(Level.SEVERE, "获取指定客户 error = " + err.getMessage());
            err.printStackTrace();
        }
        json += "]}";
        //String json = "{\"data\": [[\"1\",\"Liqiang\",\"Shanghai\"],[\"2\",\"ZLY\",\"FuJian\"]]}";
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
    }
    private void addcustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
        String province = new String(request.getParameter("province").getBytes("ISO-8859-1"), "UTF-8");
        String mobile = new String(request.getParameter("mobile").getBytes("ISO-8859-1"), "UTF-8");
        String city = new String(request.getParameter("city").getBytes("ISO-8859-1"), "UTF-8");
        
        String sql = "insert into jxc_next_customer (name,mobile,province,city) values(" 
                + "'" +  name + "'," 
                + "'"+ mobile + "',"
                + "'"+ province + "',"
                + "'"+ city + "'"
                + ")";
	Utility.getLogger().log(Level.INFO, "客户增加: name= " + name + " mobile= " + mobile + " province= " + province + " city= " + city);
        Utility.getLogger().log(Level.CONFIG, "客户增加 sql: " + sql);
       try{
         DBHelper.getDbHelper().executeUpdate(sql);
       }catch(Exception err)
       {
           Utility.getLogger().log(Level.SEVERE, "客户增加 error = " + err.getMessage()); 
           err.printStackTrace();
       }
        //request.getRequestDispatcher("/index_1.html").forward(request,response);
        response.sendRedirect("index.html?function=listcustomer");
        
    }
    private void updatecustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String id = new String(request.getParameter("id").getBytes("ISO-8859-1"), "UTF-8");
        String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
        String province = new String(request.getParameter("province").getBytes("ISO-8859-1"), "UTF-8");
        String mobile = new String(request.getParameter("mobile").getBytes("ISO-8859-1"), "UTF-8");
        String city = new String(request.getParameter("city").getBytes("ISO-8859-1"), "UTF-8");
        
        
        String sql = "update jxc_next_customer set name = " + "'"+name+"'," + " mobile = " + "'"+ mobile + "',"
                                                         + " province = "  + "'"+ province+ "'," + " city = " + "'" + city + "'"
                                                        + " where id=" + id; 
        Utility.getLogger().log(Level.INFO, "客户更新 id=: " + id + " name= " + name + " mobile= " + mobile + " province= " + province + " city= " + city);												
        Utility.getLogger().log(Level.CONFIG, "客户更新 sql: " + sql);
       try{
         DBHelper.getDbHelper().executeUpdate(sql);
       }catch(Exception err)
       {
            Utility.getLogger().log(Level.SEVERE, "客户更新 error = " + err.getMessage());          
            err.printStackTrace();
       }
        //request.getRequestDispatcher("/index_1.html").forward(request,response);
        response.sendRedirect("index.html?function=listcustomer");
        
    }
    private void sendCustomerList_ajax(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String json = "{\"data\":[";
     
        String sql = "select id, name, mobile,province,city from jxc_next_customer where del_flag=0";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            int index = 0;
            while(result.next())
            {
                String id = result.getString("id");
                String name = result.getString("name");
                String mobile = result.getString("mobile");
                String province = result.getString("province");
                String city = result.getString("city");
                // value array json
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                arrayBuilder.add(id);
                arrayBuilder.add(name);
                arrayBuilder.add(mobile);
                arrayBuilder.add(province);
                arrayBuilder.add(city);
                JsonArray empArray = arrayBuilder.build();
                
                StringWriter strWtr = new StringWriter();
                JsonWriter jsonWtr = Json.createWriter(strWtr);
                
                jsonWtr.writeArray(empArray);
                jsonWtr.close();
                if(index !=0)
                    json+=",";
                
                json += strWtr.toString();
                            
                index++;
            }
            
            Utility.getLogger().log(Level.CONFIG, "获取客户信息记录数目 = " + index);
            if(result != null)
                result.close();
        }catch(Exception err)
        {
            Utility.getLogger().log(Level.SEVERE, "获取客户信息记录数目 error = " + err.getMessage());
            err.printStackTrace();
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
        //登录验证
        if(!Utility.checkSession(request, response))
            return;      
        String uri = request.getRequestURI();
        if (uri.endsWith("/listcustomer")) {
            //return JSON
            sendCustomerList_ajax(request,response);
        }else if(uri.endsWith("/getcustomer")) {
            getcustomer_AJAX(request,response);
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
        //登录验证
        if(!Utility.checkSession(request, response))
            return;     
        
        String uri = request.getRequestURI();
        if (uri.endsWith("/addcustomer")) {
            addcustomer(request,response);
        }else if(uri.endsWith("/delcustomer")) {
            delcustomer(request,response);
        }else if(uri.endsWith("/updatecustomer")) {
            updatecustomer(request,response);
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
