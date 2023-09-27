/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg.task;

import sc.bjg.IDedToken;

/**
 * Task for removing tokens. The token to be removed is deleted from the resulting text and nothing is in its place.
 */
public class RemoveTask extends TextTask {

	private static final String empty = "";
	private final String operand;	
	
	/**
	 * Creates a remove text task.
	 * 
	 * @param source — Text source to operate on.
	 * @param operand — Token to remove.
	 */
	public RemoveTask(StringBuffer source , IDedToken operand) {

		super(source);
		this.operand = operand.token();
		
	}

	@Override public void run() {

		final int length = operand.length();
		super.forEachOccurenceOf(operand, 0 , index -> copy.replace(index, index + length, empty));
		
	}

}
