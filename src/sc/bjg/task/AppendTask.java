/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg.task;

import sc.bjg.IDedToken;

/**
 * Appends some text to the end of a token.
 */
public class AppendTask extends TextTask {

	private final String
		token ,
		append;
	
	public AppendTask(StringBuffer source , IDedToken token , String append) {
	
		super(source);
		this.token = token.token();
		this.append =append;
	
	}

	@Override public void run() {

		final int 
			tokenLength = token.length() ,
			totalLength = tokenLength + append.length();
				
		super.forEachOccurenceOf(token , totalLength , index -> copy.insert(index + tokenLength , append));
		

	}

}
