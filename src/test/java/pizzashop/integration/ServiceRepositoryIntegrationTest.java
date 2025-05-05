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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceRepositoryIntegrationTest {

    private PizzaService service;
    private PaymentRepository paymentRepository;

    @Mock
    private MenuRepository mockMenuRepo;

    @Mock
    private Payment mockPayment1;

    @Mock
    private Payment mockPayment2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Use a real repository with custom implementation for testing
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

        // Create service with real repository and mock menu repo
        service = new PizzaService(mockMenuRepo, paymentRepository);
    }

    @Test
    @DisplayName("Test add payment integration with repository")
    void testAddPaymentIntegration() {
        // Arrange
        int tableNumber = 5;
        PaymentType type = PaymentType.Card;
        double amount = 42.50;

        // Act
        service.addPayment(tableNumber, type, amount);
        List<Payment> payments = service.getPayments();

        // Assert
        assertEquals(1, payments.size(), "Repository should have one payment");
        Payment addedPayment = payments.get(0);
        assertEquals(tableNumber, addedPayment.getTableNumber(), "Payment should have correct table number");
        assertEquals(type, addedPayment.getType(), "Payment should have correct type");
        assertEquals(amount, addedPayment.getAmount(), "Payment should have correct amount");
    }

    @Test
    @DisplayName("Test getTotalAmount integration with repository")
    void testGetTotalAmountIntegration() {
        // Arrange
        service.addPayment(1, PaymentType.Cash, 15.0);
        service.addPayment(2, PaymentType.Cash, 25.0);
        service.addPayment(3, PaymentType.Card, 30.0);

        // Act
        double cashTotal = service.getTotalAmount(PaymentType.Cash);
        double cardTotal = service.getTotalAmount(PaymentType.Card);

        // Assert
        assertEquals(40.0, cashTotal, 0.001, "Cash total should be correct");
        assertEquals(30.0, cardTotal, 0.001, "Card total should be correct");
    }


    @Test
    @DisplayName("Test explicit mocking of Payment entity")
    void testExplicitMockingOfPaymentEntity() {
        // Setup mock Payment objects
        when(mockPayment1.getTableNumber()).thenReturn(1);
        when(mockPayment1.getType()).thenReturn(PaymentType.Cash);
        when(mockPayment1.getAmount()).thenReturn(15.0);

        when(mockPayment2.getTableNumber()).thenReturn(2);
        when(mockPayment2.getType()).thenReturn(PaymentType.Card);
        when(mockPayment2.getAmount()).thenReturn(25.5);

        // Add mock payments to repository
        paymentRepository.add(mockPayment1);
        paymentRepository.add(mockPayment2);

        // Test service methods that use repository
        List<Payment> payments = service.getPayments();
        double cashTotal = service.getTotalAmount(PaymentType.Cash);
        double cardTotal = service.getTotalAmount(PaymentType.Card);

        // Verify correct integration of service and repository
        assertEquals(2, payments.size(), "Repository should return both payments");
        assertTrue(payments.contains(mockPayment1), "Repository should contain first payment");
        assertTrue(payments.contains(mockPayment2), "Repository should contain second payment");
        assertEquals(15.0, cashTotal, 0.001, "Cash total should be calculated correctly");
        assertEquals(25.5, cardTotal, 0.001, "Card total should be calculated correctly");

        // Verify the mock was used properly
        verify(mockPayment1, atLeastOnce()).getType();
        verify(mockPayment1, atLeastOnce()).getAmount();
        verify(mockPayment2, atLeastOnce()).getType();
        verify(mockPayment2, atLeastOnce()).getAmount();
    }

    @Test
    @DisplayName("Test service repository integration with mocked entities")
    void testServiceRepositoryIntegrationWithMockedEntities() {
        // Create a mock Payment to be returned by service method
        Payment mockPayment = mock(Payment.class);

        // Configure repository spy to return our mock when needed
        PaymentRepository repositorySpy = spy(paymentRepository);
        when(repositorySpy.getAll()).thenReturn(Arrays.asList(mockPayment));

        // Create service with spied repository
        PizzaService serviceWithSpiedRepo = new PizzaService(mockMenuRepo, repositorySpy);

        // Test service methods
        List<Payment> payments = serviceWithSpiedRepo.getPayments();

        // Verify correct integration
        assertEquals(1, payments.size(), "Service should return the payment from repository");
        assertSame(mockPayment, payments.get(0), "Service should return exactly the same mock object");

        // Verify repository method was called by service
        verify(repositorySpy, times(1)).getAll();
    }

}