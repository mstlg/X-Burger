package entity;

import com.google.gson.*;
import password.Passwords;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Julian on 5/11/2017.
 */
public class Customer {

    int customer_id;
    String username;
    String email;
    String phone_number;
    int iterations;
    String salt;
    String passHash;
    String passPin;
    String cardToken;
    static final String api_base_url = "http://project2-burgerx-database-api.herokuapp.com/customer/";

    // Constructor for Customer object with full suite of details as pulled from the database
    public Customer(int customer_id, String username, String email, String phone_number, int iterations, String salt, String passHash, String passPin, String cardToken) {
        this.customer_id = customer_id;
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.iterations = iterations;
        this.salt = salt;
        this.passHash = passHash;
        this.passPin = passPin;
        this.cardToken = cardToken;
    }

    // Constructor for "prospective" customer with full suite of details (minus customer_id which will be assigned on registration
    public Customer(String username, String email, String phone_number, int iterations, String salt, String passHash, String passPin, String cardToken) {
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.iterations = iterations;
        this.salt = salt;
        this.passHash = passHash;
        this.passPin = passPin;
        this.cardToken = cardToken;
    }

    // Get customer details by username from the API
    public static Customer getCustomerDetailsAPI (String username, String method) {
        String api_url = api_base_url + method + "/" + username;

        try {
            //Request the json resource at the specified url
            URL url = new URL(api_url);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            //Convert JSON object to access data
            JsonParser jp = new JsonParser(); //json parser from gson library
            JsonElement root = jp.parse(new InputStreamReader((InputStream)request.getContent()));
            JsonObject customer_object = root.getAsJsonObject();

            return new Customer(customer_object.get("Customer_ID").getAsInt(), customer_object.get("Username").getAsString(), customer_object.get("Email").getAsString(), customer_object.get("Phone_Number").getAsString(), customer_object.get("Iterations").getAsInt(), customer_object.get("Salt").getAsString(), customer_object.get("PassHash").getAsString(), customer_object.get("PassPin").getAsString(), customer_object.get("Card_Token").getAsString());

        } catch (MalformedURLException e) {
            //Not expected to be realised based on api_base_url setup
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean validateCustomerPassword(String password_entry, String hashDB, String saltDB, int iterationsDB) {

        //Use Passwords class to compare password_entry with hash from the database
        boolean password_validation = Passwords.isExpectedPassword(password_entry.toCharArray(), Passwords.base64Decode(saltDB), iterationsDB, Passwords.base64Decode(hashDB));

        return password_validation;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public int getIterations() {
        return iterations;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassHash() {
        return passHash;
    }

    public String getPassPin() {
        return passPin;
    }

    public String getCardToken() {
        return cardToken;
    }
}
