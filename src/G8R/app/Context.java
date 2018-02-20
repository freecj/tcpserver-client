package G8R.app;

import java.io.IOException;
import java.util.logging.Logger;


/**
 * class which can identify the state of the server command
 */
public class Context {

	private boolean endFlag = false;
	private PollState curstate;
	
	/**
	 * @return PollState which is the command abstract class
	 */
	public PollState getState() {  
        return curstate;  
    }  
	/**
	 * @param pollState
	 */
	public void setState(PollState pollState) {  
        this.curstate = pollState;  
        this.curstate.setContext(this);  
    }  
	/**
	 * the command which accept and respond the client
	 */
	public void pull() { 
		if (curstate.read()) {
			curstate.generateMsg();
		}
	

	}
	/**
	 * @return true if it's the ending of the command. Otherwise false.
	 */
	public boolean isEndFlag() {
		return endFlag;
	}
	/**
	 * set the ending command flag.
	 */
	public void setEndFlag() {
		this.endFlag = true;
	}
	
}
