package G8R.app;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import G8R.serialization.CookieList;
import G8R.serialization.G8RResponse;
import G8R.serialization.ValidationException;

/**
 *
 */
public class G8RPollStep extends PollState{
	/**
	 * @param clntSock
	 * @param logger
	 */
	public G8RPollStep(Socket clntSock, Logger logger) {
		super(clntSock, logger);
	}
	@Override
	public void generateMsg() {

		CookieList beforeCookie = g8rRequest.getCookieList();
		System.out.println(g8rRequest.getFunction());
		try {

			if (strNameStep.equals(g8rRequest.getFunction())) {
				if (beforeCookie.findName(strFirstName) && beforeCookie.findName(strSecondName)) {
					String msString = beforeCookie.getValue(strFirstName) + "'s Food mood>";
					g8rResponse = new G8RResponse(statusOk, functionNameForFood, msString, beforeCookie);
					g8rResponse.encode(socketOut);
					context.setState(new G8RFoodStep(clntSock, logger));
				} else {

					g8rResponse = new G8RResponse(statusOk, functionNameForName, "Name (First Last)>", beforeCookie);
					g8rResponse.encode(socketOut);
					context.setState(new G8RNameStep(clntSock, logger));
					
				}

			} else {
				generateErrorMsg("Unexpected function");
				context.setEndFlag();
			}

		} catch (ValidationException e) {
			generateErrorMsg("Unexpected function");
			context.setEndFlag();
		} catch (IOException e) {
			close();
			context.setEndFlag();
		} catch (Exception e) {
			close();
			context.setEndFlag();
		}
	}
}
