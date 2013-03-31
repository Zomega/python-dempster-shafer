/**
 * Created on Nov 15, 2008
 */
package hypothesis;

import java.util.Collection;

/**
 * Represents a hypothesis in a discrete frame of discernment.
 * 
 * @author Thomas Reineking
 *
 */
public interface IDiscreteHypothesis<S extends Comparable<S>, H extends IDiscreteHypothesis<S, H>> extends IHypothesis<H>, Iterable<S> {

	/**
	 * Adds a collection of singletons to this hypothesis.
	 * 
	 * @param singletons a collection of singletons
	 */
	public void add(Collection<? extends S> singletons);
	
	/**
	 * Removes a collection of singletons from this hypothesis.
	 * 
	 * @param singletons a collection of singletons
	 * @return <code>true</code> if and only if at least one singleton was removed.
	 */
	public boolean remove(Collection<? extends S> singletons);
	
	/**
	 * Removes all singletons from this hypothesis.
	 */
	public void clear();
	
	/**
	 * Returns the number of singletons contained by this hypothesis.
	 * 
	 * @return The number of singletons contained by this hypothesis.
	 */
	public int size();
	
	/**
	 * Returns <code>true</code> if and only if all elements of <code>singletons</code> are contained
	 * by this hypothesis.
	 * 
	 * @param singletons a collection of singletons
	 * 
	 * @return <code>true</code> if and only if all elements of <code>singletons</code> are contained
	 * by this hypothesis.
	 */
	public boolean contains(Collection<? extends S> singletons);
	
}
