/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Writer for new text after all tasks have been performed.
 */
public class ResultWriter {

	/**
	 * Prepended text to the file generated which attach a programmatic message and a license message, or nothing if null. 
	 */
	private static String 
		programmaticMessage = "/* PROGRAMMATICLY GENERATED FILE. */" + TextReader.lineFeed ,
		licenseMessage = null;
		
	/**
	 * Sets the text that will be written to the file above the contents of the file but below the license text. This is intended as a note to leave
	 * readers that the file is programmatically or machine generated.
	 * 
	 * @param message — Text to write at the top of the file.
	 */
	public static void setProgrammaticMessage(String message) {
		
		if(message != null) programmaticMessage = message + TextReader.lineFeed;
		else programmaticMessage = null;
		
	}
	
	/**
	 * Sets the text that will be written to the file at the very top. This is intended to serve as a license note so readers of the file know how
	 * it is licensed.
	 * 
	 * @param message — Text to write at the top of the file.
	 */
	public static void setLicenseMessage(String message) {
		
		if(message != null) licenseMessage = message + TextReader.lineFeed;
		else licenseMessage = null;
		
	}
	
	private final String 
		filePath ,
		writeThis;
	
	public ResultWriter(String filePath , StringBuffer taskCompletedBuffer) {
		
		this.filePath = filePath;
		this.writeThis = taskCompletedBuffer.toString();
				
	}
	
	public void write() throws IOException {
		
		try(FileWriter writer = new FileWriter(filePath)) {
			
			if(licenseMessage != null) writer.write(licenseMessage);
			if(programmaticMessage != null) writer.write(programmaticMessage);
			writer.write(writeThis);
		
		}
		
	}
	
}
