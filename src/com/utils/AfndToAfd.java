package com.utils;

import java.util.*;

public class AfndToAfd {



    public static AFD generateAFD(AFND nfa) {
        // Creating the AFD
        AFD dfa = new AFD();

        // Clearing all the etats ID for the AFD
        RegEx.etatID = 0;

        // Creer arrayList  pour etat non traiter
        LinkedList<Etat> unprocessed = new LinkedList<Etat> ();

        // Creer sets
        RegEx.set1 = new HashSet<Etat>();
        RegEx.set2 = new HashSet <Etat> ();

        // Ajouter premiere etat a  set1
        RegEx.set1.add(nfa.getAfnd().getFirst());

        // Run the first remove Epsilon the get etats that
        // run with epsilon
        removeEpsilonTransition ();


        Etat dfaStart = new Etat(RegEx.set2, RegEx.etatID++);

        dfa.getAfd().addLast(dfaStart);
        unprocessed.addLast(dfaStart);

        while (!unprocessed.isEmpty()) {

            Etat etat = unprocessed.removeLast();


            for (Character symbol : RegEx.input) {
                RegEx.set1 = new HashSet<Etat> ();
                RegEx.set2 = new HashSet<Etat> ();

                moveEtats (symbol, etat.getEtats(), RegEx.set1);
                removeEpsilonTransition ();

                boolean found = false;
                Etat st = null;

                for (int i = 0 ; i < dfa.getAfd().size(); i++) {
                    st = dfa.getAfd().get(i);

                    if (st.getEtats().containsAll(RegEx.set2)) {
                        found = true;
                        break;
                    }
                }


                if (!found) {
                    Etat p = new Etat(RegEx.set2, RegEx.etatID++);
                    unprocessed.addLast(p);
                    dfa.getAfd().addLast(p);
                    etat.addTransition(p, symbol);


                } else {
                    etat.addTransition(st, symbol);
                }
            }
        }
        // Return AFD
        return dfa;
    }

    private static void removeEpsilonTransition() {
        Stack<Etat> stack = new Stack <Etat> ();
        RegEx.set2 = RegEx.set1;

        for (Etat st : RegEx.set1) { stack.push(st);	}

        while (!stack.isEmpty()) {
            Etat st = stack.pop();

            ArrayList<Etat> epsilonEtats = st.getAllTransitions ('e');

            for (Etat p : epsilonEtats) {
                // Check p is in the set otherwise Add
                if (!RegEx.set2.contains(p)) {
                    RegEx.set2.add(p);
                    stack.push(p);
                }
            }
        }
    }

    private static void moveEtats(Character c, Set<Etat> etats, Set<Etat> set) {
        ArrayList <Etat> temp = new ArrayList<Etat> ();

        for (Etat st : etats) {	temp.add(st);	}
        for (Etat st : temp) {
            ArrayList<Etat> allEtats = st.getAllTransitions(c);

            for (Etat p : allEtats) {	set.add(p);	}
        }
    }
}
