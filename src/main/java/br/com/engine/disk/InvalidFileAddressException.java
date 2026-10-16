package disk;

public class InvalidFileAddressException extends Exception {
	 private static final long serialVersionUID = 1L;

	 public InvalidFileAddressException(String message) {
		 super(message);
    }
}
