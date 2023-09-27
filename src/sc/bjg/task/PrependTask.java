/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg.task;

import sc.bjg.IDedToken;

/**
 * Adds some text to the beginning of any instance of a token.
 */
public class PrependTask extends TextTask {

	private final String 
		tokenString ,
		add;
	
	/**
	 * Creates an add task.  
	 * 
	 * @param source — Text buffer to modify.
	 * @param token — Token to prepend to.
	 * @param add — Text to prepend to tokens.
	 */
	public PrependTask(StringBuffer source , IDedToken token , String add) {

		super(source);
		this.tokenString = token.token();
		this.add = add;
		
	}

	@Override public void run() {

		final int offsetLength = tokenString.length() + add.length();
		super.forEachOccurenceOf(tokenString , offsetLength , index -> copy.insert(index, add));

	}

}