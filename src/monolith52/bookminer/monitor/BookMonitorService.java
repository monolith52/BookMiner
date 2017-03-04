package monolith52.bookminer.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.scene.image.Image;
import monolith52.bookminer.repository.Book;
import monolith52.bookminer.repository.BookDAO;
import monolith52.bookminer.util.ConnectionUtil;
import monolith52.bookminer.util.FilenameUtil;
import monolith52.bookminer.util.FiletypeUtil;
import monolith52.bookminer.util.ResourceUtil;
import monolith52.bookminer.util.ThreadUtil;

@Service
public class BookMonitorService implements Runnable {

	@Autowired
	private BookDAO bookDAO;

	boolean running = false;
	List<BookFoundListener> listeners = new ArrayList<BookFoundListener>();
	
	public void addBookFoundListener(BookFoundListener listener) {
		listeners.add(listener);
	}
	
	public void scan() {
		String page = getPage();
		if (page == null) return;
		
		List<Book> books = new BookParser().parse(page);
		books.forEach(book -> {
			if (bookDAO.hasEntry(book.getSiteId())) {
				System.out.println(String.format("[hasBinary] %s", book.getTitle()));
				return;
			}
			System.out.println(String.format("getting image %s", book.getThumbnailUrl()));
			book.setThumbnail(new Image(book.getThumbnailUrl()));
//			if (!pickupBinary(book)) {
//				System.out.println(String.format("[missingFile] %s", book.getTitle()));
//				return;
//			}
			if (bookDAO.insert(book)) {
				System.out.println(String.format("[inserted] %s", book.getTitle()));
				listeners.forEach(l -> l.bookFound(book));
			} else {
				System.out.println(String.format("[insertError] %s", book.getTitle()));
			}
		});
	}
	
	public void run() {
		running = true;
		while (running) {
			scan();
			int interval = Integer.parseInt(ResourceUtil.getString("monitor.interval"));
			System.out.println("waiting next scan [sec]: " + (interval / 1000));
			ThreadUtil.sleep(interval);
		}
	}
	
	private String getBinaryUrl(String siteId) {
		return String.format(ResourceUtil.getString("target.binary"), siteId);
	}
	
	private boolean pickupBinary(Book book) {
		File file = new File(ResourceUtil.getString("dir.download") + book.getSiteId());
		try (FileOutputStream out = new FileOutputStream(file)) {
			HttpURLConnection con = ConnectionUtil.prepare(getBinaryUrl(book.getSiteId()), 
					ResourceUtil.getString("target.hostbase"));
			con.connect();
			
			if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException(ResourceUtil.format("invalid http response code: ", con.getResponseCode()));
			}
			
			IOUtils.copy(con.getInputStream(), out);
			
		} catch (IOException e) {
			if (file.exists()) file.delete();
			return false;
		}
		
		settingFilename(book, file);
		checkFileCollision(book);
		renameTempfile(book, file);
		settingMd5(book);
		
		return true;
	}
	
	private void settingFilename(Book book, File file) {
		String safename = FilenameUtil.replaceSystemChar(book.getTitle());
		if (safename.length() == 0) safename = book.getSiteId();
		String ext = FiletypeUtil.check(file);
		if (ext != null) {
			book.setFilename(safename + "." + ext);
		}
	}
	
	private void checkFileCollision(Book book) {
		if (book.getFilename() == null) return;
		
		String destname = ResourceUtil.getString("dir.download") + book.getFilename();
		File destfile = new File(destname);
		int index = 1;
		if (destfile.exists()) {
			destfile = FilenameUtil.getCollisionName(destfile, index);
			book.setFilename(destfile.getName());
		}
	}

	private void renameTempfile(Book book, File file) {
		if (book.getFilename() == null) return;
		
		String destname = FilenameUtils.getFullPath(file.getAbsolutePath()) + book.getFilename();
		file.renameTo(new File(destname));
	}
	
	private void settingMd5(Book book) {
		File file = new File(ResourceUtil.getString("dir.download") + book.getFilename());
		try (FileInputStream in = new FileInputStream(file)) {
			book.setMd5(DigestUtils.md5Hex(in));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPage() {
		try {
			return ConnectionUtil.getPage(
					ResourceUtil.getString("target.url"),
					ResourceUtil.getString("target.hostbase"), 
					ResourceUtil.getString("target.encoding"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
