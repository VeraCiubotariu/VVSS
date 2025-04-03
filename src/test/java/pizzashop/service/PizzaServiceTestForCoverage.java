package pizzashop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceTestForCoverage {

    private PizzaService service;
    private PaymentRepository paymentRepository;
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        menuRepository = new MenuRepository();
        paymentRepository = new PaymentRepository();
        service = new PizzaService(menuRepository, paymentRepository);
    }

    @Test
    void getMenuData() {
        List<MenuDataModel> res = service.getMenuData();
        assertFalse(res.isEmpty());
    }

    @Test
    void getPayments() {
        List<Payment> res = service.getPayments();
        assertFalse(res.isEmpty());
    }

    @Test
    void addPayment() {
        int tableNumber = 4;
        PaymentType type = PaymentType.Cash;
        double amount = 25.5;
        service.addPayment(tableNumber, type, amount);
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
        assertTrue(paymentFound);

        Exception exceptionA = assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(9, PaymentType.Cash, 25.5);
        });
        assertTrue(exceptionA.getMessage().contains("Table number"));

        Exception exceptionB = assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(8, null, 22.3);
        });
        assertTrue(exceptionB.getMessage().contains("Payment type"));

        Exception exceptionC = assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(8, PaymentType.Cash, -10.0);
        });
        assertTrue(exceptionC.getMessage().contains("amount"),
                "Exception message should mention amount");
    }

    @Test
    void getTotalAmount() {
        double res1 = service.getTotalAmount(PaymentType.Cash);
        double res2 = service.getTotalAmount(PaymentType.Card);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getTotalAmount(null);
        });
        assertTrue(exception.getMessage().contains("Invalid payment type"));
    }
}