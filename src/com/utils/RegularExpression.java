package com.utils;

import javafx.scene.control.Alert;

import java.util.*;

public class RegularExpression {
	private static int stateID = 0;

	private static Stack<NFA> stackNfa = new Stack<NFA> ();
	private static Stack<Character> operator = new Stack<Character> ();

	private static Set<State> set1 = new HashSet <State> ();
	private static Set<State> set2 = new HashSet <State> ();

	// Set of inputs
	private static Set <Character> input = new HashSet <Character> ();


	// Generer AFN en utilisant expression reguliere
	public static NFA generateNFA(String regular) {
		// Generere regular expression avec la  concatenation
		regular = AddConcat (regular);

		// Only inputs available
		input.add('a');
		input.add('b');

		// Cleaning stacks
		stackNfa.clear();
		operator.clear();

		for (int i = 0 ; i < regular.length(); i++) {

			if (isInputCharacter (regular.charAt(i))) {
				pushStack(regular.charAt(i));

			} else if (operator.isEmpty()) {
				operator.push(regular.charAt(i));

			} else if (regular.charAt(i) == '(') {
				operator.push(regular.charAt(i));

			} else if (regular.charAt(i) == ')') {
				while (operator.get(operator.size()-1) != '(') {
					doOperation();
				}

				// Pop the '(' left parenthesis
				operator.pop();

			} else {
				while (!operator.isEmpty() &&
						Priority (regular.charAt(i), operator.get(operator.size() - 1)) ){
					doOperation ();
				}
				operator.push(regular.charAt(i));
			}
		}

		// effacer elements restant dans la pile
		while (!operator.isEmpty()) {	doOperation(); }

		// Get AFN
		NFA completeNfa = stackNfa.pop();

		// add the accpeting state to the end of NFA
		completeNfa.getNfa().get(completeNfa.getNfa().size() - 1).setAcceptState(true);

		// return the nfa
		return completeNfa;
	}

	// Priority of operands
	private static boolean Priority (char first, Character second) {
		if(first == second) {	return true;	}
		if(first == '*') 	{	return false;	}
		if(second == '*')  	{	return true;	}
		if(first == '.') 	{	return false;	}
		if(second == '.') 	{	return true;	}
		if(first == '|') 	{	return false;	}
		else 				{	return true;	}
	}

