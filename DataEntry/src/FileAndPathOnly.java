import java.io.File;


public class FileAndPathOnly {
	private String filePath;
	private File file;
	
	FileAndPathOnly() {
		filePath=null;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String path) {
		filePath = path;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
