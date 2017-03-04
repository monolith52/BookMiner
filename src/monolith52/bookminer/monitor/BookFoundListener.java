package monolith52.bookminer.monitor;

import monolith52.bookminer.repository.Book;

public interface BookFoundListener {
	public void bookFound(Book book);
}
