package com.simeon.pizzaria_bella_napoli;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class homePageController {
    @FXML
    public TextField clientLastName;
    public TextField clientFirstName;
    public TextField clientLocation;
    public TextField clientStreet;
    public TextField clientZip;
    public CheckBox inCity;
    public ChoiceBox employeeList;
    public MenuButton pizzaMenu;
    public ListView orderedPizza;
    public TextArea infoBox;
    public Button saveButton;
    public TextArea statusBox;

    @FXML
    public void handleSaveBtnClick(){
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String url= "jdbc:sqlite:Database/database.db";
                String saveQuery = "INSERT INTO clients (lastName,firstName,location,street,zip) VALUES(?,?,?,?,?)";

                try (Connection conn = DriverManager.getConnection(url)){
                    PreparedStatement preparedStatement = conn.prepareStatement(saveQuery);
                    preparedStatement.setString(1,clientLastName.getText());
                    preparedStatement.setString(2,clientFirstName.getText());
                    preparedStatement.setString(3,clientLocation.getText());
                    preparedStatement.setString(4,clientStreet.getText());
                    preparedStatement.setString(5,clientZip.getText());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    statusBox.setText("Ihre Bestellung war erfolgereich gesendet.");
                } catch (SQLException e) {
                   statusBox.setText(e.toString());
                }
            }
        });
    }
}