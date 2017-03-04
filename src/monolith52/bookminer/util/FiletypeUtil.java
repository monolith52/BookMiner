package monolith52.bookminer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FiletypeUtil {
	public static String check(File file) {
		byte[] b = new byte[6];
		try (FileInputStream in = new FileInputStream(file)) {
			in.read(b);
			if (b[0]=='P' && b[1]=='K') return "zip";
			if (b[0]=='R' && b[1]=='a' && b[2]=='r') return "rar";
			if (b[3]=='-' && b[4]=='l' && b[5]=='h') return "lzh";
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
