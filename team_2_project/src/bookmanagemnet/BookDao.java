package bookmanagemnet;

import java.util.ArrayList;

public interface BookDao {

	public ArrayList<BookVO> bookSelectAll() throws Exception;
}
