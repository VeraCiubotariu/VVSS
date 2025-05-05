package pizzashop.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    @DisplayName("Test Payment constructor and getters")
    void testConstructorAndGetters() {
        // Arrange
        int tableNumber = 5;
        PaymentType type = PaymentType.Card;
        double amount = 42.50;

        // Act
        Payment payment = new Payment(tableNumber, type, amount);

        // Assert
        assertEquals(tableNumber, payment.getTableNumber(), "Table number should match constructor argument");
        assertEquals(type, payment.getType(), "Payment type should match constructor argument");
        assertEquals(amount, payment.getAmount(), "Amount should match constructor argument");
    }

    @Test
    @DisplayName("Test Payment setters")
    void testSetters() {
        // Arrange
        Payment payment = new Payment(1, PaymentType.Cash, 10.0);
        int newTableNumber = 7;
        PaymentType newType = PaymentType.Card;
        double newAmount = 35.75;

        // Act
        payment.setTableNumber(newTableNumber);
        payment.setType(newType);
        payment.setAmount(newAmount);

        // Assert
        assertEquals(newTableNumber, payment.getTableNumber(), "Table number should be updated");
        assertEquals(newType, payment.getType(), "Payment type should be updated");
        assertEquals(newAmount, payment.getAmount(), "Amount should be updated");
    }

    @Test
    @DisplayName("Test Payment toString method")
    void testToString() {
        // Arrange
        int tableNumber = 3;
        PaymentType type = PaymentType.Cash;
        double amount = 25.0;
        Payment payment = new Payment(tableNumber, type, amount);

        // Act
        String result = payment.toString();

        // Assert
        assertEquals("3,Cash,25.0", result, "toString should return formatted string");
    }
}