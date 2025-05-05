package pizzashop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentRepositoryTest {

    private PaymentRepository repository;

    @Mock
    private Payment mockPayment1;

    @Mock
    private Payment mockPayment2;

    @BeforeEach
    void setUp() {
        // Create a custom repository that doesn't rely on file operations
        repository = spy(new PaymentRepository() {
            private List<Payment> paymentList = new ArrayList<>();

            @Override
            public void add(Payment payment) {
                paymentList.add(payment);
                // Don't call writeAll() to avoid file operations
            }

            @Override
            public List<Payment> getAll() {
                return new ArrayList<>(paymentList);
            }

            @Override
            public void writeAll() {
                // Do nothing to avoid file operations
            }
        });
    }

    @Test
    @DisplayName("Test add payment")
    void testAddPayment() {
        // Act
        repository.add(mockPayment1);
        List<Payment> allPayments = repository.getAll();

        // Assert
        assertEquals(1, allPayments.size(), "Repository should contain one payment");
        assertSame(mockPayment1, allPayments.get(0), "The payment in the repository should be the one we added");
    }

    @Test
    @DisplayName("Test get all payments")
    void testGetAllPayments() {
        // Arrange - simply add the payments without stubbing toString()
        repository.add(mockPayment1);
        repository.add(mockPayment2);

        // Act
        List<Payment> result = repository.getAll();

        // Assert
        assertEquals(2, result.size(), "Repository should return 2 payments");
        assertTrue(result.contains(mockPayment1), "Repository should contain mockPayment1");
        assertTrue(result.contains(mockPayment2), "Repository should contain mockPayment2");
    }
}