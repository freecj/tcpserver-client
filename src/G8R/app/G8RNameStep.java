package G8R.app;

import java.net.Socket;
import java.util.logging.Logger;

import G8R.serialization.CookieList;
import G8R.serialization.G8RResponse;
import G8R.serialization.ValidationException;

public class G8RNameStep extends PollState{

	
	public G8RNameStep(Socket clientSocket, Logger logger) {
		super(clientSocket, logger);
	}
	@Override
	public void generateOkMsg() {
		CookieList beforeCookie = g8rRequest.getCookieList();
		G8RResponse temp = null;
		try {
			g8rResponse =  new G8RResponse("OK", "NameStep", "Name (First Last)>", beforeCookie);
		} catch (ValidationException e) {
			
		}
		
		
	}
	@Override
	public void generateErrorMsg(String Msg) {
		CookieList beforeCookie = g8rRequest.getCookieList();
		G8RResponse temp = null;
		try {
			g8rResponse =  new G8RResponse("ERROR", "NULL", Msg, beforeCookie);
		} catch (ValidationException e) {
			
		}
		
	}
	
}
