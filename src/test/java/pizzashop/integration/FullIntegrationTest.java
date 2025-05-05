package pizzashop.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FullIntegrationTest {

    private PizzaService service;
    private PaymentRepository paymentRepository;

    @Mock
    private MenuRepository mockMenuRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Use real repositories and service
        paymentRepository = new PaymentRepository() {
            private List<Payment> testPaymentList = new ArrayList<>();

            @Override
            public void add(Payment payment) {
                testPaymentList.add(payment);
                // Skip writing to file
            }

            @Override
            public List<Payment> getAll() {
                return new ArrayList<>(testPaymentList);
            }

            @Override
            public void writeAll() {
                // Skip writing to file
            }
        };
        service = new PizzaService(mockMenuRepo, paymentRepository);
    }

    @Test
    @DisplayName("Test full integration with Payment entity creation")
    void testFullIntegrationWithEntity() {
        // Arrange
        int tableNumber = 4;
        PaymentType type = PaymentType.Cash;
        double amount = 25.5;

        // Act - Create and add Payment entity
        service.addPayment(tableNumber, type, amount);

        // Assert
        List<Payment> payments = service.getPayments();
        assertEquals(1, payments.size(), "Should have one payment");

        Payment payment = payments.get(0);
        assertEquals(tableNumber, payment.getTableNumber(), "Table number should match");
        assertEquals(type, payment.getType(), "Type should match");
        assertEquals(amount, payment.getAmount(), "Amount should match");
    }

    @Test
    @DisplayName("Test full integration with multiple Payment entities")
    void testFullIntegrationWithMultipleEntities() {
        // Arrange & Act
        service.addPayment(1, PaymentType.Cash, 20.0);
        service.addPayment(2, PaymentType.Card, 35.5);
        service.addPayment(3, PaymentType.Cash, 15.0);

        // Assert
        List<Payment> payments = service.getPayments();
        assertEquals(3, payments.size(), "Should have three payments");

        double cashTotal = service.getTotalAmount(PaymentType.Cash);
        double cardTotal = service.getTotalAmount(PaymentType.Card);

        assertEquals(35.0, cashTotal, 0.001, "Cash total should be correct");
        assertEquals(35.5, cardTotal, 0.001, "Card total should be correct");
    }
}