	// Appliquer operation
	private static void doOperation () {
		Alert errorAlert = new Alert(Alert.AlertType.WARNING);
		if (RegularExpression.operator.size() > 0) {
			char charAt = operator.pop();

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

	// * operation
	private static void star() {
		// Retrieve top NFA from Stack
		NFA nfa = stackNfa.pop();

		// Creer etats avec * operation
		State start = new State(stateID++);
		State end	= new State(stateID++);

		// Add transition to start and end state
		start.addTransition(end, 'e');
		start.addTransition(nfa.getNfa().getFirst(), 'e');

		nfa.getNfa().getLast().addTransition(end, 'e');
		nfa.getNfa().getLast().addTransition(nfa.getNfa().getFirst(), 'e');

		nfa.getNfa().addFirst(start);
		nfa.getNfa().addLast(end);

		// Put nfa back in the stackNfa
		stackNfa.push(nfa);
	}

	// Do the concatenation operation
	private static void concatenation() {
		// retrieve nfa 1 and 2 from stackNfa
		NFA nfa2 = stackNfa.pop();
		NFA nfa1 = stackNfa.pop();

		// Add transition to the end of nfa 1 to the begin of nfa 2
		// the transition uses empty string
		nfa1.getNfa().getLast().addTransition(nfa2.getNfa().getFirst(), 'e');

		// Add all states in nfa2 to the end of nfa1
		for (State s : nfa2.getNfa()) {	nfa1.getNfa().addLast(s); }

		// Put nfa back to stackNfa
		stackNfa.push (nfa1);
	}

	// Makes union of sub NFA 1 with sub NFA 2
	private static void union() {
		// Load two NFA in stack into variables
		NFA nfa2 = stackNfa.pop();
		NFA nfa1 = stackNfa.pop();

		// Creer  union operation
		State start = new State(stateID++);
		State end	= new State(stateID++);

		// Modifier transition au debut de chaque  subNFA avec epsilon
		start.addTransition(nfa1.getNfa().getFirst(), 'e');
		start.addTransition(nfa2.getNfa().getFirst(), 'e');

		// Modifier transition a la fin de chaque subNfa avec epsilon
		nfa1.getNfa().getLast().addTransition(end, 'e');
		nfa2.getNfa().getLast().addTransition(end, 'e');

		// Add start to the end of each AFN
		nfa1.getNfa().addFirst(start);
		nfa2.getNfa().addLast(end);

		// Add all states in AFN2 to the end of AFN1
		// in order
		for (State s : nfa2.getNfa()) {
			nfa1.getNfa().addLast(s);
		}
		// ajouter AFN a la fin de la pile
		stackNfa.push(nfa1);
	}

	// ajouter symbol  a stackNfa
	private static void pushStack(char symbol) {
		State s0 = new State(stateID++);
		State s1 = new State(stateID++);

		// ajouter transition from 0 to 1 with the symbol
		s0.addTransition(s1, symbol);

		// new temporary AFN
		NFA nfa = new NFA();

		// Ajouter etats a AFN
		nfa.getNfa().addLast(s0);
		nfa.getNfa().addLast(s1);

		// Ajouter nfa a la fin de la pile stackNfa
		stackNfa.push(nfa);
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

	// Return true if is part of the automata Language else is false
	private static boolean isInputCharacter(char charAt) {
		if 		(charAt == 'a')	return true;
		else if (charAt == 'b')	return true;
		else if (charAt == 'e')	return true;
		else					return false;
	}


	//generer AFD
	public static DFA generateDFA(NFA nfa) {
		// Creating the DFA
		DFA dfa = new DFA();

		// Clearing all the states ID for the AFD
		stateID = 0;

		// Creer arrayList  pour etat non traiter
		LinkedList <State> unprocessed = new LinkedList<State> ();

		// Creer sets
		set1 = new HashSet <State> ();
		set2 = new HashSet <State> ();

		// Ajouter premiere etat a  set1
		set1.add(nfa.getNfa().getFirst());

		// Run the first remove Epsilon the get states that
		// run with epsilon
		removeEpsilonTransition ();


		State dfaStart = new State(set2, stateID++);

		dfa.getDfa().addLast(dfaStart);
		unprocessed.addLast(dfaStart);

		while (!unprocessed.isEmpty()) {

			State state = unprocessed.removeLast();


			for (Character symbol : input) {
				set1 = new HashSet<State> ();
				set2 = new HashSet<State> ();

				moveStates (symbol, state.getStates(), set1);
				removeEpsilonTransition ();

				boolean found = false;
				State st = null;

				for (int i = 0 ; i < dfa.getDfa().size(); i++) {
					st = dfa.getDfa().get(i);

					if (st.getStates().containsAll(set2)) {
						found = true;
						break;
					}
				}


				if (!found) {
					State p = new State(set2, stateID++);
					unprocessed.addLast(p);
					dfa.getDfa().addLast(p);
					state.addTransition(p, symbol);


				} else {
					state.addTransition(st, symbol);
				}
			}
		}
		// Return AFD
		return dfa;
	}

	// Supprimer epsilon transition des etats
	private static void removeEpsilonTransition() {
		Stack <State> stack = new Stack <State> ();
		set2 = set1;

		for (State st : set1) { stack.push(st);	}

		while (!stack.isEmpty()) {
			State st = stack.pop();

			ArrayList <State> epsilonStates = st.getAllTransitions ('e');

			for (State p : epsilonStates) {
				// Check p is in the set otherwise Add
				if (!set2.contains(p)) {
					set2.add(p);
					stack.push(p);
				}
			}
		}
	}

	// Move states based on input symbol
	private static void moveStates(Character c, Set<State> states, Set<State> set) {
		ArrayList <State> temp = new ArrayList<State> ();

		for (State st : states) {	temp.add(st);	}
		for (State st : temp) {
			ArrayList<State> allStates = st.getAllTransitions(c);

			for (State p : allStates) {	set.add(p);	}
		}
	}
}
