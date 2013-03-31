/**
 * Nov 11, 2008
 */
package util;

import java.lang.reflect.Constructor;


/**
 * Represents a tuple of two elements.
 * 
 * @author Thomas Reineking
 *
 */
public class Tuple<A extends Comparable<? super A>, B extends Comparable<? super B>> implements Cloneable, Comparable<Tuple<A, B>> {

	public A a;
	
	public B b;
	
	
	public Tuple(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Tuple && ((Tuple<?, ?>) obj).a.equals(a) && ((Tuple<?, ?>) obj).b.equals(b);
	}
	
	@Override
	public String toString() {
		return a + "|" + b;
	}

	@Override
	public int compareTo(Tuple<A, B> o) {
		int adiff = a.compareTo(o.a);
		if (adiff != 0)
			return adiff;
		else
			return b.compareTo(o.b);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Tuple<A, B> clone() {
		try {
			Constructor<?> con = getClass().getConstructor(a.getClass(), b.getClass());
			return (Tuple<A, B>) con.newInstance(a, b);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
