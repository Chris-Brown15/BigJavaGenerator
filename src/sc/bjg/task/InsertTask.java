/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg.task;

import sc.bjg.IDedToken;

/**
 * Inserts some text at an offset from the first character of tokens of a given type.
 */
public class InsertTask extends TextTask {

	private final String
		token ,
		insert;
	
	private final int offset;
	
	/**
	 * Creates an insert task.
	 *  
	 * @param source — Source text to operate on.
	 * @param token — Token to insert at.
	 * @param offset — Offset from the first char to insert.
	 * @param insert — String to insert.
	 */
	public InsertTask(StringBuffer source , IDedToken token , int offset , String insert) {

		super(source);
		this.token = token.token();
		this.insert = insert;
		this.offset = offset;
		
	}

	@Override public void run() {

		final int offsetLength = token.length() + insert.length();
		
		super.forEachOccurenceOf(token , offsetLength , index -> copy.insert(index + offset, insert));
		
	}

}
