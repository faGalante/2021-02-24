package it.polito.tdp.PremierLeague.model;

public class Adiacenza {
	
	Player giocatore1 ;
	Double efficienza1 ;
	Player giocatore2 ;
	Double efficienza2 ;
	
	public Adiacenza(Player giocatore1, Double efficienza1, Player giocatore2, Double efficienza2) {
		super();
		this.giocatore1 = giocatore1;
		this.efficienza1 = efficienza1;
		this.giocatore2 = giocatore2;
		this.efficienza2 = efficienza2;
	}

	public Player getGiocatore1() {
		return giocatore1;
	}

	public void setGiocatore1(Player giocatore1) {
		this.giocatore1 = giocatore1;
	}

	public Double getEfficienza1() {
		return efficienza1;
	}

	public void setEfficienza1(Double efficienza1) {
		this.efficienza1 = efficienza1;
	}

	public Player getGiocatore2() {
		return giocatore2;
	}

	public void setGiocatore2(Player giocatore2) {
		this.giocatore2 = giocatore2;
	}

	public Double getEfficienza2() {
		return efficienza2;
	}

	public void setEfficienza2(Double efficienza2) {
		this.efficienza2 = efficienza2;
	}

	@Override
	public String toString() {
		return "Adiacenza [giocatore1=" + giocatore1 + ", efficienza1=" + efficienza1 + ", giocatore2=" + giocatore2
				+ ", efficienza2=" + efficienza2 + "]";
	}
	
	
	

}
