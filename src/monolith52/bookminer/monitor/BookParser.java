package monolith52.bookminer.monitor;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import monolith52.bookminer.repository.Book;
import monolith52.bookminer.util.MatchUtil;

public class BookParser {

	private static final Pattern SUBJECT_FROM 	= Pattern.compile("<!--アップロード一覧-->");
	private static final Pattern SUBJECT_TO 	= Pattern.compile("<!--/アップロード一覧-->");
	private static final String SPLITTER		= "<!--1-->";
	
	private static final Pattern EXT_SITE_ID	= Pattern.compile("<div class=\"list\" id=\"([^\"]+)\">");
	private static final Pattern EXT_TITLE		= Pattern.compile("<a href=\"/detail/[^\"]+\" title=\"([^\"]+)\">");
	private static final Pattern EXT_THUMBNAIL_URL = Pattern.compile("<img src=\"([^\"]+)\" class=\"picborder mb5\"");

	public List<Book> parse(String source) {
		Stream<String> parts = split(source);
		return parts.map(this::toBook).collect(Collectors.toList());
	}
	
	private Stream<String> split(String source) {
		String subject = MatchUtil.getLatter(SUBJECT_FROM, source);
		subject = MatchUtil.getFormer(SUBJECT_TO, subject);
		return Arrays.stream(subject.split(SPLITTER)).skip(1);
	}
	
	private Book toBook(String subject) {
		Book book = new Book();
		book.setSiteId(MatchUtil.findGroup1(EXT_SITE_ID, subject));
		book.setTitle(MatchUtil.findGroup1(EXT_TITLE, subject));
		book.setThumbnailUrl(MatchUtil.findGroup1(EXT_THUMBNAIL_URL, subject));
		return book;
	}
}
