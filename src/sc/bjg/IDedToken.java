/**
 * Licensed under MIT No Attribution.
 */
package sc.bjg;

/**
 * Container for individual identified tokens.
 */
public class IDedToken {
	
	final long ID;
	final String token;
	
	IDedToken(long ID , String token){
		 
		this.ID = ID;
		this.token = token;
		
	}
	
	/**
	 * Returns the ID of the token this {@code IDedToken} represents.
	 * 
	 * @return The ID of this token.
	 */
	public long ID() {
		
		return ID;
		
	}

	/**
	 * Returns the string representation of the token this {@code IDedToken} represents.
	 * 
	 * @return Textual token this {@code IDedToken} represents.
	 */
	public String token() {

		return token;
		
	}

	@Override public String toString() {
		
		return "Token of ID: " + ID + " with name: " + token;
		
	}
	
}
