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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
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
@WebServlet(name = "loginServlet", urlPatterns = {"/login","/logout","/loginfo","/listusers","/chgpasswd"})
public class loginServlet extends HttpServlet {

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
            out.println("<title>Servlet loginServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet loginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void logoutoperation(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        response.setCharacterEncoding("UTF=8");
        response.setContentType("text/html;charset=UTF-8");
        Utility.getLogger().log(Level.INFO, " 退出登录");
        HttpSession session = request.getSession();
        session.invalidate();
        
        //response.sendRedirect("login.html");
        
    }
    private void loginoperation(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        
        response.setCharacterEncoding("UTF=8");
        response.setContentType("text/html;charset=UTF-8");
        
        String user = request.getParameter("user");
        String password = request.getParameter("input_password");
        
        //内设超级账户
        if(user.compareTo("admin") == 0)
        {
           if(password.compareTo("qwert12345") == 0)
           {
                HttpSession session = request.getSession();
                AccountInfo info = new AccountInfo();
                info.name_en = "admin";
                info.name_ch = "管理者";
                info.last_login = "----";
       
                session.setAttribute("account", info);
                session.setMaxInactiveInterval(10*60);//10分钟     
                response.sendRedirect("index.html");
           }else
            {
                //登录error,要求再次登录
                response.sendRedirect("login.html?function=loginerror");
            }
           return;
        }
        String sql = "select * from jxc_user where name_en='" + user + "'" + " and password='" + password + "'" + "and del_flag=0";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            if(result.next())
            {
                Utility.getLogger().log(Level.INFO, user +" 登录成功");
                HttpSession session = request.getSession();
                AccountInfo info = new AccountInfo();
                info.name_en = result.getString("name_en");
                info.name_ch = result.getString("name_ch");
                info.last_login = result.getString("last_login");
                
                session.setAttribute("account", info);
                session.setMaxInactiveInterval(10*60);//10分钟
                
                //更新最新登录时刻
                Timestamp now = new Timestamp(System.currentTimeMillis());
                DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String strLastLogin = sdf.format(now);
                String updatesql = "update jxc_user set last_login = "+ "'"+ strLastLogin + "'"
                                    + " where name_en='" + user + "'"+" and del_flag=0";
                try{
                    DBHelper.getDbHelper().executeUpdate(updatesql);
                 }catch(Exception err)
                {
                    err.printStackTrace();
                }  
                
                response.sendRedirect("index.html");
            }else
            {
                //登录error,要求再次登录
                Utility.getLogger().log(Level.SEVERE, user +" 登录失败");
                response.sendRedirect("login.html?function=loginerror");
            }
        }catch(Exception err)
        {
            err.printStackTrace();
        }
        
    }
    
    private void loginfooperation(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        HttpSession session = request.getSession();
        AccountInfo info = (AccountInfo)session.getAttribute("account");
        String name_en = info.name_en;
        String name_ch = info.name_ch;
        String last_login = info.last_login;
        
        String json = "{\"data\":[";
        
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("name_en", name_en);
        jsonBuilder.add("name_ch", name_ch);
        jsonBuilder.add("last_login", last_login); 
        
        JsonObject empObj = jsonBuilder.build();
        StringWriter strWtr = new StringWriter();
        JsonWriter jsonWtr = Json.createWriter(strWtr);
        jsonWtr.writeObject(empObj);
        jsonWtr.close();
        json += strWtr.toString();
        
        json += "]}";
        
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
        
    }
  
    private void listoperation(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String json = "{\"data\":[";
     
        String sql = "select name_en, password, name_ch, last_login from jxc_user where del_flag=0";
        ResultSet result = null;
        int index = 0;       
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            while(result.next())
            {
                //String id = result.getString("id");
                String name_ch = result.getString("name_ch");
                String name_en = result.getString("name_en");
                String last_login = result.getString("last_login");
                
                // value array json
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                //arrayBuilder.add(id);
                arrayBuilder.add(name_ch);
                arrayBuilder.add(name_en);
                arrayBuilder.add(last_login);

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
            //Logger.getLogger(goodsServlet.class.getName()).log(Level.SEVERE, null, "result of listgoods is; " + index);
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
        if (uri.endsWith("/logout")) {
            logoutoperation(request,response);
        }else if (uri.endsWith("/loginfo")) {
            loginfooperation(request,response);
        }else if(uri.endsWith("/listusers")) {
            //return JSON
            listoperation(request,response);
        }else
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
        if (uri.endsWith("/login")) {
            //return JSON
            loginoperation(request,response);
            return;
        }
        //登录验证
        if(!Utility.checkSession(request, response))
            return; 
        if(uri.endsWith("/chgpasswd"))
        {
            chgpasswd(request,response);
        }else
            processRequest(request, response);
    }
    private void chgpasswd(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        
        response.setCharacterEncoding("UTF=8");
        response.setContentType("text/html;charset=UTF-8");
 
         HttpSession session = request.getSession();
        AccountInfo info = (AccountInfo)session.getAttribute("account");
        String name_en = info.name_en;
        String name_ch = info.name_ch;
        
        String oldpassword = request.getParameter("oldpassword");
        String newpassword1 = request.getParameter("newpassword1");
        String newpassword2 = request.getParameter("newpassword2");
        
        String sql = "update jxc_user set password='"  + newpassword1 +"' where name_en='"+name_en + "' and name_ch='" + name_ch + "' and password='" + oldpassword +"'";
        int result = 0;
        try{
            result = DBHelper.getDbHelper().executeUpdate(sql);
       }catch(Exception err)
       {
            err.printStackTrace();
       }
       if(result != 1)//失败时
            response.sendRedirect("chgpasswd.html?function=resulterror");
       else
            response.sendRedirect("index.html");
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
