package monolith52.bookminer.util;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class FilenameUtil {
	private static char[] bans = {'\\', '/', ':', '*', '?', '"', '<', '>', '|'};
	private static char[] reps = {'￥', '／', '：', '＊', '？', '”', '＜', '＞', '｜'};
	
	public static String replaceSystemChar(String str) {
		for (int i=0; i<bans.length; i++) {
			str = str.replace(bans[i], reps[i]);
		}
		return str;
	}
	
	public static File getCollisionName(File file, int index) {
		StringBuffer filename = new StringBuffer(FilenameUtils.removeExtension(file.getAbsolutePath()));
		filename.append('(').append(String.valueOf(index)).append(").");
		filename.append(FilenameUtils.getExtension(file.getAbsolutePath()));
		return new File(filename.toString());
	}
}
