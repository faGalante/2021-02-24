package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Team> listAllTeams(){
		String sql = "SELECT * FROM Teams";
		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				result.add(team);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> listAllPlayersByMatch(Match m, Map<Integer, Player>idMap){
		String sql = "SELECT DISTINCT a1.PlayerID "
				+ "FROM actions a1 "
				+ "WHERE a1.MatchID = ? ";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, m.getMatchID());
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = idMap.get(res.getInt("a1.PlayerID")) ;
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Double getEfficienzaByPlayerMatch (Player giocatore, Match partita){
		String sql = "SELECT (a1.TotalSuccessfulPassesAll + a1.Assists) / a1.TimePlayed AS efficienza "
				+ "FROM actions a1 "
				+ "WHERE a1.PlayerID = ? AND a1.MatchID = ? ";
//		List<Action> result = new ArrayList<Action>();
		Double efficienza = null ;
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, giocatore.getPlayerID());
			st.setInt(2, partita.getMatchID());
			ResultSet res = st.executeQuery();

			if(res.first()) { 
				efficienza = res.getDouble("efficienza") ;
//				result.add(action);
			}
			conn.close();
			return efficienza ;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> listAllAdiacenze (Match partita, Map<Integer, Player> idMap){
		String sql = "SELECT a1.PlayerID, ((a1.TotalSuccessfulPassesAll + a1.Assists) / a1.TimePlayed) AS e1, "
				+ "a2.PlayerID, ((a2.TotalSuccessfulPassesAll + a2.Assists) / a2.TimePlayed) AS e2 "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.MatchID = ? "
				+ "AND a1.TeamID != a2.TeamID AND a1.MatchID = a2.MatchID "
				+ "GROUP BY a1.PlayerID, a2.PlayerID ";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
//			st.setInt(1, giocatore.getPlayerID());
			st.setInt(1, partita.getMatchID());
			ResultSet res = st.executeQuery();

			while(res.next()) {
				Player player1 = idMap.get(res.getInt("a1.PlayerID")) ;
				Player player2 = idMap.get(res.getInt("a2.PlayerID")) ;
				
				Double e1 = res.getDouble("e1") ;
				Double e2 = res.getDouble("e2") ;
				
				Adiacenza a = new Adiacenza(player1, e1, player2, e2) ;
				
				result.add(a);
			}
			conn.close();
			return result ;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
