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
@WebServlet(name = "salesinputservlet", urlPatterns = {"/listsalesinput","/addsalesinput","/delsalesinput","/updatesalesinput","/getsalesinput"})
public class salesinputservlet extends HttpServlet {

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
    
    private void listoperation(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        //2018-12-1 or 2018-12-02
        String datepicker_start = new String(request.getParameter("startday").getBytes("ISO-8859-1"), "UTF-8");
        String datepicker_end = new String(request.getParameter("endday").getBytes("ISO-8859-1"), "UTF-8");

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String json = "{\"data\":[";
     
        //查询已支付数据
        String sql1 = "select a.id as id,  b.name as target_name, c.name as pay_name, d.name as customer_name, a.price as price, a.volume as volume, a.unit as unit, a.grade as grade, a.operationtime as operationtime, a.recordtime as recordtime, "
                                +  "a.refer as refer, b.del_flag as target_del_flag, c.del_flag as pay_del_flag, d.del_flag as customer_del_flag " 
                                +  "from jxc_next_sales_input as a, jxc_next_target as b , jxc_next_pay as c,jxc_next_customer as d "
                                +  "where a.operationtime >= '"+ datepicker_start + "'"
                                +  " and a.operationtime <= '"+ datepicker_end + "'"
                                +  " and a.target_id = b.id and a.pay_id = c.id and a.customer_id = d.id and a.del_flag = 0";
        //查询未支付数据
        String sql2 = "select a.id as id,  b.name as target_name, '未支付' as pay_name, d.name as customer_name, a.price as price, a.volume as volume,a.unit as unit, a.grade as grade, a.operationtime as operationtime, a.recordtime as recordtime, "
                                +  "a.refer as refer, b.del_flag as target_del_flag, 0 as pay_del_flag, d.del_flag as customer_del_flag " 
                                +  "from jxc_next_sales_input as a, jxc_next_target as b ,jxc_next_customer as d "
                                +  "where a.operationtime >= '"+ datepicker_start + "'"
                                +  " and a.operationtime <= '"+ datepicker_end + "'"
                                +  " and a.target_id = b.id and a.pay_id = 0 and a.customer_id = d.id and a.del_flag = 0";
 
        String sql = sql1 + " union " + sql2;
        Utility.getLogger().log(Level.INFO, "查询销售收入记录");
        Utility.getLogger().log(Level.CONFIG, "查询销售收入记录 sql: " + sql);
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            int index = 0;
            while(result.next())
            {
                String id = result.getString("id");
                String target_name = result.getString("target_name");
                String pay_name = result.getString("pay_name");
                String customer_name = result.getString("customer_name");               
                String price = String.valueOf(result.getInt("price"));
                String volume = String.valueOf(result.getInt("volume"));
                String unit = result.getString("unit");
                String grade = result.getString("grade");           
                String operationtime = result.getDate("operationtime").toString();
                String recordtime = result.getDate("recordtime").toString();
                String refer = result.getString("refer");                
                String target_del_flag =  String.valueOf(result.getInt("target_del_flag"));
                String pay_del_flag = String.valueOf(result.getInt("pay_del_flag"));
                String customer_del_flag = String.valueOf(result.getInt("customer_del_flag"));
                // value array json
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                arrayBuilder.add(id);            
                arrayBuilder.add(target_name);
                arrayBuilder.add(pay_name);
                arrayBuilder.add(customer_name);
                arrayBuilder.add(price);
                arrayBuilder.add(volume);
                arrayBuilder.add(unit);
                arrayBuilder.add(grade);
                arrayBuilder.add(operationtime);
                arrayBuilder.add(recordtime);
                arrayBuilder.add(refer==null?"":refer);               
                arrayBuilder.add(target_del_flag);
                arrayBuilder.add(pay_del_flag);
                arrayBuilder.add(customer_del_flag);
                
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
            Utility.getLogger().log(Level.SEVERE, "查询销售收入记录 error = " + err.getMessage());
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
        if(uri.endsWith("/getsalesinput")) {
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
        if (uri.endsWith("/listsalesinput")) {
            //return JSON
            listoperation(request,response);
        }else if(uri.endsWith("/addsalesinput")) {
            addoperation(request,response);
        }else if(uri.endsWith("/updatesalesinput")) {
            updateoperation(request,response);
        }else if(uri.endsWith("/delsalesinput")) {
            deloperation(request,response);
        }
        else
            processRequest(request, response);
    }
    private void deloperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] names = request.getParameterValues("id[]");
        String idlist="";
        if(names.length > 0)
        {
            String sql = "update jxc_next_sales_input set del_flag = 1 where id in (";
            int index = 0;
            for(String obj:names)
            {
                 String name = new String(obj.getBytes("ISO-8859-1"), "UTF-8");
                 if(index!=0)
				 {
					 idlist += ",";
                     sql += ",";
				 }
				 idlist += "'" + name + "'";
                 sql += "'" + name + "'";
                 index++;

            }
            sql += ")";
            Utility.getLogger().log(Level.INFO, "销售收入删除 idlist: " + idlist);
            Utility.getLogger().log(Level.CONFIG, "销售收入删除 sql: " + sql);
            try{
                DBHelper.getDbHelper().executeUpdate(sql);
            }catch(Exception err)
            {
		Utility.getLogger().log(Level.SEVERE, "销售收入删除 error = " + err.getMessage());
                err.printStackTrace();
            }      
        }
       response.sendRedirect("index.html?function=listsalesinput");
    } 
        
