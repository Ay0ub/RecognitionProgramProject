package com.utils;

import java.util.LinkedList;

public class AFD {
	private LinkedList<Etat> afd;
	
	public AFD () {
		this.setAfd(new LinkedList<Etat> ());
		this.getAfd().clear();
	}

	public LinkedList<Etat> getAfd() {
		return afd;
	}

	public void setAfd(LinkedList<Etat> afd) {
		this.afd = afd;
	}
}
