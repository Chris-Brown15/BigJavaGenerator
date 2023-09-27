/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg;

import static java.lang.String.format;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import sc.bjg.task.TextTask;

/**
 * Entrypoint class for BigJavaGenerator (BJG). The arguments to the main method determine its behavior.
 */
public class Run {

	/**
	 * The main method of the application.
	 * 
	 * @param args — Arguments to the application.
	 */
	public static void main(String... args) {

		List<RunArgument> arguments = initialParseArguments(args);		
		RunArgument inputArgument = arguments.stream().filter(arg -> arg.type == RunArguments.IN).findAny().get();	

		Optional<RunArgument> prefix = arguments.stream().filter(arg -> arg.type == RunArguments.SET_FLAG_PREFIX).findAny();
		if(prefix.isPresent()) TextReader.setPrefix(prefix.get().nextArgument());

		Optional<RunArgument> suffix = arguments.stream().filter(arg -> arg.type == RunArguments.SET_FLAG_SUFFIX).findAny();
		if(suffix.isPresent()) TextReader.setSuffix(suffix.get().nextArgument());
		
		TextReader reader;
		try {
			
			reader = new TextReader(inputArgument.nextArgument());
				
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return;
			
		}
		
		StringBuffer copiedText = TextTask.copy(reader.sourceFile);
		
		Stream<RunArgument> stream = arguments.stream();
		stream.filter(arg -> arg.type == RunArguments.REMOVE).forEach(arg -> arg.taskByThisType(copiedText, reader).run());
		stream = arguments.stream();			
		stream.filter(arg -> arg.type.isSafe).forEach(arg -> arg.taskByThisType(copiedText, reader).run());			
		
		Optional<RunArgument> license = arguments.stream().filter(arg -> arg.type == RunArguments.LICENSE).findAny();
		if(license.isPresent()) ResultWriter.setLicenseMessage(license.get().nextArgument());

		Optional<RunArgument> note = arguments.stream().filter(arg -> arg.type == RunArguments.NOTE).findAny();
		if(note.isPresent()) ResultWriter.setProgrammaticMessage(note.get().nextArgument());
		
		Optional<RunArgument> output = arguments.stream().filter(arg -> arg.type == RunArguments.O).findAny();
		String outputPath = output.isPresent() ? output.get().nextArgument() : getOutputName();

		Optional<RunArgument> fileType = arguments.stream().filter(arg -> arg.type == RunArguments.FILE_TYPE).findAny();
		if(fileType.isPresent()) outputPath += fileType.get().nextArgument();
		
		try {
			
			new ResultWriter(outputPath , copiedText).write();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return;
			
		}
		
	}
	
	private static List<RunArgument> initialParseArguments(String[] args) {
		
		List<RunArgument> arguments = new ArrayList<>(args.length * 2);
		
		for(int i = 0 ; i < args.length ; i++) {
			
			String x = args[i];
			
			if(x.charAt(0) == '-') {
				
				RunArguments type = RunArguments.enumForArgument(x.substring(1));
				if(!type.incrementInstances()) throw new IllegalStateException(format("Only %d %s argument can be given." , 1 , type.toString()));
				arguments.add(new RunArgument(type));
				
			} else arguments.get(arguments.size() - 1).addArgument(x);
						
		}
		
		if(RunArguments.IN.instances() < 1) throw new IllegalStateException("No input file given.") ;
		return arguments;
		
	}

	private static String getOutputName() {
		
		LocalDateTime time = LocalDateTime.now();		
		return String.format("BJG_Output_at_%d_%d_%d_%d" , time.getYear() , time.getDayOfYear() , time.getMinute() , time.getSecond());
		
	}
	
}
