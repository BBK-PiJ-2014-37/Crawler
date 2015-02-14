package org.joel.crawler;

import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

public class LargeStringSet implements StringSet {
	private Connection conn;

	public LargeStringSet(Path filename) {
		try {
			Properties p = System.getProperties();
			p.setProperty("derby.system.home", filename.getParent().toString());
			p.setProperty("derby.stream.error.file", "/dev/null");
			conn = DriverManager.getConnection("jdbc:derby:" + filename.getFileName().toString() + ";create=true", p);
			Statement stat = conn.createStatement();
			stat.execute("CREATE TABLE elements(item VARCHAR(8192) PRIMARY KEY)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Finds if the database contains the given url
	 * @param url the url 
	 * @return true if the url was found.
	 */
	public boolean contains(String url) {
		boolean found = false;
		try {
			PreparedStatement stat = conn.prepareStatement("SELECT * FROM elements WHERE item = ?");
			stat.setString(1, url);
			found = stat.executeQuery().next();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		return found;
	}

	/**
	 * Adds the url to the  database
	 * @param url the url 
	 */
	public void add(String url) {
		try {
			PreparedStatement stat = conn.prepareStatement("INSERT INTO elements VALUES (?)");
			stat.setString(1, url);
			stat.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

}
