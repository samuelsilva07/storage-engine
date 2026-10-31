package br.com.engine.disk;

public class CorruptedDataException extends RuntimeException {
	 private static final long serialVersionUID = 1L;

	 public CorruptedDataException(String message) {
		 super(message);
    }
}
