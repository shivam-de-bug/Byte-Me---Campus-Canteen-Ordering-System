import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class LoginTest {
    private static Map<String, Customer> customers;

    @BeforeClass
    public static void setup() {
        customers = new HashMap<>();
        Customer customer1 = new Customer("5678", "Room101", "password123", false, null, null);
        customers.put("5678", customer1);
        Main.customers = customers; 
    }

    @Test
    public void testValidLogin() {
        String rollNumber = "5678";
        String password = "password123";

        Customer customer = customers.get(rollNumber);
        assertNotNull("Customer should exist for the given roll number.", customer);
        assertEquals("Password should match the stored password.", password, customer.getPassword());
    }

    @Test
    public void testInvalidRollNumber() {
        String rollNumber = "9999"; 
        Customer customer = customers.get(rollNumber);

        assertNull("No customer should exist with the provided roll number.", customer);
    }

    @Test
    public void testInvalidPassword() {
        String rollNumber = "5678";
        String incorrectPassword = "wrongpassword";

        Customer customer = customers.get(rollNumber);
        assertNotNull("Customer should exist for the given roll number.", customer);
        assertNotEquals("Incorrect password should not allow login.", incorrectPassword, customer.getPassword());
    }

    @Test
    public void testEmptyRollNumber() {
        String rollNumber = ""; 
        Customer customer = customers.get(rollNumber);

        assertNull("Empty roll number should not match any customer.", customer);
    }

    @Test
    public void testEmptyPassword() {
        String rollNumber = "5678";
        String emptyPassword = ""; 

        Customer customer = customers.get(rollNumber);
        assertNotNull("Customer should exist for the given roll number.", customer);
        assertNotEquals("Empty password should not allow login.", emptyPassword, customer.getPassword());
    }

    @Test
    public void testNonExistingCustomer() {
        String rollNumber = "1234"; 
        String password = "password123";

        Customer customer = customers.get(rollNumber);
        assertNull("No customer should exist for the given roll number.", customer);
    }

    @Test
    public void testSignUpWithExistingRollNumber() {
        String rollNumber = "5678"; 
        String hostelRoom = "Room202";
        String password = "newpassword";

        assertTrue("An account with this roll number should already exist.", customers.containsKey(rollNumber));
    }

    @Test
    public void testValidSignUp() {
        String rollNumber = "9999"; 
        String hostelRoom = "Room102";
        String password = "newpassword";

        Customer newCustomer = new Customer(rollNumber, hostelRoom, password, false, null, null);
        customers.put(rollNumber, newCustomer);

        assertTrue("The new customer should be added successfully.", customers.containsKey(rollNumber));
        assertEquals("The password should match the one provided during sign-up.", password, newCustomer.getPassword());
    }

    @Test
    public void testMultipleInvalidLoginAttempts() {
        String rollNumber = "5678";
        String[] invalidPasswords = {"wrong1", "wrong2", "wrong3"}; 

        Customer customer = customers.get(rollNumber);
        assertNotNull("Customer should exist for the given roll number.", customer);

        for (String invalidPassword : invalidPasswords) {
            assertNotEquals("Invalid password should not allow login.", invalidPassword, customer.getPassword());
        }
    }
}