    private void updateoperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String id = new String(request.getParameter("id").getBytes("ISO-8859-1"), "UTF-8");
        String customer_id = new String(request.getParameter("customer_id").getBytes("ISO-8859-1"), "UTF-8");
        String target_id = new String(request.getParameter("target_id").getBytes("ISO-8859-1"), "UTF-8");
        String pay_id = new String(request.getParameter("pay_id").getBytes("ISO-8859-1"), "UTF-8");   
        String price = new String(request.getParameter("price").getBytes("ISO-8859-1"), "UTF-8"); 
        String volume = new String(request.getParameter("volume").getBytes("ISO-8859-1"), "UTF-8");
        String unit = new String(request.getParameter("unit").getBytes("ISO-8859-1"), "UTF-8");
        String grade = new String(request.getParameter("grade").getBytes("ISO-8859-1"), "UTF-8");
        String operationtime = new String(request.getParameter("operationtime").getBytes("ISO-8859-1"), "UTF-8"); 
        String refer = new String(request.getParameter("refer").getBytes("ISO-8859-1"), "UTF-8"); 
        
         //当前日期即为记录日期
        java.util.Date ud = new java.util.Date();
        Date date = new Date(ud.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String recordtime = sdf.format(date);
        
        String sql = "update jxc_next_sales_input set target_id = " + target_id +","
                                                        + " pay_id= " + pay_id + ","
                                                        + " customer_id= " +  customer_id + "," 
                                                        + " price= " + price +"," 
                                                        + " volume= " + volume +"," 
                                                        + " unit= " + "'"+ unit + "',"
                                                        + " grade= " + "'"+ grade + "',"
                                                        + " operationtime= " + "'"+ operationtime + "',"
                                                        + " recordtime= " + "'"+ recordtime + "',"
                                                        + " refer= " + "'"+ refer + "'"
                                                        + " where id="  +  id; 
	Utility.getLogger().log(Level.INFO, "销售收入更新 id= " + id);
	Utility.getLogger().log(Level.CONFIG, "销售收入更新 sql: " + sql);
       try{
         DBHelper.getDbHelper().executeUpdate(sql);
       }catch(Exception err)
       {
            Utility.getLogger().log(Level.SEVERE, "更新销售收入 error = " + err.getMessage());
            err.printStackTrace();
       }
        //request.getRequestDispatcher("/index_1.html").forward(request,response);
        response.sendRedirect("index.html?function=listsalesinput");
        
    }
        
