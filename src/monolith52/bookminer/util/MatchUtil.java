package monolith52.bookminer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtil {
	
	public static String getFormer(Pattern pattern, String input) {
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) return input.substring(0, matcher.start());
		return input;
	}
	
	public static String getLatter(Pattern pattern, String input) {
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) return input.substring(matcher.end());
		return input;
	}
	
	public static int findInteger1(Pattern pattern, String input) {
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find() || matcher.groupCount() < 1) {
			return 0;
		}
		return Integer.parseInt(matcher.group(1));
	}

	public static String findGroup1(Pattern pattern, String input) {
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find() || matcher.groupCount() < 1) {
			return null;
		}
		return matcher.group(1);
	}
}
