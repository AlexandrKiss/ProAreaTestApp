package co.proarea.services.exception;

public class StorageFileNotFoundException extends co.proarea.services.exception.StorageException {

	public StorageFileNotFoundException(String message) {
		super(message);
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
