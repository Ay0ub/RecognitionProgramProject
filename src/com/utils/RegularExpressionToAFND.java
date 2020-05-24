package com.utils;

import javafx.scene.control.Alert;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class RegularExpressionToAFND {
    public static Stack<Character> operateur = new Stack<>();

    public static AFND generateAFND(String regular) {
        // Generere regular expression avec la  concatenation
        regular = AddConcat (regular);

        // Only RegEx.inputs available
        RegEx.input.add('a');
        RegEx.input.add('b');
        RegEx.input.add('c');
        RegEx.input.add('d');
        RegEx.input.add('e');
        RegEx.input.add('f');
        RegEx.input.add('j');
        RegEx.input.add('h');
        RegEx.input.add('i');
        RegEx.input.add('g');
        RegEx.input.add('k');
        RegEx.input.add('l');
        RegEx.input.add('m');
        RegEx.input.add('n');
        RegEx.input.add('o');
        RegEx.input.add('p');
        RegEx.input.add('q');
        RegEx.input.add('r');
        RegEx.input.add('s');
        RegEx.input.add('t');
        RegEx.input.add('u');
        RegEx.input.add('v');
        RegEx.input.add('w');
        RegEx.input.add('x');
        RegEx.input.add('y');
        RegEx.input.add('z');

        // Cleaning stacks
        RegEx.stackAfnd.clear();
        operateur.clear();

        for (int i = 0 ; i < regular.length(); i++) {

            if (isInputCharacter (regular.charAt(i))) {
                pushStack(regular.charAt(i));

            } else if (operateur.isEmpty()) {
                operateur.push(regular.charAt(i));

            } else if (regular.charAt(i) == '(') {
                operateur.push(regular.charAt(i));

            } else if (regular.charAt(i) == ')') {
                while (operateur.get(operateur.size()-1) != '(') {
                    doOperation();
                }

                // Pop the '(' left parenthesis
                operateur.pop();

            } else {
                while (!operateur.isEmpty() &&
                        Priority (regular.charAt(i), operateur.get(operateur.size() - 1)) ){
                    doOperation ();
                }
                operateur.push(regular.charAt(i));
            }
        }

        // effacer elements restant dans la pile
        while (!operateur.isEmpty()) {	doOperation(); }

        // Get AFN
        AFND completeAfnd = RegEx.stackAfnd.pop();

        // add the accpeting RegEx.etat to the end of AFND
        completeAfnd.getAfnd().get(completeAfnd.getAfnd().size() - 1).setAcceptEtat(true);

        // return the nfa
        return completeAfnd;
    }

    private static String AddConcat(String regular) {
        String newRegular = new String ("");

        for (int i = 0; i < regular.length() - 1; i++) {
            if ( isInputCharacter(regular.charAt(i))  && isInputCharacter(regular.charAt(i+1)) ) {
                newRegular += regular.charAt(i) + ".";

            } else if ( isInputCharacter(regular.charAt(i)) && regular.charAt(i+1) == '(' ) {
                newRegular += regular.charAt(i) + ".";

            } else if ( regular.charAt(i) == ')' && isInputCharacter(regular.charAt(i+1)) ) {
                newRegular += regular.charAt(i) + ".";

            } else if (regular.charAt(i) == '*'  && isInputCharacter(regular.charAt(i+1)) ) {
                newRegular += regular.charAt(i) + ".";

            } else if ( regular.charAt(i) == '*' && regular.charAt(i+1) == '(' ) {
                newRegular += regular.charAt(i) + ".";

            } else if ( regular.charAt(i) == ')' && regular.charAt(i+1) == '(') {
                newRegular += regular.charAt(i) + ".";

            } else {
                newRegular += regular.charAt(i);
            }
        }
        newRegular += regular.charAt(regular.length() - 1);
        return newRegular;
    }

    private static boolean isInputCharacter(char charAt) {
        return switch (charAt) {
            case 'a' -> true;
            case 'b' -> true;
            case 'e' -> true;
            case 'c' -> true;
            case 'd' -> true;
            case 'f' -> true;
            case 'g' -> true;
            case 'h' -> true;
            case 'i' -> true;
            case 'j' -> true;
            case 'k' -> true;
            case 'l' -> true;
            case 'm' -> true;
            case 'n' -> true;
            case 'o' -> true;
            case 'p' -> true;
            case 'q' -> true;
            case 'r' -> true;
            case 's' -> true;
            case 't' -> true;
            case 'u' -> true;
            case 'v' -> true;
            case 'x' -> true;
            case 'y' -> true;
            case 'z' -> true;
            default -> false;
        };
    }

    private static void pushStack(char symbol) {
        Etat s0 = new Etat(RegEx.etatID++);
        Etat s1 = new Etat(RegEx.etatID++);

        // ajouter transition from 0 to 1 with the symbol
        s0.addTransition(s1, symbol);

        // new temporary AFN
        AFND nfa = new AFND();

        // Ajouter RegEx.etats a AFN
        nfa.getAfnd().addLast(s0);
        nfa.getAfnd().addLast(s1);

        // Ajouter nfa a la fin de la pile RegEx.stackAfnd
        RegEx.stackAfnd.push(nfa);
    }

    private static void doOperation () {
        Alert errorAlert = new Alert(Alert.AlertType.WARNING);
        if (RegularExpressionToAFND.operateur.size() > 0) {
            char charAt = operateur.pop();

            switch (charAt) {
                case ('|'):
                    union ();
                    break;

                case ('.'):
                    concatenation ();
                    break;

                case ('*'):
                    star ();
                    break;

                default :
                    System.out.println("Unkown Symbol !");
                    //System.exit(1);
                    //errorAlert.setHeaderText("Symbole inconnue !!");
                    //errorAlert.showAndWait();
                    break;
            }
        }
    }

    private static boolean Priority (char first, Character second) {
        if(first == second) {	return true;	}
        if(first == '*') 	{	return false;	}
        if(second == '*')  	{	return true;	}
        if(first == '.') 	{	return false;	}
        if(second == '.') 	{	return true;	}
        if(first == '|') 	{	return false;	}
        else 				{	return true;	}
    }

    private static void union() {
        // Load two AFND in stack into variables
        AFND nfa2 = RegEx.stackAfnd.pop();
        AFND nfa1 = RegEx.stackAfnd.pop();

        // Creer  union operation
        Etat start = new Etat(RegEx.etatID++);
        Etat end	= new Etat(RegEx.etatID++);

        // Modifier transition au debut de chaque  subAFND avec epsilon
        start.addTransition(nfa1.getAfnd().getFirst(), 'e');
        start.addTransition(nfa2.getAfnd().getFirst(), 'e');

        // Modifier transition a la fin de chaque subAfnd avec epsilon
        nfa1.getAfnd().getLast().addTransition(end, 'e');
        nfa2.getAfnd().getLast().addTransition(end, 'e');

        // Add start to the end of each AFN
        nfa1.getAfnd().addFirst(start);
        nfa2.getAfnd().addLast(end);

        // Add all RegEx.etats in AFN2 to the end of AFN1
        // in order
        for (Etat s : nfa2.getAfnd()) {
            nfa1.getAfnd().addLast(s);
        }
        // ajouter AFN a la fin de la pile
        RegEx.stackAfnd.push(nfa1);
    }

    private static void concatenation() {
        // retrieve nfa 1 and 2 from RegEx.stackAfnd
        AFND nfa2 = RegEx.stackAfnd.pop();
        AFND nfa1 = RegEx.stackAfnd.pop();

        // Add transition to the end of nfa 1 to the begin of nfa 2
        // the transition uses empty string
        nfa1.getAfnd().getLast().addTransition(nfa2.getAfnd().getFirst(), 'e');

        // Add all RegEx.etats in nfa2 to the end of nfa1
        for (Etat s : nfa2.getAfnd()) {	nfa1.getAfnd().addLast(s); }

        // Put nfa back to RegEx.stackAfnd
        RegEx.stackAfnd.push (nfa1);
    }

    private static void star() {
        // Retrieve top AFND from Stack
        AFND nfa = RegEx.stackAfnd.pop();

        // Creer RegEx.etats avec * operation
        Etat start = new Etat(RegEx.etatID++);
        Etat end	= new Etat(RegEx.etatID++);

        // Add transition to start and end RegEx.etat
        start.addTransition(end, 'e');
        start.addTransition(nfa.getAfnd().getFirst(), 'e');

        nfa.getAfnd().getLast().addTransition(end, 'e');
        nfa.getAfnd().getLast().addTransition(nfa.getAfnd().getFirst(), 'e');

        nfa.getAfnd().addFirst(start);
        nfa.getAfnd().addLast(end);

        // Put nfa back in the RegEx.stackAfnd
        RegEx.stackAfnd.push(nfa);
    }

}
