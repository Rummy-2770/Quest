package au.com.simpsons.digital.quest.error;

import lombok.Data;

/**
 * User defined exception to handle the error cases and respond back in a formatted way. 
 * @author Ramesh
 */
@Data
public class QuestException extends RuntimeException {

	public QuestException(int httpStatus, String errorMsg) {
		super();
		this.errorMsg = errorMsg;
		this.httpStatus = httpStatus;
	}

	private String errorMsg;

	private int httpStatus;

	@Override
	public String toString() {
		return  "{ \"httpStatus\" : "  + httpStatus + ", \"ErrorMsg\" : \"" + errorMsg +"\"}";
	}

}
