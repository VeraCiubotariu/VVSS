package pizzashop.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceTest {
    private PizzaService service;
    private PaymentRepository paymentRepository;
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        menuRepository = new MenuRepository();
        paymentRepository = new PaymentRepository();
        service = new PizzaService(menuRepository, paymentRepository);
    }

    // ECP test cases
    @Test
    @DisplayName("ECP-Valid: Add payment with valid parameters")
    void testAddPaymentWithValidParameters() {
        // Arrange
        int tableNumber = 4;
        PaymentType type = PaymentType.Cash;
        double amount = 25.5;

        // Act
        service.addPayment(tableNumber, type, amount);

        // Assert
        List<Payment> payments = service.getPayments();
        boolean paymentFound = false;

        for (Payment payment : payments) {
            if (payment.getTableNumber() == tableNumber &&
                    payment.getType() == type &&
                    payment.getAmount() == amount) {
                paymentFound = true;
                break;
            }
        }

        assertTrue(paymentFound, "Payment should be added to the repository");
    }

    @Test
    @DisplayName("ECP-Invalid: Add payment with invalid table number (below range)")
    void testAddPaymentWithInvalidTableBelowRange() {
        // Arrange
        int invalidTable = 0;
        PaymentType type = PaymentType.Cash;
        double amount = 25.5;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(invalidTable, type, amount);
        });

        assertTrue(exception.getMessage().contains("Table number"),
                "Exception message should mention table number");
    }

    @Test
    @DisplayName("ECP-Invalid: Add payment with invalid amount (negative)")
    void testAddPaymentWithNegativeAmount() {
        // Arrange
        int tableNumber = 4;
        PaymentType type = PaymentType.Cash;
        double invalidAmount = -10.0;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(tableNumber, type, invalidAmount);
        });

        assertTrue(exception.getMessage().contains("amount"),
                "Exception message should mention amount");
    }

    // BVA test cases
    @Test
    @DisplayName("BVA-Valid: Add payment with table number at lower boundary (1)")
    void testAddPaymentWithTableAtLowerBoundary() {
        // Arrange
        int tableNumber = 1;
        PaymentType type = PaymentType.Card;
        double amount = 15.75;

        // Act
        service.addPayment(tableNumber, type, amount);

        // Assert
        List<Payment> payments = service.getPayments();
        boolean paymentFound = false;

        for (Payment payment : payments) {
            if (payment.getTableNumber() == tableNumber &&
                    payment.getType() == type &&
                    payment.getAmount() == amount) {
                paymentFound = true;
                break;
            }
        }

        assertTrue(paymentFound, "Payment with boundary table number should be added");
    }

    @Test
    @DisplayName("BVA-Valid: Add payment with table number at upper boundary (8)")
    void testAddPaymentWithTableAtUpperBoundary() {
        // Arrange
        int tableNumber = 8;
        PaymentType type = PaymentType.Cash;
        double amount = 42.0;

        // Act
        service.addPayment(tableNumber, type, amount);

        // Assert
        List<Payment> payments = service.getPayments();
        boolean paymentFound = false;

        for (Payment payment : payments) {
            if (payment.getTableNumber() == tableNumber &&
                    payment.getType() == type &&
                    payment.getAmount() == amount) {
                paymentFound = true;
                break;
            }
        }

        assertTrue(paymentFound, "Payment with boundary table number should be added");
    }

    @Test
    @DisplayName("BVA-Invalid: Add payment with table number just below lower boundary (0)")
    void testAddPaymentWithTableJustBelowLowerBoundary() {
        // Arrange
        int tableNumber = 0;
        PaymentType type = PaymentType.Card;
        double amount = 30.0;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(tableNumber, type, amount);
        });

        assertTrue(exception.getMessage().contains("Table number"),
                "Exception message should mention table number");
    }

    @Test
    @DisplayName("BVA-Invalid: Add payment with table number just above upper boundary (9)")
    void testAddPaymentWithTableJustAboveUpperBoundary() {
        // Arrange
        int tableNumber = 9;
        PaymentType type = PaymentType.Cash;
        double amount = 27.5;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(tableNumber, type, amount);
        });

        assertTrue(exception.getMessage().contains("Table number"),
                "Exception message should mention table number");
    }

    // Additional JUnit 5 features
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    @DisplayName("Parameterized test with valid table numbers")
    void testAddPaymentWithValidTableNumbers(int tableNumber) {
        // Arrange
        PaymentType type = PaymentType.Cash;
        double amount = 20.0;
        double initialTotalAmount = service.getTotalAmount(type);

        // Act
        service.addPayment(tableNumber, type, amount);

        // Assert
        double newTotalAmount = service.getTotalAmount(type);
        assertEquals(initialTotalAmount + amount, newTotalAmount, 0.001,
                "Total amount should increase by exactly the added payment amount");
    }

    @Test
    @Timeout(value = 100, unit = java.util.concurrent.TimeUnit.MILLISECONDS)
    @DisplayName("Performance test for addPayment")
    void testAddPaymentPerformance() {
        // Arrange
        int tableNumber = 3;
        PaymentType type = PaymentType.Card;
        double amount = 50.0;

        // Act
        service.addPayment(tableNumber, type, amount);

        // Assert is implicit with the @Timeout annotation
    }

    @RepeatedTest(3)
    @DisplayName("Repeated test for addPayment reliability")
    void repeatedTestAddPayment() {
        // Arrange
        int tableNumber = 5;
        PaymentType type = PaymentType.Cash;
        double amount = 35.75;
        double initialTotalAmount = service.getTotalAmount(type);

        // Act
        service.addPayment(tableNumber, type, amount);

        // Assert
        double newTotalAmount = service.getTotalAmount(type);
        assertEquals(initialTotalAmount + amount, newTotalAmount, 0.001,
                "Total amount should increase by exactly the added payment amount");
    }
}