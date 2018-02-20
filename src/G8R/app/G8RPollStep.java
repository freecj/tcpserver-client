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
		
		try {

			if (strNameStep.equals(g8rRequest.getFunction())) {
				if (beforeCookie.findName(strFirstName) && beforeCookie.findName(strSecondName)) {
					String msString = beforeCookie.getValue(strFirstName) + "'s Food mood>";
					g8rResponse = new G8RResponse(statusOk, functionNameForFood, msString, beforeCookie);
					
					context.setState(new G8RFoodStep(clntSock, logger));
				} else {

					g8rResponse = new G8RResponse(statusOk, functionNameForName, "Name (First Last)>", beforeCookie);
					context.setState(new G8RNameStep(clntSock, logger));
					
				}
				writerMsg();
			} else {
				generateErrorMsg("Unexpected function");
			}

		} catch (ValidationException e) {
			
		} catch (Exception e) {
			close();
			context.setEndFlag();
		}
	}
}
