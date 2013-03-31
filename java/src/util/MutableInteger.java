/**
 * Oct 23, 2008
 */
package util;

/**
 * Acts as a mutable Integer object.
 * 
 * @author Thomas Reineking
 *
 */
public class MutableInteger implements Cloneable, Comparable<MutableInteger> {

	public Integer value;
	
	
	public MutableInteger() {
		// empty
	}
	
	public MutableInteger(int value) {
		this.value = value;
	}
	
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof MutableInteger && ((MutableInteger) obj).value == value;
	}
	
	@Override
	public int compareTo(MutableInteger o) {
		return value - o.value;
	}
	
	@Override
	public MutableInteger clone() {
		return new MutableInteger(value);
	}
	
}
