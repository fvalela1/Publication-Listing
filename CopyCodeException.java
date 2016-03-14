//Custom exception subclass of the Exception class that is thrown when a user tries to input a publication code that already exists

public class CopyCodeException extends Exception{
	
	public CopyCodeException(String message) {
        super(message);
	}
}
