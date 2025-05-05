package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaServiceMockTest {

    private PizzaService service;

    @Mock
    private MenuRepository mockMenuRepo;

    @Mock
    private PaymentRepository mockPayRepo;

    @Mock
    private Payment mockPayment1;

    @Mock
    private Payment mockPayment2;

    @BeforeEach
    void setUp() {
        service = new PizzaService(mockMenuRepo, mockPayRepo);
    }

    @Test
    @DisplayName("Test getPayments")
    void testGetPayments() {
        // Arrange
        List<Payment> mockPayments = Arrays.asList(mockPayment1, mockPayment2);
        when(mockPayRepo.getAll()).thenReturn(mockPayments);

        // Act
        List<Payment> result = service.getPayments();

        // Assert
        assertEquals(mockPayments, result, "Should return payments from repository");
        verify(mockPayRepo, times(1)).getAll();
    }

    @Test
    @DisplayName("Test addPayment with valid parameters")
    void testAddPaymentWithValidParameters() {
        // Arrange
        int tableNumber = 3;
        PaymentType type = PaymentType.Cash;
        double amount = 25.0;

        // Act
        service.addPayment(tableNumber, type, amount);

        // Assert
        verify(mockPayRepo, times(1)).add(any(Payment.class));
    }
}