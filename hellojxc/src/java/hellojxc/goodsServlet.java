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
     
        //String sql = "select name, unit, type from jxc_goods where del_flag=0";
        String sql = "select id, name, type from jxc_goods where del_flag=0";
        Utility.getLogger().log(Level.CONFIG, "获取货物品种list sql: " + sql);
        ResultSet result = null;
        int index = 0;       
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            while(result.next())
            {
                String id = result.getString("id");
                String name = result.getString("name");
                String type = result.getString("type");
                
                // value array json
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                arrayBuilder.add(id);
                arrayBuilder.add(id);
                arrayBuilder.add(name);
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

            Utility.getLogger().log(Level.CONFIG, "获取货物品种list记录数目 = " + index);
			
            if(result != null)
                result.close();
        }catch(Exception err)
        {
            Utility.getLogger().log(Level.SEVERE, "获取货物品种list error = " + err.getMessage());
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
            
        String[] ids = request.getParameterValues("id");
        String input;
        //if(ids.length > 0)
        //{
            input = new String( ids[0].getBytes("ISO-8859-1"), "UTF-8");
        //}
        String sql = "select id, name, type from jxc_goods where id= " + "'" + input + "'";
        Utility.getLogger().log(Level.INFO, "获取指定货物品种: id= " + input);
        Utility.getLogger().log(Level.CONFIG, "获取指定货物品种 sql: " + sql);
        String json = "{\"data\":[";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            int index = 0;
            while(result.next())
            {
                String id = result.getString("id");
                String name = result.getString("name");
                String type = result.getString("type");
                
                //name-value json
                JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                jsonBuilder.add("id", id);
                jsonBuilder.add("name", name);
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
            if(index == 0)
                Utility.getLogger().log(Level.SEVERE, "没有获取到指定货物品种: sql=" + sql);
            if(result != null)
                result.close();
        }catch(Exception err)
        {
            Utility.getLogger().log(Level.SEVERE, "获取指定货物品种 error= " + err.getMessage());
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

        String[] ids = request.getParameterValues("id[]");
	String idlist="";
        if(ids.length > 0)
        {
            String sql = "update jxc_goods set del_flag = 1 where id in (";
            int index = 0;
            for(String obj:ids)
            {
                 String id = new String(obj.getBytes("ISO-8859-1"), "UTF-8");
                 if(index!=0)
		{
                    idlist += ",";
                     sql += ",";
		}
                sql += "'" + id + "'";
		idlist += "'" + id + "'";
                index++;

            }
            sql += ")";
	    Utility.getLogger().log(Level.INFO, "货物品种删除 idlist=: " + idlist);
            Utility.getLogger().log(Level.CONFIG, "货物品种删除 sql: " + sql);
            try{
				DBHelper.getDbHelper().executeUpdate(sql);
            }catch(Exception err)
            {
		Utility.getLogger().log(Level.SEVERE, "删除货物品种 error = " + err.getMessage());
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
        String type = new String(request.getParameter("type").getBytes("ISO-8859-1"), "UTF-8");

        String sql = "insert into jxc_goods (name,type) values(" 
                + "'" +  name + "'," 
                + "'" +  type + "'" 
                + ")";
		Utility.getLogger().log(Level.INFO, "货物品种增加: name= " + name + " type= " + type);
        Utility.getLogger().log(Level.CONFIG, "货物品种增加 sql: " + sql);
       try{
         DBHelper.getDbHelper().executeUpdate(sql);
       }catch(Exception err)
       {
		   Utility.getLogger().log(Level.SEVERE, "货物品种增加 error = " + err.getMessage());
           err.printStackTrace();
       }
        //request.getRequestDispatcher("/index_1.html").forward(request,response);
        response.sendRedirect("index.html?function=listgoods");
        
    }
    
    private void updateoperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String id = new String(request.getParameter("id").getBytes("ISO-8859-1"), "UTF-8");
        String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
        String type = new String(request.getParameter("type").getBytes("ISO-8859-1"), "UTF-8"); 
        
        String sql = "update jxc_goods set name = " + "'"+name +"'," + " type = " + "'"+ type + "'"
                                                        + " where id=" + "'" + id + "'"; 
		Utility.getLogger().log(Level.INFO, "货物品种更新 id=: " + id + " name= " + name);												
        Utility.getLogger().log(Level.CONFIG, "货物品种更新 sql: " + sql);
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
