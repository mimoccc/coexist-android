package com.externc.coexist.api;

/**
 * Base class for Responses (like Schema and Sync). Every Response
 * will have a status code and a status message (i.e. 200 OK).
 * @author Anthony Naddeo
 */
public abstract class Response{

	private int status;
	private String message;
	
	public Response() {}

	/**
	 * Gets the status of this response. This will be the 
	 * HTTP status code that the server responds with.
	 * @return The HTTP status of this Response.
	 */
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the message associated with this response. If its not
	 * "OK" then it will be some useful error message.
	 * @return The message associated with this response.
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	

}
