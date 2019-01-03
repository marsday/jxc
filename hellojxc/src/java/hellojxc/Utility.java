/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hellojxc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author marsday
 */
public class Utility {
    private static Logger logger;
    //登录验证
    public static boolean checkSession(HttpServletRequest request,HttpServletResponse response) throws IOException
    {
        //区分ajax请求和其他请求
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
    
    public static Logger getLogger()
    {
        if(logger == null)
        {
            logger = Logger.getLogger("jxc");
            StringBuffer logpath = new StringBuffer();
            logpath.append("d:\\");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd-HHmmss");
            logpath = logpath.append("jxc_" + sdf.format(new Date()) + ".log");
            try{
                FileHandler filehandler = new FileHandler(logpath.toString(),true);
                logger.addHandler(filehandler);
                filehandler.setFormatter(new SimpleFormatter());
                filehandler.setLevel(Level.ALL);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return logger;
    }
}