    private void addoperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //html:utf-8 --> http:ISO-8859-1 --> servlet:utf-8
        //byte[] orgname = request.getParameter("name").getBytes("ISO-8859-1");
        String customer_id = new String(request.getParameter("customer_id").getBytes("ISO-8859-1"), "UTF-8"); 
        String target_id = new String(request.getParameter("target_id").getBytes("ISO-8859-1"), "UTF-8");
        String pay_id = new String(request.getParameter("pay_id").getBytes("ISO-8859-1"), "UTF-8");
        String price = new String(request.getParameter("price").getBytes("ISO-8859-1"), "UTF-8");
        String volume = new String(request.getParameter("volume").getBytes("ISO-8859-1"), "UTF-8");
        String unit = new String(request.getParameter("unit").getBytes("ISO-8859-1"), "UTF-8");
        String grade = new String(request.getParameter("grade").getBytes("ISO-8859-1"), "UTF-8");
        String operationtime = new String(request.getParameter("operationtime").getBytes("ISO-8859-1"), "UTF-8");
        String refer=""; 
        if(request.getParameter("refer") != null)
               refer  = new String(request.getParameter("refer").getBytes("ISO-8859-1"), "UTF-8");
        //当前日期即为记录日期
        java.util.Date ud = new java.util.Date();
        Date date = new Date(ud.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String recordtime = sdf.format(date);
        
        String sql = "insert into jxc_next_sales_input (customer_id,target_id,pay_id,price,volume,unit,grade,operationtime,recordtime,refer) values(" 
                +  customer_id  + ","   
                +  target_id + "," 
                +  pay_id + ","
                +  price  + ","
                +  volume  + ","
                +  "'" + unit + "'," 
                +  "'" + grade + "'," 
                +  "'" + operationtime + "'," 
                +  "'" +  recordtime  + "',"   
                +  "'"  +  refer  + "'"  
                + ")";
	
        Utility.getLogger().log(Level.INFO, " 销售收入添加 target_id=" + target_id + " pay_id=" + pay_id  + " customer_id=" + customer_id+ " price=" + price + " volume=" + volume + " unit=" + unit  + " grade=" + grade + " operationtime=" + operationtime + " refer=" + refer);		
        Utility.getLogger().log(Level.CONFIG, "销售收入添加 sql: " + sql);
        try{
            DBHelper.getDbHelper().executeUpdate(sql);
        }catch(Exception err)
        {
            Utility.getLogger().log(Level.SEVERE, "销售收入添加 error = " + err.getMessage());
            err.printStackTrace();
        }
        response.sendRedirect("index.html?function=listsalesinput");
        
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
        String sql = "select id, customer_id, pay_id,target_id, price, volume, unit,grade,operationtime, recordtime, refer from jxc_next_sales_input where id= " + "'" + input + "'";
	Utility.getLogger().log(Level.INFO, "获取指定销售收入 id= " + input);
        Utility.getLogger().log(Level.CONFIG, "获取指定销售收入 sql: " + sql);
        String json = "{\"data\":[";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            int index = 0;
            while(result.next())
            {
                String id = result.getString("id");
                String target_id = String.valueOf(result.getInt("target_id"));
                String pay_id = String.valueOf(result.getInt("pay_id"));
                String customer_id = String.valueOf(result.getInt("customer_id"));
                String price = String.valueOf(result.getInt("price"));
                String volume = String.valueOf(result.getInt("volume"));
                String unit = result.getString("unit");
                String grade = result.getString("grade");
                String operationtime = result.getString("operationtime");
                String refer = result.getString("refer");
                
                //name-value json
                JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                jsonBuilder.add("id", id);
                jsonBuilder.add("target_id", target_id);
                jsonBuilder.add("pay_id", pay_id);
                jsonBuilder.add("customer_id", customer_id);
                jsonBuilder.add("price", price);
                jsonBuilder.add("volume", volume);
                jsonBuilder.add("unit", unit);
                jsonBuilder.add("grade", grade);
                jsonBuilder.add("operationtime", operationtime);
                jsonBuilder.add("refer", refer==null?"":refer);
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
                Utility.getLogger().log(Level.SEVERE, "没有获取到指定销售收入记录: sql=" + sql);			
            if(result != null)
                result.close();
        }catch(Exception err)
        {
            Utility.getLogger().log(Level.SEVERE, "获取指定销售收入记录 error = " + err.getMessage());
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
