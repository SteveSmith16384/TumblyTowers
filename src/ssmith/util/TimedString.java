package ssmith.util;

public final class TimedString {
	
	private String text = "";
	private long duration;
	private long expires;
	
	public TimedString(long dur) {
		duration = dur;
		this.setText("");
		
	}

	
	public String getString() {
		return this.text;
	}
	
	
	public void setText(String s) {
		text = s;
		expires = System.currentTimeMillis() + duration;
	}
	
	
	public void process(long interpol) {
		//timer += interpol;
		if (this.expires < System.currentTimeMillis()) {
			this.text = "";
		}
		
	}
	
	

}
