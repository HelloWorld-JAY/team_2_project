package chart;

import java.sql.*;
import java.util.*;

public class Database {

	String DRIVER 	= "oracle.jdbc.driver.OracleDriver";
	String URL 		= "jdbc:oracle:thin:@192.168.0.52:1521:xe";
	String USER 	= "jinsang";
	String PASS 	= "hwang";

	public ArrayList<ArrayList> getDataGender() {

		ArrayList<ArrayList> data = new ArrayList<ArrayList>();
		try{
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER , PASS);	
			
			//***************************************************************
			String sql = "SELECT ks.gender gender, COUNT(kb.RENTID) rentcount "
					+ "   FROM K_BOOKMANAGEMENT kb INNER JOIN K_STUDENT ks ON kb.STUDENTID = ks.STUDENTID "
					+ "   GROUP BY ks.gender";
			
			
			//***************************************************************
			
			PreparedStatement stmt = con.prepareStatement( sql );	

			ResultSet rset = stmt.executeQuery();

			
			while( rset.next() ){
				ArrayList temp = new ArrayList();
				temp.add( rset.getInt("RENTCOUNT"));				//****************
				temp.add( rset.getString("GENDER"));			//****************		
				data.add(temp);
			}
			rset.close();
			stmt.close();
			con.close();
		} catch(Exception ex){
			System.out.println("에러 : " + ex.getMessage() );
		}
		return data;
	}
	public ArrayList<ArrayList> getDataAge() {

		ArrayList<ArrayList> data = new ArrayList<ArrayList>();
		try{
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER , PASS);	
			
			//***************************************************************
			String sql = "SELECT "
					+ "    CASE "
					+ "        WHEN ks.age BETWEEN 20 AND 29 THEN '20대'"
					+ "        WHEN ks.age BETWEEN 30 AND 39 THEN '30대'"
					+ "        WHEN ks.age BETWEEN 40 AND 49 THEN '40대'"
					+ "        ELSE '기타'"
					+ "    END AS age_group, "
					+ "    COUNT(kb.RENTID) AS rentcount"
					+ " FROM "
					+ "    K_BOOKMANAGEMENT kb "
					+ " INNER JOIN "
					+ "    K_STUDENT ks ON kb.STUDENTID = ks.STUDENTID"
					+ " GROUP BY "
					+ "    CASE "
					+ "        WHEN ks.age BETWEEN 20 AND 29 THEN '20대'"
					+ "        WHEN ks.age BETWEEN 30 AND 39 THEN '30대'"
					+ "        WHEN ks.age BETWEEN 40 AND 49 THEN '40대'"
					+ "        ELSE '기타'"
					+ "    END";
			
			
			//***************************************************************
			
			PreparedStatement stmt = con.prepareStatement( sql );	

			ResultSet rset = stmt.executeQuery();

			
			while( rset.next() ){
				ArrayList temp = new ArrayList();
				temp.add( rset.getInt("RENTCOUNT"));				//****************
				temp.add( rset.getString("AGE_GROUP"));			//****************		
				data.add(temp);
			}
			rset.close();
			stmt.close();
			con.close();
		} catch(Exception ex){
			System.out.println("에러 : " + ex.getMessage() );
		}
		return data;
	}
	public ArrayList<ArrayList> getDataClass() {

		ArrayList<ArrayList> data = new ArrayList<ArrayList>();
		try{
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER , PASS);	
			
			//***************************************************************
			String sql = " SELECT ks.CLASSNAME classname ,  COUNT(kb.RENTID) rentcountn"
					+ " FROM K_BOOKMANAGEMENT kb"
					+ " INNER JOIN K_STUDENT ks ON kb.STUDENTID = ks.STUDENTID"
					+ " GROUP BY ks.CLASSNAME ";
			
			
			//***************************************************************
			
			PreparedStatement stmt = con.prepareStatement( sql );	

			ResultSet rset = stmt.executeQuery();

			
			while( rset.next() ){
				ArrayList temp = new ArrayList();
				temp.add( rset.getInt("RENTCOUNT"));				//****************
				temp.add( rset.getString("CLASSNAME"));			//****************		
				data.add(temp);
			}
			rset.close();
			stmt.close();
			con.close();
		} catch(Exception ex){
			System.out.println("에러 : " + ex.getMessage() );
		}
		return data;
	}
}
