/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellojxc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author marsday
 */
@WebServlet(name = "inputservlet", urlPatterns = {"/listinput","/addinput","/delinput","/updateinput","/getinput"})
public class inputservlet extends HttpServlet {

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
            out.println("<title>Servlet inputservlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet inputservlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void sendInputList_ajax(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        //2018-12-1 or 2018-12-02
        String datepicker_start = new String(request.getParameter("startday").getBytes("ISO-8859-1"), "UTF-8");
        String datepicker_end = new String(request.getParameter("endday").getBytes("ISO-8859-1"), "UTF-8");
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        Date date_start;
        Date date_end;
        try {
            date_start = (Date) sdf.parse(datepicker_start);
            date_end = (Date) sdf.parse(datepicker_end);
        } catch (ParseException ex) {
            Logger.getLogger(inputservlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String json = "{\"data\":[";
     
        String sql = "select a.id, b.name as goods_name, b.del_flag as del_flag, a.volume, a.price, a.buytime, a.recordtime, a.operator, a.customer_info, a.refer from jxc_input as a, jxc_goods as b where "
                                + " a.buytime >= '"+ datepicker_start + "'"
                                + " and a.buytime <= '"+ datepicker_end + "'"
                                + " and a.goods_id = b.id and a.del_flag = 0";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            int index = 0;
            while(result.next())
            {
                String id = result.getString("id");
                String goods_name = result.getString("goods_name");
                int del_flag = result.getInt("del_flag");
                if(del_flag != 0)
                   goods_name = "---"; 
                String volume = String.valueOf(result.getInt("volume"));
                String price = String.valueOf(result.getInt("price"));
                String buytime = result.getDate("buytime").toString();
                String recordtime = result.getDate("recordtime").toString();
                String operator = result.getString("operator");
                String customer_info = result.getString("customer_info");
                String refer = result.getString("refer");
                // value array json
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                arrayBuilder.add(id);
                arrayBuilder.add(goods_name);
                arrayBuilder.add(volume);
                arrayBuilder.add(price);
                arrayBuilder.add(buytime);
                arrayBuilder.add(recordtime);
                arrayBuilder.add(operator);
                arrayBuilder.add(customer_info==null?"":customer_info);
                arrayBuilder.add(refer);
                
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
        if(uri.endsWith("/getinput")) {
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
        if (uri.endsWith("/listinput")) {
            //return JSON
            sendInputList_ajax(request,response);
        }else if(uri.endsWith("/addinput")) {
            addoperation(request,response);
        }else if(uri.endsWith("/updateinput")) {
            updateoperation(request,response);
        }else if(uri.endsWith("/delinput")) {
            deloperation(request,response);
        }
        else
            processRequest(request, response);
    }
    private void deloperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] names = request.getParameterValues("id[]");
        if(names.length > 0)
        {
            String sql = "update jxc_input set del_flag = 1 where id in (";
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
       response.sendRedirect("index.html?function=listinput");
    } 
        
    private void updateoperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String id = new String(request.getParameter("id").getBytes("ISO-8859-1"), "UTF-8");
        String goodsid = new String(request.getParameter("goodsid").getBytes("ISO-8859-1"), "UTF-8");
        String volume = new String(request.getParameter("volume").getBytes("ISO-8859-1"), "UTF-8");   
        String price = new String(request.getParameter("price").getBytes("ISO-8859-1"), "UTF-8"); 
        String buytime = new String(request.getParameter("buytime").getBytes("ISO-8859-1"), "UTF-8"); 
        String customerinfo = new String(request.getParameter("customerinfo").getBytes("ISO-8859-1"), "UTF-8"); 
        String refer = new String(request.getParameter("refer").getBytes("ISO-8859-1"), "UTF-8"); 
        //当前登录者即为经办人
        HttpSession session = request.getSession();
        AccountInfo info = (AccountInfo)session.getAttribute("account");
        String operator = info.name_ch;
        
         //当前日期即为记录日期
        java.util.Date ud = new java.util.Date();
        Date date = new Date(ud.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String record_day = sdf.format(date);
        
        String sql = "update jxc_input set goods_id = " + "'"+goodsid +"',"
                                                        + " volume= " + volume + "," 
                                                        + " price= " + price +"," 
                                                        + " buytime= " + "'"+ buytime + "',"
                                                        + " recordtime= " + "'"+ record_day + "',"
                                                        + " refer= " + "'"+ refer + "',"
                                                        + " customer_info= " + "'"+ customerinfo + "',"
                                                        + " operator= " + "'"+ operator + "'"
                                                        + " where id="  +  id; 

       try{
         DBHelper.getDbHelper().executeUpdate(sql);
       }catch(Exception err)
       {
            err.printStackTrace();
       }
        //request.getRequestDispatcher("/index_1.html").forward(request,response);
        response.sendRedirect("index.html?function=listinput");
        
    }
        
    private void addoperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String goodsid = new String(request.getParameter("goodsid").getBytes("ISO-8859-1"), "UTF-8");
        String volume = new String(request.getParameter("volume").getBytes("ISO-8859-1"), "UTF-8");
        String price = new String(request.getParameter("price").getBytes("ISO-8859-1"), "UTF-8");
        String operation_day = new String(request.getParameter("operation_day").getBytes("ISO-8859-1"), "UTF-8");
        String customerinfo = new String(request.getParameter("customerinfo").getBytes("ISO-8859-1"), "UTF-8"); 
        String refer = new String(request.getParameter("refer").getBytes("ISO-8859-1"), "UTF-8");
        
        //当前登录者即为经办人
        HttpSession session = request.getSession();
        AccountInfo info = (AccountInfo)session.getAttribute("account");
        String operator = info.name_ch;
        
        //当前日期即为记录日期
        java.util.Date ud = new java.util.Date();
        Date date = new Date(ud.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String record_day = sdf.format(date);
        
        String sql = "insert into jxc_input (goods_id,volume,price,buytime,recordtime,operator,customer_info,refer) values(" 
                + "'" +  goodsid + "'," 
                +  volume + ","
                +  price  + ","
                +  "'" + operation_day + "'," 
                +  "'" +  record_day  + "',"  
                +  "'" +  operator  + "',"  
                +  "'" +  customerinfo  + "',"  
                +  "'"  +  refer  + "'"  
                + ")";
 
       try{
         DBHelper.getDbHelper().executeUpdate(sql);
       }catch(Exception err)
       {
            err.printStackTrace();
       }
        response.sendRedirect("index.html?function=listinput");
        
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
        String sql = "select id,goods_id, volume, price, buytime,operator,customer_info,refer from jxc_input where id= " + "'" + input + "'";
        String json = "{\"data\":[";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            int index = 0;
            while(result.next())
            {
                String id = result.getString("id");
                String goods_id = result.getString("goods_id");
                String volume = String.valueOf(result.getInt("volume"));
                String price = String.valueOf(result.getInt("price"));
                String buytime = result.getString("buytime");
                String operator = result.getString("operator");
                String customer_info = result.getString("customer_info");
                String refer = result.getString("refer");
                
                //name-value json
                JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                jsonBuilder.add("id", id);
                jsonBuilder.add("goods_id", goods_id);
                jsonBuilder.add("volume", volume);
                jsonBuilder.add("price", price);
                jsonBuilder.add("buytime", buytime);
                jsonBuilder.add("operator", operator);
                jsonBuilder.add("customer_info", customer_info==null?"":customer_info);
                jsonBuilder.add("refer", refer);
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
            err.printStackTrace();
        }
        json += "]}";
        //String json = "{\"data\": [[\"1\",\"Liqiang\",\"Shanghai\"],[\"2\",\"ZLY\",\"FuJian\"]]}";
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
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
