/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Enumeration of arguments the main method can accept.
 */
enum RunArguments {

	O(false , false , 1 , "o" , "output") ,
	IN(false , false , 1 , "in" , "input") ,
	REPLACE(true , true , 2 , "rp" , "replace") ,
	REMOVE(false , true , 1 , "rm" , "remove") ,
	PREPEND(true , true , 2 , "p" , "prepend") ,
	LICENSE(false , false , 1 , "l" , "license") ,
	NOTE(false , false , 1 , "n" , "note") ,
	FILE_TYPE(false , false , 1 , "f" , "filetype") ,
	INSERT(true , true , 3 , "i" , "insert") ,
	SET_FLAG_PREFIX(false , false , 1 , "sfp" , "setflagprefix") ,
	SET_FLAG_SUFFIX(false , false , 1 , "sfs" , "setflagsuffix") ,
	APPEND(true , true , 2 , "a" , "append")
	;

	/**
	 * Creates and returns a {@code RunArguments} which contains an argument string that matches exactly {@code from}.
	 * 
	 * @param from — A string representing an argument type.
	 * @return {@code RunArguments} containing an argument matching {@code from}.
	 * @throws NoSuchElementException if {@code from} does not match an argument for any of the argument types.
	 */
	public static RunArguments enumForArgument(String from) {
		
		RunArguments[] args = values();
		for(RunArguments x : args) if(x.validArguments.stream().anyMatch(argument -> argument.equals(from))) return x;
		throw new NoSuchElementException(from + " is not a valid argument for this application.");
		
	}
	
	private AtomicInteger instances = new AtomicInteger(0);
	
	public final int numberInputValues; 
	public final boolean 
		isSafe ,
		canHaveMultiple;
	private final List<String> validArguments = new ArrayList<>();
	
	RunArguments(boolean isSafe , boolean canHaveMultiple , int numberInputValues , String... arguments) {
		
		this.numberInputValues = numberInputValues;
		this.canHaveMultiple = canHaveMultiple;
		this.isSafe = isSafe;
		for(String x : arguments) validArguments.add(x);
		
	}

	/**
	 * Increments the count of instances this argument has been encountered, returning whether the new instance is OK or not.
	 * 
	 * @return {@code true} if it is OK to proceed after encountering another instance of this run argument.
	 */
	boolean incrementInstances() {
		
		int newInstances = instances.incrementAndGet();
		return canHaveMultiple || newInstances <= 1;
		
	}
	
	int instances() {
		
		return instances.get();
		
	}
	
}