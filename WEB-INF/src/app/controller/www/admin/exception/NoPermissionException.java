package app.controller.www.admin.exception;

public class NoPermissionException extends Exception {
	
	public NoPermissionException(){
		super();
	}
	
	public NoPermissionException(String message){
		super(message);
	}
	
	public NoPermissionException(Throwable cause){
		super(cause);
	}
	
	public NoPermissionException(String message,Throwable cause){
		super(message,cause);
	}

}
