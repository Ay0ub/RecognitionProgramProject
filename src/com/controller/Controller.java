package com.controller;

import com.utils.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller {

    public Button testez;
    public TextField regExText;
    public TextField expText;


    //enregistre  AFN
    private static AFND afnd;

    //enregistre  AFD
    private static AFD afd;
    public Button quitter;

    public void onTest(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Alert errorAlert = new Alert(Alert.AlertType.WARNING);

        String regEx = regExText.getText();
        String exp = expText.getText();

        if(regEx.isEmpty() || exp.isEmpty())
        {
            errorAlert.setHeaderText("Un des champs est vide");
            errorAlert.setContentText("Veuillez remplire tous les champs");
            errorAlert.showAndWait();
        }else
        {
            // Generer AFN en utilisant thompson algorithms avec  Expression Reguliere
            setNfa (RegularExpressionToAFND.generateAFND (regEx));

            // Generer AFD  en utilisant AFN
            setAfd (AfndToAfd.generateAFD (getNfa()));

            // Valider String avec AFD

            if (ValidateExpression.validate(getAfd(), exp)) {
                alert.setHeaderText("Mot reconnu");
                alert.showAndWait();
            }else
            {
                alert.setHeaderText("Mot non reconnu");
                alert.showAndWait();
            }
        }
    }

    // Getters and Setters
    public static AFND getNfa() {
        return afnd;
    }

    public static void setNfa(AFND afnd) {
        Controller.afnd = afnd;
    }

    public static AFD getAfd() {
        return afd;
    }

    public static void setAfd(AFD afd) {
        Controller.afd = afd;
    }

    public void onClick(MouseEvent mouseEvent) {
    }

    public void onQuitter(ActionEvent actionEvent) {
        System.exit(0);
    }
}
