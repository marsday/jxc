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
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
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
@WebServlet(name = "loginServlet", urlPatterns = {"/login"})
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
    private void loginoperation(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        
        response.setCharacterEncoding("UTF=8");
        response.setContentType("text/html;charset=UTF-8");
        
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        
        //String sql = "select * from jxc_user where name_en='liqiang' and password='123456' and del_flag=0";
        String sql = "select * from jxc_user where name_en='" + user + "'" + " and password='" + password + "'" + "and del_flag=0";
        ResultSet result = null;
        try{
            result = DBHelper.getDbHelper().executeQuery(sql);
            if(result.next())
            {
                //TODO生成session
                HttpSession session = request.getSession();
                AccountInfo info = new AccountInfo();
                info.name_en = result.getString("name_en");
                info.name_ch = result.getString("name_ch");
                info.last_login = result.getString("last_login");
                
                session.setAttribute("account", info);
                session.setMaxInactiveInterval(2*60);//2分钟
                
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
                //TODO 登录error
                response.sendRedirect("login_error.html");
            }
        }catch(Exception err)
        {
            err.printStackTrace();
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
        String uri = request.getRequestURI();
        if (uri.endsWith("/login")) {
            //return JSON
            loginoperation(request,response);
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
