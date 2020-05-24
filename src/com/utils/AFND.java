package com.utils;

import java.util.LinkedList;

public class AFND {
	private LinkedList<Etat> afnd;
	
	public AFND () {
		this.setAfnd(new LinkedList<Etat> ());
		this.getAfnd().clear();
	}

	public LinkedList<Etat> getAfnd() {
		return afnd;
	}

	public void setAfnd(LinkedList<Etat> afnd) {
		this.afnd = afnd;
	}
}
