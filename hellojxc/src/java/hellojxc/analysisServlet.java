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
@WebServlet(name = "analysisServlet", urlPatterns = {"/store","/finabygoods","/finabyuser"})
public class analysisServlet extends HttpServlet {
    public class FinaVolume {
        public String goodsname;       
        public int in_volume;    
        public int out_volume;
        public int net_volume;
    };
    public class FinaPrice {
        public String goodsname;       
        public int in_price;    
        public int out_price;
        public int net_price;
    };
    
     public class FinaUser {
        public String username;       
        public int in_price;    
        public int out_price;
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
        if (uri.endsWith("/store")) {
            //return JSON
            storeOperation(request,response);
        }else if(uri.endsWith("/finabygoods")) {
            finabygoodsOperation(request,response);
        }else if(uri.endsWith("/finabyuser")) {
            finabyuserOperation(request,response);
        }else
            processRequest(request, response);
    }
    //出入货物的数量统计：核销类货物才需要进行库存管理
    private void storeOperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    /*
    //所有的
    select a.name as goods_name, a.del_flag as del_flag ,sum(b.volume)as in_volume from jxc_goods as a ,jxc_input as b 
    where a.id = b.goods_id and b.del_flag = 0 group by goods_name
    //指定的    
    select a.name as goods_name, a.del_flag as del_flag ,sum(b.volume)as in_volume from jxc_goods as a ,jxc_input as b 
    where a.id = b.goods_id and b.del_flag = 0 and b.goods_id = 3
    */
        String startday = new String(request.getParameter("startday").getBytes("UTF-8"), "UTF-8");
        String endday = new String(request.getParameter("endday").getBytes("UTF-8"), "UTF-8");
        String goods_id = new String(request.getParameter("goods_id").getBytes("UTF-8"), "UTF-8");
        
        String input_sql;
        String output_sql;

        /*
        input_sql = "select goods_name,sum(volume)as in_volume from jxc_input where "
                + " buytime >= '"+ startday + "'"
                + " and buytime <= '"+ endday + "'"                    
                +" and del_flag=0 and goods_name in ('" +goods_name+ "')";
        output_sql = "select goods_name,sum(volume)as out_volume from jxc_output where "
               + " buytime >= '"+ startday + "'"
               + " and buytime <= '"+ endday + "'"                        
               +" and del_flag=0 and goods_name in ('" +goods_name+ "')";
        */
        if(goods_id.equals("all"))
        {
            //所有在货物管理中定义的(包含删除的)货物的进出情况
            input_sql = "select a.name as goods_name,sum(b.volume)as in_volume from jxc_goods as a ,jxc_input as b  where"
                    + " b.buytime >= '"+ startday + "'"
                    + " and b.buytime <= '"+ endday + "'"
                    + "and a.id = b.goods_id and b.del_flag = 0 group by goods_name";
            output_sql = "select a.name as goods_name,sum(b.volume)as out_volume from jxc_goods as a ,jxc_output as b  where"
                    + " b.buytime >= '"+ startday + "'"
                    + " and b.buytime <= '"+ endday + "'"
                    + "and a.id = b.goods_id and b.del_flag = 0 group by goods_name";
        }else{
            //在货物管理中有效的，指定的货物的进出情况
            input_sql = "select a.name as goods_name, sum(b.volume)as in_volume from jxc_goods as a ,jxc_input as b  where"
                    + " b.buytime >= '"+ startday + "'"
                    + " and b.buytime <= '"+ endday + "'"
                    + "and a.id = b.goods_id and b.del_flag = 0 and b.goods_id=" + goods_id;
            output_sql = "select a.name as goods_name, sum(b.volume)as out_volume from jxc_goods as a ,jxc_output as b  where"
                    + " b.buytime >= '"+ startday + "'"
                    + " and b.buytime <= '"+ endday + "'"
                    + "and a.id = b.goods_id and b.del_flag = 0 and b.goods_id=" + goods_id;
        }
        Map store = new HashMap();
        //获取进货数量信息
        ResultSet in_result = null;
        try{
            in_result = DBHelper.getDbHelper().executeQuery(input_sql);
            while(in_result.next())
            {
                String name = in_result.getString("goods_name");
                int volume = in_result.getInt("in_volume");     
                FinaVolume detail = new FinaVolume();
                detail.goodsname = name;
                detail.in_volume = volume;
                store.put(name,detail);
            }
            if(in_result != null)
                in_result.close();            
        }catch(Exception err)
        {
            err.printStackTrace();
        }

