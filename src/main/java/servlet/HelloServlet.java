package servlet;

import com.google.gson.*;
import password.Passwords;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Julian on 31/10/2017.
 */
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String customer_url = "http://project2-burgerx-database-api.herokuapp.com/customer/julian";

        URL url = new URL(customer_url);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        //Convert JSON object to access data
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser(); //json parser from gson library
        JsonElement root = jp.parse(new InputStreamReader((InputStream)request.getContent()));

        JsonObject rootobj = root.getAsJsonObject();

        String jsonString = gson.toJson(rootobj);

        System.out.println(jsonString);

        String string = "hello";
        String hash = Passwords.base64Encode(Passwords.hash(string.toCharArray(), Passwords.getNextSalt(16), Passwords.getNextNumIterations()));
        byte[] hashbytes = Passwords.base64Decode(hash);

        ServletOutputStream out = resp.getOutputStream();
        out.write("<h1>Under Construction</h1>".getBytes());
        out.write(jsonString.getBytes());
        out.flush();
        out.close();
    }

}
