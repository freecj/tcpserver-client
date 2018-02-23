/************************************************
*
* Author: <Jian Cao>
* Assignment: <Programe 3 >
* Class: <CSI 4321>
*
************************************************/
package G8R.app;

import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

import G8R.serialization.CookieList;
import G8R.serialization.G8RResponse;
import G8R.serialization.ValidationException;

/**
 * Send Poll or Guess command Response message
 */
public class G8RPollStep extends PollState {
	/**
	 * @param clntSock
	 * @param logger
	 */
	public G8RPollStep(Socket clntSock, Logger logger) {
		super(clntSock, logger);
	}

	@Override
	public void generateMsg() {
		// get the cookielist from the request message
		CookieList beforeCookie = g8rRequest.getCookieList();

		try {

			if (strNamePoll.equals(g8rRequest.getFunction())) {
				// Poll command fits
				if (beforeCookie.findName(strFirstName) && beforeCookie.findName(strSecondName)) {
					// have firstname and last name in request cookielist, go to the foodstep
					String msString = beforeCookie.getValue(strFirstName) + "'s Food mood>";
					g8rResponse = new G8RResponse(statusOk, functionNameForFood, msString, beforeCookie);

					context.setState(new G8RFoodStep(clntSock, logger));
				} else {
					// does not have the name cookies, then go to the namestep
					g8rResponse = new G8RResponse(statusOk, functionNameForName, "Name (First Last)>", beforeCookie);
					context.setState(new G8RNameStep(clntSock, logger));

				}
				writerMsg();
			} else if (strNameGuess.equals(g8rRequest.getFunction())) {
				// Guess command fits
				beforeCookie.add("Num", String.valueOf(new Random().nextInt(10)));
				if (!beforeCookie.findName("Score")) {
					// if cookielist does not have Score, then set it as 0
					beforeCookie.add("Score", "0");
				}

				g8rResponse = new G8RResponse(statusOk, functionNameForSendGuess, "Guess (0-9)?", beforeCookie);
				context.setState(new G8RSendGuess(clntSock, logger));
				writerMsg();
			} else {
				// command function is wrong
				generateErrorMsg("Unexpected function");
			}

		} catch (ValidationException e) {
			close();
		} catch (Exception e) {
			close();
		}
	}
}
