package com.controller;

import com.utils.DFA;
import com.utils.NFA;
import com.utils.RegularExpression;
import com.utils.ValidateExpression;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class controller {

    public Button testez;
    public TextField regExText;
    public TextField expText;

    String regEx = regExText.getText();
    String exp = expText.getText();


    // stores de NFA
    private static NFA nfa;

    // stores the DFA
    private static DFA dfa;

    public void onTest(ActionEvent actionEvent) {

        // Generate NFA using thompson algorithms with the Regular Expression
        setNfa (RegularExpression.generateNFA (regEx));

        // Generate DFA using the previous NFA and the Subset Construction Algorithm
        setDfa (RegularExpression.generateDFA (getNfa()));

        // Validate all the string with the DFA
        // yes = valid string
        // no = invalid string
        if (ValidateExpression.validate(getDfa(), exp))
        {
            System.out.println ("yes");
        }else
        {
            System.out.println ("no");
        }
        //testez.setOnAction(event -> onTest(actionEvent));
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
}
