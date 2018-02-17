package G8R.app;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import G8R.serialization.ValidationException;

public class Context {
	public G8RNameStep nameStep;  
	private PollState pollState;
	private String strNameStep = "Poll";
	public PollState getPollState() {  
        return pollState;  
    }  
	public void setPollState(PollState pollState) {  
        this.pollState = pollState;  
        
        this.pollState.setContext(this);  
    }  
	public void nameStepFunc() {
		try {
			pollState.read();
		} catch (NullPointerException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ValidationException e) {
			
			e.printStackTrace();
		}
		if (strNameStep.equals(pollState.g8rRequest.getFunction())) {
			pollState.generateOkMsg();
		} else {
			pollState.generateOkMsg();
		}

		
	}
}
