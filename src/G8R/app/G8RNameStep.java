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
public class G8RNameStep extends PollState {

	/**
	 * @param clntSock
	 * @param logger
	 */
	public G8RNameStep(Socket clntSock, Logger logger) {
		super(clntSock, logger);
	}

	@Override
	public void generateMsg() {

		/*
		 * try { read(); } catch (ValidationException e1) { CookieList beforeCookie =
		 * new CookieList(); try { g8rResponse = new G8RResponse(statusErroe,
		 * functionNameForNull, e1.getToken(), beforeCookie);
		 * g8rResponse.encode(socketOut); close(); context.setEndFlag(); return; } catch
		 * (ValidationException e) { close(); context.setEndFlag(); return; } catch
		 * (IOException e) { close(); context.setEndFlag(); return; }
		 * 
		 * } catch (IOException e1) { close(); context.setEndFlag(); return; } catch
		 * (Exception e) { close(); context.setEndFlag(); return; }
		 */

		CookieList beforeCookie = g8rRequest.getCookieList();
		
		try {

			if (functionNameForName.equals(g8rRequest.getFunction()) && g8rRequest.getParams().length == 2) {
				String[] values = g8rRequest.getParams();
				beforeCookie.add(strFirstName, values[0]);
				beforeCookie.add(strSecondName, values[1]);
				String msString = values[0] + "'s Food mood>";

				g8rResponse = new G8RResponse(statusOk, functionNameForFood, msString, beforeCookie);
				g8rResponse.encode(socketOut);
				context.setState(new G8RFoodStep(clntSock, logger));

			} else {
				String mString = "Poorly formed name. Name (First Last)>";

				g8rResponse = new G8RResponse(statusError, functionNameForName, mString, beforeCookie);
				g8rResponse.encode(socketOut);
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
