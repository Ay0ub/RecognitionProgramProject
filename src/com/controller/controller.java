package com.controller;

import com.utils.DFA;
import com.utils.NFA;
import com.utils.RegularExpression;
import com.utils.ValidateExpression;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class controller {

    public Button testez;
    public TextField regExText;
    public TextField expText;


    //enregistre  AFN
    private static NFA nfa;

    //enregistre  AFD
    private static DFA dfa;
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
            setNfa (RegularExpression.generateNFA (regEx));

            // Generer AFD  en utilisant AFN
            setDfa (RegularExpression.generateDFA (getNfa()));

            // Valider String avec AFD

            if (ValidateExpression.validate(getDfa(), exp)) {
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
    public static NFA getNfa() {
        return nfa;
    }

    public static void setNfa(NFA nfa) {
        controller.nfa = nfa;
    }

    public static DFA getDfa() {
        return dfa;
    }

    public static void setDfa(DFA dfa) {
        controller.dfa = dfa;
    }

    public void onClick(MouseEvent mouseEvent) {
    }

    public void onQuitter(ActionEvent actionEvent) {
        System.exit(0);
    }
}
