package at.jku.tk.mms.mpx.wave;

/**
 * Exception which is beeing raised in {@link AudioFileReader} if the format is not compatilbe with the TkWave encoder 
 * 
 * @author matthias
 */
public class AudioFormatException extends Exception {

	private static final long serialVersionUID = -8330148468522106176L;
	
	/**
	 * Creates a new {@link AudioFormatException}
	 * 
	 * @param message
	 */
	public AudioFormatException(String message) {
		super(message);
	}

}
