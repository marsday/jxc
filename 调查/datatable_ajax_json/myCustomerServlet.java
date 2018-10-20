/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getstarted;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author liqiang
 */
@WebServlet(name = "myCustomerServlet", urlPatterns = {"/mycustomer","/getcustomers", "/myeditCustomer", "/myupdateCustomer"})
public class myCustomerServlet extends HttpServlet {
 private static final long serialVersionUID = -20L;

    private List<Customer> customers = new ArrayList<Customer>();
    
    @Override
    public void init() throws ServletException {
        Customer customer1 = new Customer();
        customer1.setId(1);
        customer1.setName("Donald D.");
        customer1.setCity("Miami");
        customers.add(customer1);
        
        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setName("Mickey M.");
        customer2.setCity("Orlando");
        customers.add(customer2);       
    }
    
    private void sendCustomerList(HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<!DOCTYPE html>"
                + "<html><head><title>Customers</title></head>"
                + "<body><h2>Customers </h2>");
        writer.println("<ul>");
        for (Customer customer : customers) {
            writer.println("<li>" + customer.getName() 
                   + "(" + customer.getCity() + ") (" 
                   + "<a href='myeditCustomer?id=" + customer.getId()
                   + "'>edit</a>)");
        }
        writer.println("</ul>");
        writer.println("</body></html>");
        
    }
    
     private void sendCustomerList_jsp(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
            request.setAttribute("customers",customers);
            //request.getRequestDispatcher("/mycustomer.jsp").forward(request,response);     
            request.getRequestDispatcher("/mycustomer_datatable.jsp").forward(request,response);
        
    }
     
    private void sendCustomerList_ajax(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

       String json = "{\"data\": [[\"1\",\"Liqiang\",\"Shanghai\"],[\"2\",\"ZLY\",\"FuJian\"]]}";
        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
 
    }
    
    private Customer getCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }
    
    private void sendEditCustomerForm(HttpServletRequest request, 
            HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        int customerId = 0;
        try {
            customerId = 
                    Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
        }
        Customer customer = getCustomer(customerId);
        
        if (customer != null) {
            writer.println("<!DOCTYPE html><html><head>"
                    + "<title>Edit Customer</title></head>"
                    + "<body><h2>Edit Customer</h2>"
                    + "<form method='post' "
                    + "action='myupdateCustomer'>");
            writer.println("<input type='hidden' name='id' value='" 
                    + customerId + "'/>");
            writer.println("<table>");
            writer.println("<tr><td>Name:</td><td>" 
                    + "<input name='name' value='" + 
                    customer.getName().replaceAll("'", "&#39;") 
                    + "'/></td></tr>");
            writer.println("<tr><td>City:</td><td>" 
                    + "<input name='city' value='" + 
                    customer.getCity().replaceAll("'", "&#39;") 
                    + "'/></td></tr>");
            writer.println("<tr>"
                    + "<td colspan='2' style='text-align:right'>" 
                    + "<input type='submit' value='Update'/></td>" 
                    + "</tr>");
            writer.println("<tr><td colspan='2'>"
                    + "<a href='customer'>Customer List</a>" 
                    + "</td></tr>");
            writer.println("</table>");
            writer.println("</form></body>");
        } else {
            writer.println("No customer found");
        }
        
    }
    @Override
    public void doGet(HttpServletRequest request, 
            HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.endsWith("/mycustomer")) {
            //jump to JSP
            sendCustomerList_jsp(request,response);
        }else if (uri.endsWith("/getcustomers")) {
            //return JSON
            sendCustomerList_ajax(request,response);
        }
        else if (uri.endsWith("/myeditCustomer")) {
            sendEditCustomerForm(request, response);
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, 
            HttpServletResponse response)
            throws ServletException, IOException {
        // update customer
        int customerId = 0;
        try {
            customerId = 
                    Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
        }
        Customer customer = getCustomer(customerId);
        if (customer != null) {
            customer.setName(request.getParameter("name"));
            customer.setCity(request.getParameter("city"));
        }
        sendCustomerList(response);
    }
}
