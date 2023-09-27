/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg;

import static java.lang.Long.parseLong;
import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.List;

import sc.bjg.task.AppendTask;
import sc.bjg.task.InsertTask;
import sc.bjg.task.PrependTask;
import sc.bjg.task.RemoveTask;
import sc.bjg.task.ReplaceTask;
import sc.bjg.task.TextTask;

/**
 * Contains a run argument and values for it to operate with. 
 */
class RunArgument {

	final RunArguments type;
	final List<String> values;
	
	private int nextArgument = 0;
	
	RunArgument(RunArguments type) {
		
		this.type = type;
		values = new ArrayList<>(type.numberInputValues);
				
	}
	
	TextTask taskByThisType(StringBuffer buffer , TextReader reader) {
		
		switch(type) {		
			case PREPEND: return new PrependTask(buffer , reader.getTokenByID(parseLong(nextArgument())) , nextArgument());
			case REMOVE: return new RemoveTask(buffer , reader.getTokenByID(parseLong(nextArgument())));
			case REPLACE: return new ReplaceTask(buffer , reader.getTokenByID(parseLong(nextArgument())) , nextArgument());
			case INSERT: return new InsertTask(buffer , reader.getTokenByID(parseLong(nextArgument())) , parseInt(nextArgument()) , nextArgument());
			case APPEND: return new AppendTask(buffer , reader.getTokenByID(parseLong(nextArgument())) , nextArgument());
			default: throw new UnsupportedOperationException("A text task cannot be generated from a run argument of type " + type);
				
		}
		
	}

	void addArgument(String argument) {

		values.add(argument);
		
		if(type.numberInputValues < values.size()) throw new IllegalArgumentException(
			"For an argument type of " + type + ", exactly " + type.numberInputValues + " inputs must be given, but " + values.size() + " were: " +
			values.toString()
		);
				
	}
	
	String nextArgument() {
		
		return values.get(nextArgument++);
		
	}

	int numberArgumentValues() {
		
		return values.size();
		
	}
	
}
