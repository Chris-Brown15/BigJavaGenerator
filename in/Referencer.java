package test;

public class /*__1__*/Referencer /*__2__*/<T> {
	
	private volatile /*__3__*/T value;
	
	public Referencer(T initialValue) {
		
		this.value = initialValue;
		
	}
	
	public Referencer() {
		
		value = /*__4__*/null;
		
	}
	
	public T /*__8__*/get () {
		
		return value;
		
	}
	
	public void /*__9__*/set (T value) {
		
		this.value = value;
		
	}
	
 	@Override public String toString() {
		
 		return /*__7__*/"Referencer containing: " + String.valueOf(value);
 		
	}
	
 	@Override public int hashCode() {
 		
 		return /*__5__*/value.hashCode();
 		
 	}
 	
 	@SuppressWarnings("unchecked") @Override public boolean equals(Object other) {
 		
 		return other instanceof Referencer && ((Referencer<T>)other).value/*__6__*/.equals(value);
 		
 	}
 	
}