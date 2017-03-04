package monolith52.bookminer.util;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

public class FileLockUtil {
	public final static File LAUNCH_LOCK_FILE = new File(".Launch.lock");
	FileChannel lockFileChannel;
	FileLock lockFileLock;
	File file;

	public FileLockUtil(File file) {
		this.file = file;
	}
	
	/**
	 * ファイルによる二重起動防止ロック
	 * @return
	 */
	public boolean tryLockForLaunch() {
		try {
			lockFileChannel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			file.deleteOnExit();
			lockFileLock = lockFileChannel.tryLock();
			if (lockFileLock == null) return false;
			return true;
		} catch (IOException e) {
			try {
				if (lockFileChannel != null) lockFileChannel.close();
				return false;
			} catch (IOException ee) {
				return false;
			}
		}
	}
	
	/**
	 * ファイルによる二重起動防止ロックの解除
	 * 異常終了の場合、ファイルは残るがロックは外れるので問題無い
	 */
	public void unlockForLaunch() {
		try {
			if (lockFileLock != null) lockFileLock.release();
		} catch (IOException e) {}
		try {
		if (lockFileChannel != null) lockFileChannel.close();
		} catch (IOException e) {}
	}


}
