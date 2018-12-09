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
 * @author marsday
 */
@WebServlet(name = "goodsServlet", urlPatterns = {"/listgoods","/addgoods","/delgoods","/updategoods","/getgoods"})
public class goodsServlet extends HttpServlet {

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
            out.println("<title>Servlet goodsServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet goodsServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void listoperation(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String json = "{\"data\":[";
     
        String sql = "select name, unit, type from jxc_goods where del_flag=0";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            int index = 0;
            while(result.next())
            {
                //String id = result.getString("id");
                String name = result.getString("name");
                String unit = result.getString("unit");
                String type = result.getString("type");
                /*
                //name-value json
                JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                jsonBuilder.add("id", id);
                jsonBuilder.add("name", name);
                jsonBuilder.add("mobile", mobile);
                jsonBuilder.add("location", location);
                jsonBuilder.add("type", type);
                JsonObject empObj = jsonBuilder.build();
                
                StringWriter strWtr = new StringWriter();
                JsonWriter jsonWtr = Json.createWriter(strWtr);
                
                jsonWtr.writeObject(empObj);
                jsonWtr.close();
                */
                // value array json
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                //arrayBuilder.add(id);
                arrayBuilder.add(name);
                arrayBuilder.add(name);
                arrayBuilder.add(unit);
                arrayBuilder.add(type);
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
            if(result != null)
                result.close();
        }catch(Exception err)
        {
            err.printStackTrace();
        }
        json += "]}";
        //String json = "{\"data\": [[\"1\",\"Liqiang\",\"Shanghai\"],[\"2\",\"ZLY\",\"FuJian\"]]}";
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
 
    }
    
    private void getoperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
            
        String[] names = request.getParameterValues("id");
        String input;
        //if(ids.length > 0)
        //{
            input = new String( names[0].getBytes("ISO-8859-1"), "UTF-8");
        //}
        String sql = "select name, unit, type from jxc_goods where name= " + "'" + input + "'";
        String json = "{\"data\":[";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            int index = 0;
            while(result.next())
            {
                //String id = result.getString("id");
                String name = result.getString("name");
                String unit = result.getString("unit");
                String type = result.getString("type");
                
                //name-value json
                JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                jsonBuilder.add("id", name);
                jsonBuilder.add("name", name);
                jsonBuilder.add("unit", unit);
                jsonBuilder.add("type", type);
                JsonObject empObj = jsonBuilder.build();
                
                StringWriter strWtr = new StringWriter();
                JsonWriter jsonWtr = Json.createWriter(strWtr);
                
                jsonWtr.writeObject(empObj);
                jsonWtr.close();
                
                /*
                // value array json
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                arrayBuilder.add(id);
                arrayBuilder.add(name);
                arrayBuilder.add(mobile);
                arrayBuilder.add(location);
                arrayBuilder.add(type);
                JsonArray empArray = arrayBuilder.build();
                
                StringWriter strWtr = new StringWriter();
                JsonWriter jsonWtr = Json.createWriter(strWtr);
                
                jsonWtr.writeArray(empArray);
                jsonWtr.close();
                */
                if(index !=0)
                    json+=",";
                
                json += strWtr.toString();
                            
                index++;
            }
            if(result != null)
                result.close();
        }catch(Exception err)
        {
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
        if (uri.endsWith("/listgoods")) {
            //return JSON
            listoperation(request,response);
        }else if(uri.endsWith("/getgoods")) {
            getoperation(request,response);
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
        if (uri.endsWith("/addgoods")) {
            addoperation(request,response);
        }else if(uri.endsWith("/delgoods")) {
            deloperation(request,response);
        }else if(uri.endsWith("/updategoods")) {
            updateoperation(request,response);
        }
        else
            processRequest(request, response);
    }
    private void deloperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] names = request.getParameterValues("id[]");
        if(names.length > 0)
        {
            String sql = "update jxc_goods set del_flag = 1 where name in (";
            int index = 0;
            for(String obj:names)
            {
                 String name = new String(obj.getBytes("ISO-8859-1"), "UTF-8");
                 if(index!=0)
                     sql += ",";
                 sql += "'" + name + "'";
                 index++;

            }
            sql += ")";

            try{
             DBHelper.getDbHelper().executeUpdate(sql);
            }catch(Exception err)
            {
                err.printStackTrace();
            }      
        }
       response.sendRedirect("index.html?function=listgoods");
    } 
    
    private void addoperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
        String unit = new String(request.getParameter("unit").getBytes("ISO-8859-1"), "UTF-8");
        String type = new String(request.getParameter("type").getBytes("ISO-8859-1"), "UTF-8");

        String sql = "insert into jxc_goods (name,type,unit) values(" 
                + "'" +  name + "'," 
                + "'" +  type + "'," 
                + "'" +  unit + "'" 
                + ")";

       try{
         DBHelper.getDbHelper().executeUpdate(sql);
       }catch(Exception err)
       {
            err.printStackTrace();
       }
        //request.getRequestDispatcher("/index_1.html").forward(request,response);
        response.sendRedirect("index.html?function=listgoods");
        
    }
    
    private void updateoperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String org_name = new String(request.getParameter("id").getBytes("ISO-8859-1"), "UTF-8");
        String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
        String unit = new String(request.getParameter("unit").getBytes("ISO-8859-1"), "UTF-8");   
        String type = new String(request.getParameter("type").getBytes("ISO-8859-1"), "UTF-8"); 
        
        String sql = "update jxc_goods set name = " + "'"+name +"'," + " type = " + "'"+ type + "'," + " unit = " + "'"+ unit +"'"
                                                        + " where name=" + "'" + org_name + "'"; 

       try{
         DBHelper.getDbHelper().executeUpdate(sql);
       }catch(Exception err)
       {
            err.printStackTrace();
       }
        //request.getRequestDispatcher("/index_1.html").forward(request,response);
        response.sendRedirect("index.html?function=listgoods");
        
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
