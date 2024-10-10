package bookmanagemnet;

public class BookVO {

	private int bookID;
	private String largeCategory;
	private String smallCategory;
	private String bookName;
	private String author;
	private String publisher;
	private String location;
	private int	   count;
	private int	   totalrenting;
	
	
	public int getBookID() {
		return bookID;
	}
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	public String getLargeCategory() {
		return largeCategory;
	}
	public void setLargeCategory(String largeCategory) {
		this.largeCategory = largeCategory;
	}
	public String getSmallCategory() {
		return smallCategory;
	}
	public void setSmallCategory(String smallCategory) {
		this.smallCategory = smallCategory;
	}
	
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
		public int getTotalrenting() {
		return totalrenting;
	}
	public void setTotalrenting(int totalrenting) {
		this.totalrenting = totalrenting;
	}
	
	
	
	public String[] toStringList() {
		 String[] list = {bookID+"",largeCategory,smallCategory,bookName,author,publisher,count+"",totalrenting+"",location};
		 return list;
	}
	
}
