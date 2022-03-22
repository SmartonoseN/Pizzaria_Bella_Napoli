package com.simeon.pizzaria_bella_napoli;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;


//TODO Select pizza and add it to the list view, after it has been added, should show the price
//TODO If a user want to remove pizza from his order, is should be possible
//TODO User can specify how many pizza quantity he wants. For example Margaritha, 2.... and this should be calculated in the total price
//TODO have multiple check boxes which can change the price any moment, that means the price variable should be global variable
//TODO Search function which a employee can use to search a client in the database, after the search a second window/ popup should show the result from the query
//TODO If a client has be found, should be selectable and the user can edit it's data

public class homePageController {
    @FXML


    public TextField orderIdTextField;
    public TextField clientLastName;
    public TextField clientFirstName;
    public TextField clientLocation;
    public TextField clientStreet;
    public TextField clientZip;
    public TextField priceTextField;
    public CheckBox inCity;
    public ComboBox employeeMenu;
    public ComboBox pizzaMenu;
    public ListView orderedPizza;
    public TextArea infoBox;
    public Button saveButton;
    public TextArea statusBox;
    double price = 0.0;

    @FXML
    private void initialize(){
        getPizza();
        getEmployee();
    }

    @FXML
    //Programmierung der "Speichern" Knopf
    public void handleSaveBtnClick() {
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String url = "jdbc:sqlite:Database/database.db";
                String saveQuery = "INSERT INTO clients (lastName,firstName,location,street,zip) VALUES(?,?,?,?,?)";

                try (Connection conn = DriverManager.getConnection(url)) {
                    PreparedStatement preparedStatement = conn.prepareStatement(saveQuery);
                    preparedStatement.setString(1, clientLastName.getText());
                    preparedStatement.setString(2, clientFirstName.getText());
                    preparedStatement.setString(3, clientLocation.getText());
                    preparedStatement.setString(4, clientStreet.getText());
                    preparedStatement.setString(5, clientZip.getText());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    statusBox.setText("Ihre Bestellung war erfolgereich gesendet.");
                } catch (SQLException e) {
                    statusBox.setText(e.toString());
                }
            }

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            public void handleOrder(ActionEvent actionEvent) {                      //Speicherung der Bestellung
                String url = "jdbc:sqlite:Database/database.db";
                String saveQuery = "INSERT INTO orders (id,pizzaID,totalSum,employeeID,time,inCity) VALUES(?,?,?,?,?,?)";
                String inCityBoolean;
                String pizzaID = null;

                if (inCity.isSelected()) {                                           //Boolean für InCity Button.
                    inCityBoolean = "1";
                } else {
                    inCityBoolean = "0";
                }

                try (Connection conn = DriverManager.getConnection(url)) {
                    PreparedStatement preparedStatement = conn.prepareStatement(saveQuery);
                    preparedStatement.setString(1, orderIdTextField.getText());
                    preparedStatement.setString(2, pizzaID);
                    preparedStatement.setString(3, priceTextField.getText());
                    preparedStatement.setString(4, employeeMenu.getValue().toString());
                    preparedStatement.setString(5, timestamp.toString());
                    preparedStatement.setString(6, inCityBoolean);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    statusBox.setText("Ihre Bestellung war erfolgereich gespeichert.");

                } catch (SQLException e) {
                    statusBox.setText(e.toString());
                }
            }
        });
    }

    public String generateOrderID() throws SQLException {
        String url = "jdbc:sqlite:Database/database.db";
        String orderID = "";
        Integer tempID;
        String getIDQuery = "SELECT id FROM orders";
        Connection conn = DriverManager.getConnection(url);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getIDQuery);
            orderID = rs.getString("id");

            if (orderID.isEmpty()) {
                orderID = "1";
            }else{
                while (rs.next()){
                    rs.last();
                    tempID = Integer.parseInt(orderID) + 1;
                    orderID = Integer.toString(tempID);
                }
            }
        } catch (Exception e) {
            statusBox.setText(e.toString());
        }
        return orderID;
    }

    @FXML
    //Programmierung der "In Stadt" Checkbox
    public void handleCheckBoxClick() {
        inCity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                while (inCity.isSelected()) {
                    if (price >= 10.00) {                            //Stadt Mindestbestellwert 10 €
                        price = price + 1;                          //Stadt 1 € Liefergebühr
                    }
                }
                while (inCity.isSelected() == false) {
                    if (price >= 20.00) {                           //Land Mindestbestellwert 20 €
                        price = price + 2;                          //Land 2 € Liefergebühr
                    }
                }
            }
        });
    }

    @FXML
    //Programmierung der Pizza Menu
    public void handlePizzaMenu() throws SQLException {
        pizzaMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String pizza = pizzaMenu.getValue().toString();
                orderedPizza.getItems().add(pizza);
                try {
                    orderIdTextField.setText(generateOrderID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                calculateOrderSum();
            }
        });
    }
    //TODO calculate the tax of the price for one pizza and then add it together
    //TODO convert to 2 numbers after decimal

    //Programmierung der Berechnung der Summe
    public void calculateOrderSum() {
        if (orderedPizza.getItems().isEmpty() == false) {

            if (orderedPizza.getItems().size() >= 2) {
                for (int i = 0; i < orderedPizza.getItems().size(); i++) {
                    String pizza = orderedPizza.getItems().toString();
                    String[] sum = pizza.split(" ");
                    price = Double.parseDouble(sum[1]) + price; //String aufgeteilt, um das zweite Teil (bzw. Preis) zu nehmen
                }
                price = price * 1.07;
                double roundedPrice = Math.round((price * 100.00 / 100.00));
                String.format("%.3f", roundedPrice);
                String textPrice = Double.toString(roundedPrice);
                priceTextField.setText(textPrice);
            } else {
                String pizza = orderedPizza.getItems().toString();
                String[] sum = pizza.split(" ");
                price = Double.parseDouble(sum[1]) * 1.07;

                double roundedPrice = Math.round((price * 100.00 / 100.00));
                String.format("%.3f", roundedPrice);
                String textPrice = Double.toString(roundedPrice);
                priceTextField.setText(textPrice);

            }
        }
    }

    //Programmierung der Verbindung mit Database Pizza Table
    public void getPizza() {
        String url = "jdbc:sqlite:Database/database.db";
        String getPizza = "SELECT * FROM pizza";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getPizza);
            List<String> pizzas = new LinkedList<>();

            while (rs.next()) {
                pizzas.add(rs.getString("name") + " " + rs.getString("price") + " €");
            }
            for (int i = 0; i < pizzas.size(); i++) {
                pizzaMenu.getItems().add(pizzas.get(i));
            }
        } catch (SQLException e) {
            statusBox.setText(e.toString()); //Falls Fehler vorhanden - wird in Status Box angezeigt
        }
    }


    //Programmierung der Verbindung mit Database Employee (Mitarbeiter) Table
    public void getEmployee() {
        String url = "jdbc:sqlite:Database/database.db";
        String getEmployee = "SELECT * FROM employee";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getEmployee);
            List<String> employee = new LinkedList<>();

            while (rs.next()) {
                employee.add(rs.getString("firstName") + " " + rs.getString("lastName"));
            }
            for (int i = 0; i < employee.size(); i++) {
                employeeMenu.getItems().add(employee.get(i));
            }

        } catch (SQLException e) {
            statusBox.setText(e.toString());
        }
    }

}
