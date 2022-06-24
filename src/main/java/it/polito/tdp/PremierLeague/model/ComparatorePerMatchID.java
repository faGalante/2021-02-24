package it.polito.tdp.PremierLeague.model;

import java.util.Comparator;

public class ComparatorePerMatchID implements Comparator<Match> {

	@Override
	public int compare(Match o1, Match o2) {
		
		return o1.getMatchID()-o2.getMatchID();
	}

}
