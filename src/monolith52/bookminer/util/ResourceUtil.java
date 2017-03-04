package monolith52.bookminer.util;

import java.util.ResourceBundle;

public class ResourceUtil {
	static ResourceBundle resource;
	
	public static ResourceBundle getBundle() {
		if (resource == null) {
			resource = ResourceBundle.getBundle("resources.messages", new UTF8ResourceBundleControl());
		}
		return resource;
	}
	
	public static String format(String key, Object... args) {
		return String.format(getString(key), args);
	}
	
	public static String getString(String key) {
		return getBundle().getString(key);
	}
}
