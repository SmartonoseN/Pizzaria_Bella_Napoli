package com.simeon.pizzaria_bella_napoli;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
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

    @FXML
    private void initialize() {
        getPizza();
        getEmployee();
    }

    @FXML
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

        });
    }

    @FXML
    public void handlePizzaMenu() throws SQLException {
        pizzaMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String pizza = pizzaMenu.getValue().toString();
                orderedPizza.getItems().add(pizza);
            }
        });
    }


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
                pizzas.add(rs.getString("name"));
            }
            for (int i = 0; i < pizzas.size(); i++) {
                pizzaMenu.getItems().add(pizzas.get(i));
            }
        } catch (SQLException e) {
            statusBox.setText(e.toString());
        }

    }

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
