package ee.pri.rl.blog.util;

import java.io.File;

public class FileUtil {
	
	public static final boolean insideDirectory(File file, File directory) {
		File current = file;
		boolean insideDir = false;
		while (current != null) {
			if (current.equals(directory)) {
				insideDir = true;
				break;
			}
			current = current.getParentFile();
		}
		
		return insideDir;
	}
}
