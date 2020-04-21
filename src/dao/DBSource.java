package dao;


import java.io.File;
import java.io.IOException;
import java.sql.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DBSource {

	private static BasicDataSource connectionPool;
	
//	static final String configXML = "./config.xml";
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static String DB_URL;
	static String USER;
	static String PASS;
	
	static{		
//		File file = new File(configXML);
//		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder documentBuilder;		
		try {
			
//			documentBuilder = documentBuilderFactory.newDocumentBuilder();
//			Document document = documentBuilder.parse(file);
//			USER = document.getElementsByTagName("user").item(0).getTextContent();
//			PASS = document.getElementsByTagName("password").item(0).getTextContent();
//			DB_URL = document.getElementsByTagName("db-url").item(0).getTextContent();

			connectionPool = new BasicDataSource();
			connectionPool.setPoolPreparedStatements(true);
	    	connectionPool.setUsername("root");

	    	connectionPool.setPassword("rootrootroot");
		    connectionPool.setDriverClassName(JDBC_DRIVER);
		    
		    //revert this to original 
		    //connectionPool.setUrl("jdbc:mysql://aa1arb2i67pp0c7.clcjwswdswyh.us-east-2.rds.amazonaws.com:3306/aa1arb2i67pp0c7?useSSL=false");
		    
		    connectionPool.setUrl("jdbc:mysql://aarc9cin3z08cw.clcjwswdswyh.us-east-2.rds.amazonaws.com:3306/aarc9cin3z08cw");

	    	/*connectionPool.setPassword("root");
		    connectionPool.setDriverClassName(JDBC_DRIVER);
		    connectionPool.setUrl("jdbc:mysql://localhost:3306/psych?useSSL=false");*/

		    connectionPool.setInitialSize(2);
		    
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public DBSource(){
	}

	public static BasicDataSource getConnectionPool(){

		System.out.println(connectionPool.getUrl());

		return connectionPool;
	}
	
	public static void closeConnectionPool() throws SQLException{
		connectionPool.close();
	}
}
