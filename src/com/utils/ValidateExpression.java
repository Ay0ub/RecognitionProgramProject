package com.utils;

public class ValidateExpression {

	// Return if the string is valid with regular expression or not
	public static boolean validate(AFD afd, String s) {
		
		Etat etat = afd.getAfd().getFirst();
		
		// Especial case when is empty string
		if (s.compareTo("e") == 0) {
			// If first is etat is accept etat, so empty string is valid
			if (etat.isAcceptEtat()) 	{	return true; 	}
			else						{	return false; 	}
			
		} else if (afd.getAfd().size() > 0) {

			for (int i = 0 ; i < s.length(); i++) {
				// No transition, so break the AFD
				// and it's invalid string
				if (etat == null) { break; }
				
				// Get the transition with the input
				etat = etat.getNextEtat().get(s.charAt(i)).get(0);
			}
			
			// Is valid string
			if (etat != null && etat.isAcceptEtat()) {	return true;	}
			// is INvalid string
			else 										{	return false;	}
		
		} else {
			return false;
		}
		
	}
}
