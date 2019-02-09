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
@WebServlet(name = "analysisreportServlet", urlPatterns = {"/finabytarget","/finabypay"})
public class analysisreportServlet extends HttpServlet {
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
    //所有的
    select a.name as pay_name, sum(b.price)as daily_in_price 
    from jxc_next_pay as a, jxc_next_daily_input as b
    where a.id = b.pay_id and b.del_flag = 0 group by pay_name

    //指定的(已支付)
    select a.name as pay_name, sum(b.price)as daily_in_price 
    from jxc_next_pay as a, jxc_next_daily_input as b 
    where a.id = b.pay_id and b.del_flag = 0 and b.pay_id = 1

    //指定的(未支付)
    select '未支付' as pay_name, sum(price)as daily_in_price 
    from jxc_next_daily_input  
    where del_flag = 0 and pay_id = 0
     */ 

        String startday = new String(request.getParameter("startday").getBytes("UTF-8"), "UTF-8");
        String endday = new String(request.getParameter("endday").getBytes("UTF-8"), "UTF-8");
        String pay_id = new String(request.getParameter("pay_id").getBytes("UTF-8"), "UTF-8");
        
        //日常收入
        String daily_in_sql = "select a.name as pay_name, sum(b.price)as daily_in_price from jxc_next_pay as a, jxc_next_daily_input as b where"
                    + " operationtime >= '"+ startday + "'"
                    + " and operationtime <= '"+ endday + "'"
                    + "and a.id = b.pay_id and b.del_flag = 0 ";
        //日常支出
        String daily_out_sql = "select a.name as pay_name, sum(b.price)as daily_out_price from jxc_next_pay as a, jxc_next_daily_output as b where"
                    + " operationtime >= '"+ startday + "'"
                    + " and operationtime <= '"+ endday + "'"
                    + "and a.id = b.pay_id and b.del_flag = 0 "; 
        //销售收入   
        String sales_in_sql = "select a.name as pay_name, sum(b.price)as sales_in_price from jxc_next_pay as a, jxc_next_sales_input as b where"
                    + " operationtime >= '"+ startday + "'"
                    + " and operationtime <= '"+ endday + "'"
                    + "and a.id = b.pay_id and b.del_flag = 0 "; 

        //日常收入（未支付）
        String  daily_in_sql_nopay = "select '未支付' as pay_name, sum(price)as daily_in_price from jxc_next_daily_input  where del_flag = 0 and pay_id = 0";
        //日常支出未支付）
        String  daily_out_sql_nopay =  "select '未支付' as pay_name, sum(price)as daily_out_price from jxc_next_daily_output  where del_flag = 0 and pay_id = 0";
        //销售收入未支付）
        String  sales_in_sql_nopay =  "select '未支付' as pay_name, sum(price)as sales_in_price from jxc_next_sales_input  where del_flag = 0 and pay_id = 0";

         if(pay_id.equals("all"))
         {
             //包含未支付
            daily_in_sql += "group by pay_name" + " union " + daily_in_sql_nopay;
            daily_out_sql += "group by pay_name"+ " union " + daily_out_sql_nopay;
            sales_in_sql += "group by pay_name"+ " union " + sales_in_sql_nopay;
         }else if(pay_id.equals("0"))
         {
             //日常收入
             daily_in_sql = daily_in_sql_nopay;
             //日常支出
             daily_out_sql =  daily_out_sql_nopay;
             //销售收入
             sales_in_sql = sales_in_sql_nopay;
         }else{
            daily_in_sql += "and b.pay_id =" + pay_id;
            daily_out_sql += "and b.pay_id =" + pay_id;
            sales_in_sql += "and b.pay_id =" + pay_id;
         } 

        Map store = new HashMap();
        //获取日常收入
        ResultSet daily_in_result = null;
        try{
            daily_in_result = DBHelper.getDbHelper().executeQuery(daily_in_sql);
            while(daily_in_result.next())
            {
                String name = daily_in_result.getString("pay_name");
                int price = daily_in_result.getInt("daily_in_price");
                FinaUser detail = new FinaUser();
                detail.payname = name;
                detail.daily_in_price = price;
                detail.daily_out_price = 0;
                detail.sales_in_price = 0;
                store.put(name,detail);
            }
            if(daily_in_result != null)
                daily_in_result.close();            
        }catch(Exception err)
        {
            err.printStackTrace();
        }
        
        //获取日常支出
       ResultSet daily_out_result = null;
        try{
            daily_out_result = DBHelper.getDbHelper().executeQuery(daily_out_sql);
            while(daily_out_result.next())
            {
                String name = daily_out_result.getString("pay_name");
                int price = daily_out_result.getInt("daily_out_price"); 
                if(store.containsKey(name))
                {
                    FinaUser detail = (FinaUser)store.get(name);
                    detail.daily_out_price = price;
                    store.put(name, detail);
                }else
                {
                    FinaUser detail = new FinaUser();
                    detail.payname = name;
                    detail.daily_in_price = 0;
                    detail.daily_out_price = price;
                    detail.sales_in_price = 0;
                   store.put(name,detail); 
                }
            }             
            if(daily_out_result != null)
                daily_out_result.close();             
        }catch(Exception err)
        {
            err.printStackTrace();
        } 

        //获取销售收入
       ResultSet sales_in_result = null;
        try{
            sales_in_result = DBHelper.getDbHelper().executeQuery(sales_in_sql);
            while(sales_in_result.next())
            {
                String name = sales_in_result.getString("pay_name");
                int price = sales_in_result.getInt("sales_in_price"); 
                if(store.containsKey(name))
                {
                    FinaUser detail = (FinaUser)store.get(name);
                    detail.sales_in_price = price;
                    store.put(name, detail);
                }else
                {
                    FinaUser detail = new FinaUser();
                    detail.payname = name;
                    detail.daily_in_price = 0;
                    detail.daily_out_price = 0;
                    detail.sales_in_price = price;
                   store.put(name,detail); 
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
            FinaUser detail = (FinaUser)store.get(name);
            detail.net_price =  detail.daily_in_price +  detail.sales_in_price - detail.daily_out_price;
            
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            arrayBuilder.add(String.valueOf(index));
            arrayBuilder.add(name);
            arrayBuilder.add(String.valueOf(detail.daily_out_price));
            arrayBuilder.add(String.valueOf(detail.daily_in_price));
            arrayBuilder.add(String.valueOf(detail.sales_in_price));
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
