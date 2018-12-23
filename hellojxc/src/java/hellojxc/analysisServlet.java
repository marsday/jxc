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
@WebServlet(name = "analysisServlet", urlPatterns = {"/store","/fina"})
public class analysisServlet extends HttpServlet {

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
        }else if(uri.endsWith("/fina")) {
            finaOperation(request,response);
        }else
            processRequest(request, response);
    }
    //出入货物的数量统计：核销类货物才需要进行库存管理
    private void storeOperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    /*
    select goods_name,sum(volume)as in_volume from jxc_input where del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0 and type=0)  group by goods_name
    select goods_name,sum(volume)as out_volume from jxc_output where del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0 and type=0) group by goods_name
    */
        String startday = new String(request.getParameter("startday").getBytes("UTF-8"), "UTF-8");
        String endday = new String(request.getParameter("endday").getBytes("UTF-8"), "UTF-8");
        String goods_name = new String(request.getParameter("goods_name").getBytes("UTF-8"), "UTF-8");
        
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
        if(goods_name.equals("all"))
        {
            input_sql = "select goods_name,sum(volume)as in_volume from jxc_input where"
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"
                    + "and del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0 and type=0)  group by goods_name";
            output_sql = "select goods_name,sum(volume)as out_volume from jxc_output where" 
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"                    
                    +" and del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0 and type=0) group by goods_name";
        }else
        {
            input_sql = "select goods_name,sum(volume)as in_volume from jxc_input where "
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"                    
                    +" and del_flag=0 and goods_name = '" +goods_name+ "'";
            output_sql = "select goods_name,sum(volume)as out_volume from jxc_output where "
                   + " buytime >= '"+ startday + "'"
                   + " and buytime <= '"+ endday + "'"                        
                   +" and del_flag=0 and goods_name = '" +goods_name+ "'";
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
                if(name != null)
                    store.put(name,volume);
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
                        //有进货信息，更新存货量
                       int in_volume = (int)store.get(name);
                       store.put(name, in_volume-volume);
                    }else
                    {
                       store.put(name,-volume); 
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
            String volume = String.valueOf((int)store.get(name));

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            arrayBuilder.add(String.valueOf(index));
            arrayBuilder.add(name);
            arrayBuilder.add(volume);
            
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
   private void finaOperation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    /*
    select goods_name,sum(price)as in_volume from jxc_input where del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0)  group by goods_name
    select goods_name,sum(price)as out_volume from jxc_output where del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0) group by goods_name
    */ 
        String startday = new String(request.getParameter("startday").getBytes("UTF-8"), "UTF-8");
        String endday = new String(request.getParameter("endday").getBytes("UTF-8"), "UTF-8");
        String goods_name = new String(request.getParameter("goods_name").getBytes("UTF-8"), "UTF-8");
        
        String input_sql;
        String output_sql;

        if(goods_name.equals("all"))
        {
            input_sql = "select goods_name,sum(price)as in_price from jxc_input where"
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"
                    + "and del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0 and type=0)  group by goods_name";
            output_sql = "select goods_name,sum(price)as out_price from jxc_output where" 
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"                    
                    +" and del_flag=0 and goods_name in (select name from jxc_goods where del_flag=0 and type=0) group by goods_name";
        }else
        {
            input_sql = "select goods_name,sum(price)as in_price from jxc_input where "
                    + " buytime >= '"+ startday + "'"
                    + " and buytime <= '"+ endday + "'"                    
                    +" and del_flag=0 and goods_name = '" +goods_name+ "'";
            output_sql = "select goods_name,sum(price)as out_price from jxc_output where "
                   + " buytime >= '"+ startday + "'"
                   + " and buytime <= '"+ endday + "'"                        
                   +" and del_flag=0 and goods_name = '" +goods_name+ "'";
        }
 
        Map store = new HashMap();
        //获取进货数量信息
        ResultSet in_result = null;
        try{
            in_result = DBHelper.getDbHelper().executeQuery(input_sql);
            while(in_result.next())
            {
                String name = in_result.getString("goods_name");
                int price = in_result.getInt("in_price");                
                store.put(name,price);
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
                int price = out_result.getInt("out_price"); 
                if(store.containsKey(name))
                {
                    //有进货信息，更新存货量
                   int in_price = (int)store.get(name);
                   store.put(name, in_price-price);
                }else
                {
                   store.put(name,-price); 
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
            String price = String.valueOf((int)store.get(name));

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            arrayBuilder.add(String.valueOf(index));
            arrayBuilder.add(name);
            arrayBuilder.add(price);
            
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
