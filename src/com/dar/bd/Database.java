package com.dar.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * @author GamesXchanges Team
 * La classe <code>Database</code> permet de se connecter et ouvrir une connexion à la base de données 
 * à l'aide du Driver et les variables static qu'on récupère de la classe abstraite <code>DBStatic</code>
 */
public class Database {
	private DataSource dataSource;
	private static Database  database;
	
	/**
	 * 
	 * @param jndiname
	 * @throws SQLException
	 */
	public Database(String jndiname) throws SQLException{
		try{
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/"+jndiname);
		}
		catch(NamingException e){
			throw new SQLException(jndiname+" is missing in JNDI! :"+e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return une instance de <code>Connection</code> à la base de données
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
	/**
	 * 
	 * @return une instance de <code>Connection</code> à la base de données
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static Connection getMySQLConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		if(!DBStatic.mysql_pooling){
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return DriverManager.getConnection("jdbc:mysql://"+DBStatic.mysql_host+"/"+DBStatic.mysql_db+"?useSSL=false", DBStatic.mysql_username,DBStatic.mysql_password);
		}
		else{
			if(database==null){
				database= new Database("jdbc/db");
			}
			return(database.getConnection());
		}
	}
}