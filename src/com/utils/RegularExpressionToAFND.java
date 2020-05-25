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

            }
            else if (regular.charAt(i) == '+'  && isInputCharacter(regular.charAt(i+1)) ) {
                newRegular += regular.charAt(i) + ".";

            }else if ( regular.charAt(i) == '*' && regular.charAt(i+1) == '(' ) {
                newRegular += regular.charAt(i) + ".";

            }
            else if ( regular.charAt(i) == '+' && regular.charAt(i+1) == '(' ) {
                newRegular += regular.charAt(i) + ".";

            }else if ( regular.charAt(i) == ')' && regular.charAt(i+1) == '(') {
                newRegular += regular.charAt(i) + ".";

            } else {
                newRegular += regular.charAt(i);
            }
        }
        newRegular += regular.charAt(regular.length() - 1);
        return newRegular;
    }

    private static boolean isInputCharacter(char charAt) {
        boolean x=false;
        switch (charAt) {
            case 'a': x=true;break;
            case 'b': x=true;break;
            case 'e': x=true;break;
            case 'c': x=true;break;
            case 'd': x=true;break;
            case 'f': x= true;break;
            case 'g': x=true;break;
            case 'h' : x= true;break;
            case 'i': x= true;break;
            case 'j': x= true;break;
            case 'k': x= true;break;
            case 'l' : x=true;break;
            case 'm' : x= true;break;
            case 'n': x= true;break;
            case 'o' : x=true;break;
            case 'p' : x= true;break;
            case 'q' : x=true;break;
            case 'r' : x= true;break;
            case 's' : x= true;break;
            case 't' : x=true;break;
            case 'u' : x=true;break;
            case 'v' : x= true;break;
            case 'x' : x= true;break;
            case 'y' : x=true;break;
            case 'z' : x= true;break;
            default : x=false;break;

        }return x;
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
                case ('+'):
                    plus();break;

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
        if(first == '+') 	{	return false;	}
        if(second == '+')  	{	return true;	}
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

    private static void plus() {
        // Retrieve top AFND from Stack
        AFND nfa = RegEx.stackAfnd.pop();

        // Creer RegEx.etats avec * operation
        Etat start = new Etat(RegEx.etatID++);
        //  Etat end	= new Etat(RegEx.etatID++);

        // Add transition to start and end RegEx.etat

        start.addTransition(nfa.getAfnd().getFirst(), 'e');

        //    nfa.getAfnd().getLast().addTransition(end, 'e');
        nfa.getAfnd().getLast().addTransition(nfa.getAfnd().getFirst(), 'e');

        nfa.getAfnd().addFirst(start);
        //   nfa.getAfnd().addLast(end);

        // Put nfa back in the RegEx.stackAfnd
        RegEx.stackAfnd.push(nfa);
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
