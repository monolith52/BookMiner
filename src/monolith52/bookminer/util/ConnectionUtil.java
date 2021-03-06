package monolith52.bookminer.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class ConnectionUtil {
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
	
	public static HttpURLConnection prepare(String url, String hostbase) throws IOException {
		URL urlobj;
		
		try {
			urlobj = new URL(url);
		} catch (MalformedURLException e) {
			throw new IOException(e);
		}
		
		// 指定したホスト外への攻撃防止
		if (urlobj == null || 
				urlobj.getHost() == null || !urlobj.getHost().endsWith(hostbase)) {
			throw new IOException(ResourceUtil.getString("error.connection.url"));
		}
		
		// 誤ってループした場合の攻撃防止
		ThreadUtil.sleep(200);
		System.out.println("HTTP GET: " + url);
		
		HttpURLConnection con = (HttpURLConnection)urlobj.openConnection();
		con.setRequestProperty("User-Agent", USER_AGENT);
		return con;
	}
	
	public static String getPage(String url, String hostbase, String encoding) throws IOException {
		HttpURLConnection con = prepare(url, hostbase);
		con.connect();
		
		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException(ResourceUtil.format("invalid http response code: ", con.getResponseCode()));
		}
		
		return IOUtils.toString(con.getInputStream(), encoding);
	}
	
	public static void saveBinary(String url, String hostbase, OutputStream out) throws IOException {
		HttpURLConnection con = prepare(url, hostbase);
		con.connect();
		
		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException(ResourceUtil.format("invalid http response code: ", con.getResponseCode()));
		}
		
		IOUtils.copy(con.getInputStream(), out);
	}
}
