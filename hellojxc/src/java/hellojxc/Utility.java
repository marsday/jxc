/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellojxc;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author marsday
 */
public class Utility {
    
    //登录验证
    public static boolean checkSession(HttpServletRequest request,HttpServletResponse response) throws IOException
    {
        if(request.getSession() == null || request.getSession().getAttribute("account") == null)
        {
            if(request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equals("XMLHttpRequest"))
            {
                response.sendError(401);
                response.setStatus(401);
                response.setHeader("sessionstatus", "timeout");
            }else
            {
                 response.sendRedirect("login.html");
            }
            return false;
        }        
        return true;
    }
}
