import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Image {
	private String category;
	private String word;
	private String url;
	private int frequency;
	private int length;
	private int imageability;
	
	Image(String w, String c, String i, String l,String f,String u) {
		try {
			this.setCategory(c);
			this.setWord(w);
			this.setUrl(u);
			this.setFrequency(Integer.parseInt(f));
			this.setLength(Integer.parseInt(l));
			this.setImageability(Integer.parseInt(i));
			}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getImageability() {
		return imageability;
	}

	public void setImageability(int imageability) {
		this.imageability = imageability;
	}



	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}