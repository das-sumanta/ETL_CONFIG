package com.configuration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class ConfigLoader {


	public static void main(String[] args) {
		copyConfigToDb();
		System.out.println("Configuration Successfully Loaded!!");

	}
	
	public static Map<String, String> readPropertiesFileAsMap(String filename, String delimiter)
			throws Exception
			{
			  Map<String, String> map = new HashMap<>();
			  BufferedReader reader = new BufferedReader(new FileReader(filename));
			  String line;
			  Properties properties = new Properties();
			  File pf = new File(filename);
			  properties.load(new FileReader(pf));
			  String key,value;
			  
			  while ((line = reader.readLine()) != null)
			  {
			    if (line.trim().length()==0) continue;
			    if (line.charAt(0)=='#') continue;
			    int delimPosition = line.indexOf(delimiter);
			    key = line.substring(0, delimPosition).trim();
			    value = properties.getProperty(key).trim();
			    map.put(key, value);
			  }
			  reader.close();
			  return map;
			}
	
	public static void copyConfigToDb() {
		
		Map<String, String> propMap = null;
		PreparedStatement ps;
		String logSql = "";
		
		
		String appConfigPropFile = "p2pconfig.properties";
		Connection con = null;
				
		try {
			
			Properties properties = new Properties();
			File pf = new File(appConfigPropFile);
			properties.load(new FileReader(pf));
			String logDbURL = properties.getProperty("LogDBURL");
			String logDbUid = properties.getProperty("LogDBUID");
			String logDbPwd = properties.getProperty("LogDBPwd");
			String secretKey = properties.getProperty("SECRETKEY");
			
			
			con = createConnection(logDbURL, logDbUid, logDbPwd);
			
			
			propMap = readPropertiesFileAsMap(appConfigPropFile, "=");
			
			logSql = "TRUNCATE TABLE p2p_config";
			ps = con.prepareStatement(logSql);
			ps.executeUpdate();
			
			for (Map.Entry<String, String> entry : propMap.entrySet()) {
				
				logSql = "INSERT INTO p2p_config(property,value) "
						+ "VALUES(?,?)";
				ps = con.prepareStatement(logSql);
				ps.setString(1, entry.getKey());
				if(!entry.getKey().equalsIgnoreCase("secretKey")) {
					
					
			    	ps.setString(2,EncryptionUtil.encrypt(entry.getValue(), secretKey));
					
				} else {
					
					ps.setString(2,entry.getValue());
				}
				
				ps.executeUpdate();
								
				
			}
			
			logSql = "INSERT INTO p2p_config(property,value) "
					+ "VALUES(?,?)";
			ps = con.prepareStatement(logSql);
			ps.setString(1, "RUNID");
			ps.setString(2, "1");
			ps.executeUpdate();
			ps.close();

			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection(con);
		}
	}
	
	

	private static Connection createConnection(String logDbURL, String logDbUid,
			String logDbPwd) {
		
		try {
			Connection con = DriverManager.getConnection(logDbURL,logDbUid,logDbPwd);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void closeConnection(Connection con) {

		try {
			if (!con.isClosed()) {
				con.close();
				con = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}
