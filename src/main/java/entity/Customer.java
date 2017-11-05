package entity;

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

    // Constructor for Customer object with full suite of details as pulled from the database
    public Customer(int customer_id, String username, String email, String phone_number, int iterations, String salt) {
        this.customer_id = customer_id;
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.iterations = iterations;
        this.salt = salt;
    }

    // Constructor for "prospective" customer with full suite of details (minus customer_id which will be assigned on registration
    public Customer(String username, String email, String phone_number, int iterations, String salt) {
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.iterations = iterations;
        this.salt = salt;
    }

    public static Customer getCustomerDetailsAPI (String username) {
        return null;
    }



}
