package pizzashop.gui;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pizzashop.controller.OrdersGUIController;
import pizzashop.service.PizzaService;

import java.io.IOException;

public class OrdersGUI {
    protected int tableNumber;
    public int getTableNumber() {
        return tableNumber;
    }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }
    private PizzaService service;

    public void setService(PizzaService service) {
        this.service = service;
    }

    public void displayOrdersForm(){
     VBox vBoxOrders = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OrdersGUIFXML.fxml"));

            vBoxOrders = loader.load();
            OrdersGUIController ordersCtrl= loader.getController();
            ordersCtrl.setService(service, tableNumber);

        } catch (IOException e) {
            e.printStackTrace();
        }

     Stage stage = new Stage();
     stage.setTitle("Table"+getTableNumber()+" order form");
     stage.setResizable(false);
     // disable X on the window
        // consume event
        stage.setOnCloseRequest(Event::consume);
     stage.setScene(new Scene(vBoxOrders));
     stage.show();
    }
}