        //获取出货数量信息
        ResultSet out_result = null;
        try{
            out_result = DBHelper.getDbHelper().executeQuery(output_sql);
            while(out_result.next())
            {
                String name = out_result.getString("goods_name");
                int volume = out_result.getInt("out_volume"); 
                if(name != null)
                {
                    if(store.containsKey(name))
                    {
                        //有进货信息，更新存货量:进货量-出货量(因为进货量一般总是大于出货量)
                        FinaVolume detail = (FinaVolume)store.get(name);
                        detail.out_volume = volume;
                        detail.net_volume = detail.in_volume - detail.out_volume;
                        store.put(name, detail);
                    }else
                    {
                        FinaVolume detail = new FinaVolume();
                        detail.goodsname = name;
                        detail.in_volume = 0;
                        detail.out_volume = volume;
                        detail.net_volume = detail.in_volume - detail.out_volume;
                        store.put(name,detail); 
                    }
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
            FinaVolume detail = (FinaVolume)store.get(name);
            
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            arrayBuilder.add(String.valueOf(index));
            arrayBuilder.add(name);
            arrayBuilder.add(String.valueOf(detail.in_volume));
            arrayBuilder.add(String.valueOf(detail.out_volume));
            arrayBuilder.add(String.valueOf(detail.net_volume));
            
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
    
    //出入货物的金额统计：所有货物(核销类，非核销类)都需要出入财务管理
   private void finabygoodsOperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    /*
    select goods_name,sum(price)as in_volume from jxc_input where del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0)  group by goods_name
    select goods_name,sum(price)as out_volume from jxc_output where del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0) group by goods_name
       
     //所有的
    select a.name as goods_name, sum(b.price)as in_price from jxc_goods as a ,jxc_input as b 
    where a.id = b.goods_id and b.del_flag = 0 group by goods_name
    //指定的    
    select a.name as goods_name, sum(b.price)as in_price from jxc_goods as a ,jxc_input as b 
    where a.id = b.goods_id and b.del_flag = 0 and b.goods_id = 3      
    */ 
        String startday = new String(request.getParameter("startday").getBytes("UTF-8"), "UTF-8");
        String endday = new String(request.getParameter("endday").getBytes("UTF-8"), "UTF-8");
        String goods_id = new String(request.getParameter("goods_id").getBytes("UTF-8"), "UTF-8");
        
        String input_sql;
        String output_sql;

        if(goods_id.equals("all"))
        {
            input_sql = "select a.name as goods_name, sum(b.price)as in_price from jxc_goods as a ,jxc_input as b where"
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"
                    + "and a.id = b.goods_id and b.del_flag = 0 group by goods_name";
            output_sql = "select a.name as goods_name, sum(b.price)as out_price from jxc_goods as a ,jxc_output as b where" 
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"                    
                    +" and a.id = b.goods_id and b.del_flag = 0 group by goods_name";
        }else
        {
            input_sql = "select a.name as goods_name, sum(b.price)as in_price from jxc_goods as a ,jxc_input as b  where "
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"                    
                    +" and a.id = b.goods_id and b.del_flag = 0 and b.goods_id =" +goods_id;
            output_sql = "select a.name as goods_name, sum(b.price)as out_price from jxc_goods as a ,jxc_output as b  where "
                   + " buytime >= '"+ startday + "'"
                   + " and buytime <= '"+ endday + "'"                        
                   +" and a.id = b.goods_id and b.del_flag = 0 and b.goods_id =" +goods_id;
        }
 
        Map store = new HashMap();
        //获取进货消费资金信息
        ResultSet in_result = null;
        try{
            in_result = DBHelper.getDbHelper().executeQuery(input_sql);
            while(in_result.next())
            {
                String name = in_result.getString("goods_name");
                int price = in_result.getInt("in_price"); 
                FinaPrice detail = new FinaPrice();
                detail.goodsname = name;
                detail.in_price = price;
                detail.out_price = 0;
                detail.net_price = 0;
                store.put(name,detail);
            }
            if(in_result != null)
                in_result.close();            
        }catch(Exception err)
        {
            err.printStackTrace();
        }

        //获取出货所得资金信息
        ResultSet out_result = null;
        try{
            out_result = DBHelper.getDbHelper().executeQuery(output_sql);
            while(out_result.next())
            {
                String name = out_result.getString("goods_name");
                int price = out_result.getInt("out_price"); 
                if(store.containsKey(name))
                {
                    //有进货信息，更新存货占用总资金:进货-出货
                   //int in_price = (int)store.get(name);
                    FinaPrice d = (FinaPrice)store.get(name);
                    d.out_price = price;
                    d.net_price = d.in_price - d.out_price;
                   store.put(name, d);
                }else
                {
                    FinaPrice d = new FinaPrice();
                    d.goodsname = name;
                    d.in_price = 0;
                    d.out_price = price;
                    d.net_price = d.in_price - d.out_price;
                   store.put(name,d); 
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
            FinaPrice d = (FinaPrice)store.get(name);

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            arrayBuilder.add(String.valueOf(index));
            arrayBuilder.add(name);
            arrayBuilder.add(String.valueOf(d.in_price));
            arrayBuilder.add(String.valueOf(d.out_price));
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
   private void finabyuserOperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    /*
    select operator,sum(price)as in_volume from jxc_input where del_flag=0 and operator in (select name_ch from jxc_user where del_flag = 0 ) group by operator
    select operator,sum(price)as out_volume from jxc_output where del_flag=0 and operator in (select name_ch from jxc_user where del_flag = 0 ) group by operator 
       */ 
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
                detail.net_price = 0;
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
