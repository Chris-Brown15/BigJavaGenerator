/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Reads a text file from the given file path, storing generator regexes in a public list.
 * <p>
 * 	The regexes are of the form of a non javadoc comment, i.e, / *__#__* /, with no spaces between the asterisks and the forward slashes, and any
 * 	numerical value replacing the pound sign. 
 * 	The next token found after the ending forward slash of the regex will be associated with that regex ID (the number in the middle). From there,
 *  the user can either replace the given regex with some other text, remove the token associated with the regex, or replace the token with 
 *  something new. 
 *  
 * </p>
 */
public class TextReader {

	/**
	 * Containers for the prefix and suffix regexes to look for when trying to find identified tokens.
	 */
	public static List<Character> 
		prefixChars = new ArrayList<>() ,
		suffixChars = new ArrayList<>();
	
	public static final long MALFORMED_ID = -1l;
	public static final char lineFeed = System.lineSeparator().charAt(0);

	static {
		
		prefixChars.add('/');
		prefixChars.add('*');
		prefixChars.add('_');
		prefixChars.add('_');
		suffixChars.add('_');
		suffixChars.add('_');
		suffixChars.add('*');
		suffixChars.add('/');
		
	}
	
	/**
	 * Sets the prefix to look for in flags to {@code newChars}.
	 * 
	 * @param newChars — New characters to look for when identifying flags in text.
	 * @throws IllegalArgumentException if {@code newChars} contains white space, which is not allowed for prefix or suffix flags.
	 */
	public static void setPrefix(String newChars) {

		setxxxfix(newChars , prefixChars);
		
	}

	/**
	 * Sets the suffix to look for in flags to {@code newChars}.
	 * 
	 * @param newChars — New characters to look for when identifying flags in text.
	 * @throws IllegalArgumentException if {@code newChars} contains white space, which is not allowed for prefix or suffix flags.
	 */
	public static void setSuffix(String newChars) {
		
		setxxxfix(newChars, suffixChars);
		
	}
	
	private static void setxxxfix(String newChars , List<Character> list) {

		ListIterator<Character> chars = stringIterator(newChars);
		list.clear();
		while(chars.hasNext()) {
			
			char x = chars.next();
			if(isWhiteSpace(x)) throw new IllegalArgumentException("Flags cannot contain white space characters; " + newChars + " does.");
			list.add(x);
			
		}
				
	}
	
	private static ListIterator<Character> stringIterator(String source) {

		char[] array = source.toCharArray();		
		List<Character> ints = new ArrayList<>(); 
		for(char x : array) ints.add(x);
		return ints.listIterator();
		
	}
	
	private static boolean isWhiteSpace(char character) {
		
		return character == ' ' || character == '\t' || character == lineFeed;
		
	}

	public final StringBuilder sourceFile = new StringBuilder();
	
	private final List<IDedToken> IDedTokens = new ArrayList<>();
	private final List<String> allTokens = new ArrayList<>();
	private int allTokensCurrentIndex = 0;
	
	/**
	 * Creates a new text reader. The file pointed to by the given {@code sourcePath} string is read and parsed in this constructor.  
	 * 
	 * @param sourcePath — A file path to a string.
	 * @throws FileNotFoundException if {@code sourcePath} does not point to a readable file.
	 * @throws NullPointerException if {@code sourcePath} is null.
	 */
	public TextReader(String sourcePath) throws FileNotFoundException {
		
		Objects.requireNonNull(sourcePath);
				
		Scanner scanner = new Scanner(new File(sourcePath));
		
		for(String line ; scanner.hasNextLine() ; ) { 
			
			line = scanner.nextLine();				
			LinkedList<String> split = customSplit(line);
			allTokens.addAll(split);
			sourceFile.append(line).append(lineFeed);
			
		}			
				
		scanner.close();
		
		while(allTokensCurrentIndex < allTokens.size()) receiveString(allTokens.get(allTokensCurrentIndex++));
	
	}
	
	/**
	 * Gets the token by the given ID.
	 * 
	 * @param ID — The ID to find.
	 * @return Token whose ID is the given ID.
	 * @throws NoSuchElementException if {@code ID} does not point to an IDed token.
	 * @throws IllegalArgumentException if {@code ID} cannot be a valid ID.
	 */
	public IDedToken getTokenByID(long ID) {
		
		if(ID == MALFORMED_ID) throw new IllegalArgumentException(ID + " is not a valid ID.");
		for(IDedToken x : IDedTokens) if(x.ID == ID) return x;
		throw new NoSuchElementException(ID + " does not identify a IDed token.");
		
	}
	
