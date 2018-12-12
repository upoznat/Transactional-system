package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.management.Query;
import javax.management.RuntimeErrorException;

public class DatabaseManager {
	
	private static Connection con;
	
	public DatabaseManager() throws ClassNotFoundException, SQLException{
	
		//connection to a database
		Class.forName("org.sqlite.JDBC");
				
	}
	
	public void initializeDatabase(List<HashMap<String, String>> list, String databasePath) throws ClassNotFoundException, SQLException{
		//table making
		con = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
		String tableName = null, insertString = null;
		String accountTableString =
				"CREATE TABLE balance(account_id integer PRIMARY KEY, account text, balance integer);";
		Statement stmt = con.createStatement();
		stmt.execute(accountTableString);
		
		//create all account tables
		for (String account : PrvaKlasa.accounts){
			accountTableString =  
					"CREATE TABLE " + account.toLowerCase() + "(transaction_id integer PRIMARY KEY,\n "
					+ " participant text,\n"
					+ " amount integer);";
			
			Statement stmt1 = con.createStatement();
			stmt1.execute(accountTableString);
			stmt1.close();
		}

		insertString = "INSERT INTO balance(account, balance) VALUES(?,?);";
		//used for creating tables for each individual user and updating table BALANCE with account money amounts for each user

		for(HashMap<String, String> map : list){
			tableName = map.get("name");
			//insert into balance table 
			PreparedStatement pstmt = con.prepareStatement(insertString);
			pstmt.setString(1, tableName);
			pstmt.setInt(2, (int)Double.parseDouble(map.get("BANK")));
			pstmt.executeUpdate();
			
			for(String key : map.keySet()){
				if(!(key.equals("BANK") || key.equals("name"))){
					String insertString1 = "INSERT INTO " + tableName.toLowerCase() + "(participant, amount) VALUES(?,?);";
					PreparedStatement pstmt2 = con.prepareStatement(insertString1);
					pstmt2.setString(1, key);
					pstmt2.setInt(2, (int)Double.parseDouble(map.get(key)));
					pstmt2.executeUpdate();
					pstmt2.close();}
			}
			pstmt.close();
		}
		stmt.close();
		con.close();
	}

	public void updateDatabaseAfterTransaction (int amount, String from, String to, String databasePath) throws SQLException, Exception{
		con = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
		String amountString = "SELECT b.balance FROM balance b WHERE b.account = ?;";
		PreparedStatement pstmt1 = con.prepareStatement(amountString);
		pstmt1.setString(1, from);
		ResultSet rs = pstmt1.executeQuery();
			int accountAmount = rs.getInt(1);
		//check if there is enough funds on our account, if not, we cannot transfer the money, if yes, we transfer it and update the database
		if(accountAmount<amount){
			throw new RuntimeException("You cannot transfer money");
		}
		pstmt1.close();
		
		String updateBalance = "UPDATE balance SET balance = balance + ? WHERE account = ?"; 
		pstmt1 = con.prepareStatement(updateBalance);
		pstmt1.setInt(1, amount);
		pstmt1.setString(2, to);
		pstmt1.executeUpdate();
		pstmt1.close();
		
		updateBalance = "UPDATE balance SET balance = balance - ? WHERE account = ?"; 
		pstmt1 = con.prepareStatement(updateBalance);
		pstmt1.setInt(1, amount);
		pstmt1.setString(2, from);
		pstmt1.executeUpdate();
		pstmt1.close();
		
		String insert1 = "INSERT INTO " + to.toLowerCase() + "(participant, amount) VALUES (?, ?) ";
		pstmt1 = con.prepareStatement(insert1);
		pstmt1.setString(1, from);
		pstmt1.setInt(2, amount);
		pstmt1.executeUpdate();
		pstmt1.close();
		
		String insert2 =  "INSERT INTO " + from.toLowerCase() + "(participant, amount) VALUES (?, ?) ";
		pstmt1 = con.prepareStatement(insert2);
		pstmt1.setString(1, to);
		pstmt1.setInt(2, -amount);
		pstmt1.executeUpdate();
		pstmt1.close();
		con.close();
	}

}
