package pizzashop.service;

import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.List;

public class PizzaService {

    private MenuRepository menuRepo;
    private PaymentRepository payRepo;

    public PizzaService(MenuRepository menuRepo, PaymentRepository payRepo){
        this.menuRepo=menuRepo;
        this.payRepo=payRepo;
    }

    public List<MenuDataModel> getMenuData(){return menuRepo.getMenu();}

    public List<Payment> getPayments(){return payRepo.getAll(); }

    public void addPayment(int table, PaymentType type, double amount){
        // Validate table number
        if (table < 1 || table > 8) {
            throw new IllegalArgumentException("Table number must be between 1 and 8");
        }

        // Validate payment type
        if (type == null) {
            throw new IllegalArgumentException("Payment type cannot be null");
        }

        // Validate amount
        if (amount < 0) {
            throw new IllegalArgumentException("Payment amount cannot be negative");
        }

        Payment payment = new Payment(table, type, amount);
        payRepo.add(payment);
    }

    public double getTotalAmount(PaymentType type) {
        if (type != PaymentType.Cash && type != PaymentType.Card) {
            throw new IllegalArgumentException("Invalid payment type");
        }
        double total=0.0f;
        List<Payment> payments=getPayments();
        if ((payments==null) ||(payments.isEmpty())) {
            return total;
        }
        for (Payment payment:payments){
            if (payment.getType().equals(type))
                total+=payment.getAmount();
        }
        return total;
    }

}