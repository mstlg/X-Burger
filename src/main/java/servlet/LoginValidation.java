package servlet;

import entity.Customer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Julian on 5/11/2017.
 */
public class LoginValidation extends HttpServlet {

    // If this servlet is accessed via HTTP GET: return the user to the login page (.jsp)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    // This servlet should only be accessed via post. If posted, access the customers details from the API and validate credentials and return response depending on validation process
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Customer customer_login_submission = null;

        //Get request parameters from form submission
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //Check whether user has input a username or an email and access details from the API
        if (username.length() > 0) {
            customer_login_submission = Customer.getCustomerDetailsAPI(username, "username");
        } else if (email.length() > 0) {
            customer_login_submission = Customer.getCustomerDetailsAPI(email, "email");
        }

        ServletOutputStream out = response.getOutputStream();

        if (customer_login_submission != null) {
            if (customer_login_submission.validateCustomerPassword(password, customer_login_submission.getPassHash(), customer_login_submission.getSalt(), customer_login_submission.getIterations())) {
                out.write("<h1>Login Sucessful</h1>".getBytes());
            } else {
                out.write("<h1>Incorrect Password</h1>".getBytes());
            }
        }

        out.write("<h1>Under Construction</h1>".getBytes());
        out.flush();
        out.close();

    }
}