	/**
	 * Invokes {@code callback} for each IDed token of this reader.
	 * 
	 * @param callback — Code to invoke.
	 */
	public void forEachToken(Consumer<IDedToken> callback) {
		
		Objects.requireNonNull(callback);
		IDedTokens.forEach(callback);
		
	}
	
	private void receiveString(String fromFile) {
		
		ListIterator<Character> iter = stringIterator(fromFile);
		receiveString(iter , true);
		
	}

	private void receiveString(ListIterator<Character> chars , boolean checkFirstChar) {
				
		long ID = getIDFromFlag(chars , checkFirstChar);
		if(ID == MALFORMED_ID) return;
		if(IDedTokens.stream().map(token -> token.ID).anyMatch(value -> value.equals(ID))) { 
			
			throw new IllegalArgumentException(ID + " already identifies a token.");
			
		}
		
		boolean isTokenNextToID = chars.hasNext();
		
		if(!isTokenNextToID) { 
			
			String next = allTokens.get(allTokensCurrentIndex++);
			if(next == null) return;
			chars = stringIterator(next);
			
		} 
		
		String token = nextToken(chars);
		if(chars.hasNext()) receiveString(chars , true);		
		IDedTokens.add(new IDedToken(ID , token));
		
	}
	
	/**
	 * Returns an ID from a flag. Flags are comments or textual structures within text files that identify important tokens. They will contain some
	 * prefix and suffix, with a number in the middle, their ID. This ID is returned, or -1 is returned if there is no ID to get, either because
	 * a flag is not present or it is malformed.
	 * 
	 * @param chars — Iterator of characters of a string. 
	 * @param checkFirstChar — Sometimes the iterator should not start at the first char of the prefix for the flag, so if {@code true}, the first
	 * 						   char is checked.
	 * @return — ID for a flag, or -1.
	 */
	private long getIDFromFlag(ListIterator<Character> chars , boolean checkFirstChar) {
				
		int xxxfixIndex = checkFirstChar ? 0 : 1;
		//iterate over the chars of the token, trying to find a substring matching the prefix list. 
		while(chars.hasNext()) {
			
			char next = chars.next();
			if(next == prefixChars.get(xxxfixIndex++)) { 
			
				if(xxxfixIndex == prefixChars.size()) break;
			
			} else xxxfixIndex = 0;
			
		}

		if(xxxfixIndex == 0) return -1;
		//start at 1 because the first value of the suffixChars will be found when getting the ID 
		xxxfixIndex = 1;
		char next = 0;		
		StringBuilder IDBuilder = new StringBuilder();
		//get the chars of the ID
		//TODO: allow for different codices (we currently - 48 to get a character, which I believe will work for ASCII, UTF8, and UTF16)
		while(chars.hasNext() && (next = chars.next()) != suffixChars.get(0)) IDBuilder.append(next - 48);
		if(IDBuilder.length() == 0) return -1;
		//ensure the suffix is correct
		while(xxxfixIndex < suffixChars.size()) if(!chars.hasNext() || chars.next() != suffixChars.get(xxxfixIndex++)) return -1;		
		return Long.parseLong(IDBuilder.toString());
					
	}
	
	private String nextToken(ListIterator<Character> chars) {
		
		StringBuilder builder = new StringBuilder();
		//go until another whitespace or comment char is found
		char next = 0;
		while((chars.hasNext() && (next = chars.next()) != '/')) builder.append((char)next);
		chars.previous();
		return builder.toString();
		
	}

	private LinkedList<String> customSplit(String line) {
		
		LinkedList<String> splits = new LinkedList<>();
		
		ListIterator<Character> iter = stringIterator(line);
		StringBuilder builder = new StringBuilder();
		while(iter.hasNext()) {
			
			char next = iter.next();
			if(isWhiteSpace(next)) {
				
				if(builder.length() > 0) {
					
					splits.add(builder.toString());
					builder.setLength(0);
					
				}
				
				continue;
				
			}
			
			builder.append(next);
			
		}
		
		splits.add(builder.toString());
		
		return splits;
		
	}
	
}
