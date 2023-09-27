/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg.task;

import java.util.function.IntConsumer;

/**
 * Abstract class for tasks on text.
 */
public abstract class TextTask implements Runnable {

	/**
	 * Creates a distinct {@code StringBuilder} which is a deep copy of {@code source}.
	 * 
	 * @param source — {@code StringBuilder} to copy from.
	 * @return New {@code StringBuilder}.
	 */ 
	public static StringBuffer copy(StringBuilder source) {
		
		int size = (int) source.chars().count();
		StringBuffer copy = new StringBuffer(size);
		source.chars().forEach(value -> copy.append((char)value));		
		return copy;
		
	}
	
	protected StringBuffer copy;
	
	public TextTask(StringBuffer source) {
		
		this.copy = source;
		
	}
	
	protected final void forEachOccurenceOf(String occurrence , int offsetBy , IntConsumer callback) {
		
		int index = 0;
		int nextOccurence;
		while((nextOccurence = copy.indexOf(occurrence , index)) != -1) {
			
			callback.accept(nextOccurence);
			index = nextOccurence + offsetBy;
					
		}
		
	}
	
}
