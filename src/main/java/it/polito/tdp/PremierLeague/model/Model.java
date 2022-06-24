package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao ;
	private List<Match> matches ;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap ;
	private List<Player> players ;
	
	public Model() {
		dao = new PremierLeagueDAO() ;
		idMap = new HashMap<Integer, Player>() ;
		matches = new ArrayList<Match>(dao.listAllMatches()) ;
		this.players = new ArrayList<>() ;
		
		for(Player p : dao.listAllPlayers()) {
			idMap.put(p.playerID, p) ;
		}
		
	}
	
	public String creaGrafo(Match partita) {
		this.grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class) ;
		this.players = dao.listAllPlayersByMatch(partita, idMap) ;
		
		Graphs.addAllVertices(this.grafo, this.players) ;
		
		
			List<Adiacenza> archi = new ArrayList<>(dao.listAllAdiacenze(partita, idMap)) ;
			for(Adiacenza arco : archi) {

				if(arco.getEfficienza2() >= arco.getEfficienza1() 
						&& !this.grafo.containsEdge(this.grafo.getEdge(arco.getGiocatore1(), arco.getGiocatore2()))) {
					double peso = arco.getEfficienza2()-arco.getEfficienza1() ;
					Graphs.addEdgeWithVertices(this.grafo, arco.getGiocatore2(), arco.getGiocatore1(), peso) ;
				} else if(arco.getEfficienza2() <= arco.getEfficienza1()
						&& !this.grafo.containsEdge(this.grafo.getEdge(arco.getGiocatore2(), arco.getGiocatore1()))){
					double peso = arco.getEfficienza1()-arco.getEfficienza2() ;
					Graphs.addEdgeWithVertices(this.grafo, arco.getGiocatore1(), arco.getGiocatore2(), 
							peso) ;

				}
		
		
	}
		
		return "Grafo creato\n" + "#VERTICI: " + this.grafo.vertexSet().size() + "\n"
				+ "#ARCHI: " + this.grafo.edgeSet().size() + "\n";
	}
	
	public double getEfficienza(Player giocatore, Match partita) {
		double efficienza = dao.getEfficienzaByPlayerMatch(giocatore, partita);
		return efficienza ;
	}
	
	public List<Match> getMatches() {
		Collections.sort(matches, new ComparatorePerMatchID());
		return matches;
	}

	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public Map<Integer, Player> getIdMap() {
		return idMap;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public boolean grafoCreato() {
		if(this.grafo==null) {
			return false ;
		} else 
			return true ;
	}
	
	public String getGiocatoreDeltaMigliore() {

		double max = 0.0 ;
		Player migliore = null ;
		for(Player p : this.grafo.vertexSet()) {
			if(getDeltaEfficienza(p) > max) {
				max = getDeltaEfficienza(p) ;
				migliore  = p ;
			}
			
		}
		return migliore.getPlayerID() + " - " + migliore.getName()  
				+", delta efficienza = " + getDeltaEfficienza(migliore) + "\n" 
		;
	}

	private double getDeltaEfficienza(Player p) {
		double sumUscenti = 0.0 ;
		double sumEntranti = 0.0 ;
		double deltaEfficienzaComplessivo = 0.0 ;
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
			sumUscenti += this.grafo.getEdgeWeight(e) ;
		}
		for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
			sumUscenti += this.grafo.getEdgeWeight(e) ;
		}
		
		deltaEfficienzaComplessivo = sumUscenti - sumEntranti ;
		return deltaEfficienzaComplessivo;
	}
	
}
