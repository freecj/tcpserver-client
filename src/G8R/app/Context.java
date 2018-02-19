package G8R.app;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import G8R.serialization.ValidationException;

public class Context {
	public G8RNameStep nameStep;  
	private boolean endFlag = false;
	private PollState curstate;
	
	/**
	 * @return
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
	 * 
	 */
	public void pull() {
		if (curstate.read()) {
			curstate.generateMsg();
		}
	

	}
	/**
	 * @return
	 */
	public boolean isEndFlag() {
		return endFlag;
	}
	/**
	 * 
	 */
	public void setEndFlag() {
		this.endFlag = true;
	}
	
}
