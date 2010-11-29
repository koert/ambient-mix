package net.kazed.ambient.service;

/**
 * Audio failure.
 */
public class AudioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param detailMessage Error message.
	 * @param throwable Cause.
	 */
	public AudioException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * Constructor.
	 * @param detailMessage Error message.
	 */
	public AudioException(String detailMessage) {
		super(detailMessage);
	}

}
