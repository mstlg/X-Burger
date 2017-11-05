package servlet;

import entity.Customer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Julian on 5/11/2017.
 */
public class LoginValidation extends HttpServlet {

    // If this servlet is accessed via HTTP GET: return the user to the login page (.jsp)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loginStatusRedirection(request, response);
    }

    // This servlet should only be accessed via post. If posted, access the customers details from the API and validate credentials and return response depending on validation process
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Customer customer_login_submission = null;

        //Get request parameters from form submission
        String username_entry = request.getParameter("username");
        String email_entry = request.getParameter("email");
        String password_entry = request.getParameter("password");

        //Check whether user has input a username or an email and access details from the API
        if (username_entry.length() > 0) {
            customer_login_submission = Customer.getCustomerDetailsAPI(username_entry, "username");
        } else if (email_entry.length() > 0) {
            customer_login_submission = Customer.getCustomerDetailsAPI(email_entry, "email");
        }

        ServletOutputStream out = response.getOutputStream();

        if (customer_login_submission != null) {
            if (customer_login_submission.validateCustomerPassword(password_entry, customer_login_submission.getPassHash(), customer_login_submission.getSalt(), customer_login_submission.getIterations())) {

               //Login username and password verified
               //Create user session and setMaxInactiveInterval to 12 hours. Also store the user details and login status variables in the session
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(60 * 60 * 12);
                session.setAttribute("loginStatus", "active");
                session.setAttribute("customer", customer_login_submission);

                //REPLACE WITH REDIRECT TO APPROPRIATE JSP
                out.write("<h1>Login Sucessful</h1>".getBytes());

            } else {
                response.sendRedirect("Login?loginStatus=incorrectPassword&username=" + username_entry);
            }
        } else {
            response.sendRedirect("Login?loginStatus=invalidUsername");
        }

        out.flush();
        out.close();

    }

    /*Method to check a user's login-status and redirect accordingly
   (this method is used across most user-sensitive servlets to confirm their login status)*/
    public static void loginStatusRedirection(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute("loginStatus") != "active"){
            response.sendRedirect("login");
        } else {
            Customer customer = (Customer) session.getAttribute("customer");
            //REPLACE WITH REDIRECT TO APPROPRIATE JSP
            ServletOutputStream out = response.getOutputStream();
            out.write("<h1>Already Logged In</h1>".getBytes());
            out.flush();
            out.close();
        }
    }
}
