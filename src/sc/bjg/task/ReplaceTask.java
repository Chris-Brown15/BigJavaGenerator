/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg.task;

import sc.bjg.IDedToken;

/**
 * Task for replacing a token of text with some other text.
 */
public class ReplaceTask extends TextTask implements Runnable {

	private final String 
		operand ,
		replaceWith;
	
	/**
	 * Creates a replace task. This task will replace all tokens of the given operands with some new text.
	 * 
	 * @param source — Source for text contents to replace.
	 * @param operands — Token to replace.
	 * @param replaceWith — Text to replace any instance of an operand with.
	 */
	public ReplaceTask(StringBuffer source , IDedToken operand , String replaceWith) {

		super(source);
		this.operand = operand.token();
		this.replaceWith = replaceWith;
		
	}

	@Override public void run() {
		
		final int 
			length = operand.length() ,
			replaceLength = replaceWith.length();
		
		super.forEachOccurenceOf(operand, replaceLength , index -> copy.replace(index , index + length , replaceWith));
		
	}

}
