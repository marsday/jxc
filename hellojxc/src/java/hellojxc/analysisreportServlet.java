/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellojxc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

/**
 *
 * @author marsday
 */
@WebServlet(name = "analysisServlet", urlPatterns = {"/finabytarget","/finabypay"})
public class analysisreportServlet extends HttpServlet {
    public class FinaVolume {
        public String goodsname;       
        public int in_volume;    
        public int out_volume;
        public int net_volume;
    };
    public class FinaPrice {
        public String targetname;       
        public int daily_in_price;    
        public int daily_out_price;
        public int sales_in_price;
        public int net_price;
    };
    
     public class FinaUser {
        public String payname;       
        public int daily_in_price;    
        public int daily_out_price;
        public int sales_in_price;
        public int net_price;
    };   
    
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
            out.println("<title>Servlet storeServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet storeServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        if(uri.endsWith("/finabytarget")) {
           finabytargetOperation(request,response);
        }else if(uri.endsWith("/finabypay")) {
            finabypayOperation(request,response);
        }else
            processRequest(request, response);
    }

    //管理对象资金统计
   private void finabytargetOperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    /*
     //所有的
    select a.name as target_name, sum(b.price)as daily_in_price 
    from jxc_next_target as a, jxc_next_daily_input as b
    where a.id = b.target_id and b.del_flag = 0 group by target_name

    //指定的    
    select a.name as target_name, sum(b.price)as daily_in_price 
    from jxc_next_target as a, jxc_next_daily_input as b 
    where a.id = b.target_id and b.del_flag = 0 and b.target_id = 4
    */ 
        String startday = new String(request.getParameter("startday").getBytes("UTF-8"), "UTF-8");
        String endday = new String(request.getParameter("endday").getBytes("UTF-8"), "UTF-8");
        String target_id = new String(request.getParameter("target_id").getBytes("UTF-8"), "UTF-8");
        
        //日常收入
        String daily_in_sql = "select a.name as target_name, sum(b.price)as daily_in_price from jxc_next_target as a, jxc_next_daily_input as b where"
                    + " operationtime >= '"+ startday + "'"
                    + " and operationtime <= '"+ endday + "'"
                    + "and a.id = b.target_id and b.del_flag = 0 ";
        
        //日常支出
        String daily_out_sql = "select a.name as target_name, sum(b.price)as daily_out_price from jxc_next_target as a, jxc_next_daily_output as b where"
                    + " operationtime >= '"+ startday + "'"
                    + " and operationtime <= '"+ endday + "'"
                    + "and a.id = b.target_id and b.del_flag = 0 ";        
        //销售收入   
        String sales_in_sql = "select a.name as target_name, sum(b.price)as sales_in_price from jxc_next_target as a, jxc_next_sales_input as b where"
                    + " operationtime >= '"+ startday + "'"
                    + " and operationtime <= '"+ endday + "'"
                    + "and a.id = b.target_id and b.del_flag = 0 ";

         if(target_id.equals("all"))
         {
            daily_in_sql += "group by target_name";
            daily_out_sql += "group by target_name";
            sales_in_sql += "group by target_name";
         }else
         {
            daily_in_sql += "and b.target_id =" + target_id;
            daily_out_sql += "and b.target_id =" + target_id;
            sales_in_sql += "and b.target_id =" + target_id;
         }

         Map store = new HashMap();

        //获取日常收入资金信息
        ResultSet daily_in_result = null;
        try{
            daily_in_result = DBHelper.getDbHelper().executeQuery(daily_in_sql);
            while(daily_in_result.next())
            {
                String name = daily_in_result.getString("target_name");
                int price = daily_in_result.getInt("daily_in_price"); 
                FinaPrice d = new FinaPrice();
                d.targetname = name;
                d.daily_in_price = price;
                d.daily_out_price = 0;
                d.sales_in_price = 0;
                //d.net_price = d.daily_in_price +  d.sales_in_price - d.daily_out_price;
                store.put(name,d);
            }
            if(daily_in_result != null)
                daily_in_result.close();            
        }catch(Exception err)
        {
            err.printStackTrace();
        }

        //获取日常支出资金信息
        ResultSet daily_out_result = null;
        try{
            daily_out_result = DBHelper.getDbHelper().executeQuery(daily_out_sql);
            while(daily_out_result.next())
            {
                String name = daily_out_result.getString("target_name");
                int price = daily_out_result.getInt("daily_out_price"); 
                if(store.containsKey(name))
                {
                    FinaPrice d = (FinaPrice)store.get(name);
                    d.daily_out_price = price;
                    //d.net_price = d.daily_in_price +  d.sales_in_price - d.daily_out_price;
                    store.put(name, d);
                }else
                {
                    FinaPrice d = new FinaPrice();
                    d.targetname = name;
                    d.daily_in_price = 0;
                    d.daily_out_price = price;
                    d.sales_in_price = 0;
                   // d.net_price = d.daily_in_price +  d.sales_in_price - d.daily_out_price;
                    store.put(name,d); 
                }
            }             
            if(daily_out_result != null)
                daily_out_result.close();             
        }catch(Exception err)
        {
            err.printStackTrace();
        } 

        //获取销售收入资金信息
        ResultSet sales_in_result = null;
        try{
            sales_in_result = DBHelper.getDbHelper().executeQuery(sales_in_sql);
            while(sales_in_result.next())
            {
                String name = sales_in_result.getString("target_name");
                int price = sales_in_result.getInt("sales_in_price"); 
                if(store.containsKey(name))
                {
                    FinaPrice d = (FinaPrice)store.get(name);
                    d.sales_in_price = price;
                    //d.net_price = d.daily_in_price +  d.sales_in_price - d.daily_out_price;
                    store.put(name, d);
                }else
                {
                    FinaPrice d = new FinaPrice();
                    d.targetname = name;
                    d.daily_in_price = 0;
                    d.daily_out_price = 0;
                    d.sales_in_price = price;
                    //d.net_price = d.daily_in_price +  d.sales_in_price - d.daily_out_price;
                    store.put(name,d);
                }
            }
            if(sales_in_result != null)
                sales_in_result.close();            
        }catch(Exception err)
        {
            err.printStackTrace();
        }   


        String json = "{\"data\":[";
        Iterator iter = store.keySet().iterator();
        int index=1;
        while (iter.hasNext()) {
            String name = (String)iter.next();
            FinaPrice d = (FinaPrice)store.get(name);
            d.net_price =  d.daily_in_price +  d.sales_in_price - d.daily_out_price;

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            arrayBuilder.add(String.valueOf(index));
            arrayBuilder.add(name);
            arrayBuilder.add(String.valueOf(d.daily_out_price));
            arrayBuilder.add(String.valueOf(d.daily_in_price));
            arrayBuilder.add(String.valueOf(d.sales_in_price));
            arrayBuilder.add(String.valueOf(d.net_price));
            
            JsonArray empArray = arrayBuilder.build();
            StringWriter strWtr = new StringWriter();
            JsonWriter jsonWtr = Json.createWriter(strWtr);
            jsonWtr.writeArray(empArray);
            jsonWtr.close();
            if(index !=1)
                json+=",";
            json += strWtr.toString(); 
            
            index++;
        }        
        json += "]}";
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");        
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();       
    }
    
    //经办人的财务统计
   private void finabypayOperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    /*
    select operator,sum(price)as in_volume from jxc_input where del_flag=0 and operator in (select name_ch from jxc_user where del_flag = 0 ) group by operator
    select operator,sum(price)as out_volume from jxc_output where del_flag=0 and operator in (select name_ch from jxc_user where del_flag = 0 ) group by operator 
       */ 
    /*
        String startday = new String(request.getParameter("startday").getBytes("UTF-8"), "UTF-8");
        String endday = new String(request.getParameter("endday").getBytes("UTF-8"), "UTF-8");
        String user_names = new String(request.getParameter("usernames").getBytes("UTF-8"), "UTF-8");
        
        String input_sql;
        String output_sql;

        if(user_names.equals("all"))
        {
            input_sql = "select operator,sum(price)as in_price from jxc_input where"
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"
                    + "and del_flag=0 and operator in (select name_ch from jxc_user where del_flag = 0 )  group by operator";
            output_sql = "select operator,sum(price)as out_price from jxc_output where" 
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"                    
                    +" and del_flag=0 and operator in (select name_ch from jxc_user where del_flag = 0 ) group by operator";
        }else
        {
            input_sql = "select operator,sum(price)as in_price from jxc_input where "
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"                    
                    +" and del_flag=0 and operator = '" +user_names+ "'";
            output_sql = "select operator,sum(price)as out_price from jxc_output where "
                   + " buytime >= '"+ startday + "'"
                   + " and buytime <= '"+ endday + "'"                        
                   +" and del_flag=0 and operator = '" +user_names+ "'";
        }
 
        Map store = new HashMap();
        //获取进货金额(负数)
        ResultSet in_result = null;
        try{
            in_result = DBHelper.getDbHelper().executeQuery(input_sql);
            while(in_result.next())
            {
                String name = in_result.getString("operator");
                int price = in_result.getInt("in_price");
                FinaUser detail = new FinaUser();
                detail.username = name;
                detail.in_price = price;
                detail.out_price = 0;
                detail.net_price = detail.out_price - detail.in_price;;
                store.put(name,detail);
            }
            if(in_result != null)
                in_result.close();            
        }catch(Exception err)
        {
            err.printStackTrace();
        }

        //获取出货金额(正数),更新净收入
        ResultSet out_result = null;
        try{
            out_result = DBHelper.getDbHelper().executeQuery(output_sql);
            while(out_result.next())
            {
                String name = out_result.getString("operator");
                int price = out_result.getInt("out_price"); 
                if(store.containsKey(name))
                {
                    //已有进货信息，更新存货量
                    FinaUser detail = (FinaUser)store.get(name);
                    detail.out_price = price;
                    detail.net_price = detail.out_price - detail.in_price;
                    store.put(name, detail);
                }else
                {
                    FinaUser detail = new FinaUser();
                    detail.username = name;
                    detail.in_price = 0;
                    detail.out_price = price;
                    detail.net_price = detail.out_price - detail.in_price;
                   store.put(name,detail); 
                }
            }             
            if(out_result != null)
                out_result.close();             
        }catch(Exception err)
        {
            err.printStackTrace();
        }
        
        String json = "{\"data\":[";
        Iterator iter = store.keySet().iterator();
        int index=1;
        while (iter.hasNext()) {
            String name = (String)iter.next();
            FinaUser detail = (FinaUser)store.get(name);

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            arrayBuilder.add(String.valueOf(index));
            arrayBuilder.add(name);
            arrayBuilder.add(String.valueOf(detail.in_price));
            arrayBuilder.add(String.valueOf(detail.out_price));
            arrayBuilder.add(String.valueOf(detail.net_price));
            
            JsonArray empArray = arrayBuilder.build();
            StringWriter strWtr = new StringWriter();
            JsonWriter jsonWtr = Json.createWriter(strWtr);
            jsonWtr.writeArray(empArray);
            jsonWtr.close();
            if(index !=1)
                json+=",";
            json += strWtr.toString(); 
            
            index++;
        }        
        json += "]}";
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");        
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();      
    */
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
