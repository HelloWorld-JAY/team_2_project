package bookmanagemnet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BookDaoImpl implements BookDao{

	private static final String DBDRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DBURL	 = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	private static final String DBUSER	 = "jinsang";
	private static final String DBPASS	 = "hwang";
	
	public BookDaoImpl()throws Exception {
		
		Class.forName(DBDRIVER);
	}
	
	@Override
	public ArrayList<BookVO> bookSelectAll() throws Exception {

		ArrayList<BookVO> list = new ArrayList<BookVO>();
		
		Connection con = null;
		PreparedStatement ps = null;
		
		String sql = "SELECT * FROM k_book";
		
		try {
		con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);
		
		ps = con.prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			
			BookVO vo = new BookVO();
			
			vo.setBookID(rs.getInt("BOOKID"));
			vo.setLargeCategory(rs.getString("LARGECATEGORY"));
			vo.setSmallCategory(rs.getString("SMALLCATEGORY"));
			vo.setBookName(rs.getString("BOOKNAME"));
			vo.setAuthor(rs.getString("AUTHOR"));
			vo.setPublisher(rs.getString("PUBLISHER"));
			vo.setLargeCategory(rs.getString("LOCATION"));
			
			list.add(vo);
		}
		
		}finally {
			ps.close();
			con.close();
		}
		
		return list;
	}

}
