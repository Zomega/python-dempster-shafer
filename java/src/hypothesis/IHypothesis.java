/**
 * Nov 11, 2008
 */
package hypothesis;

/**
 * Interface for all hypotheses. A hypothesis is a non-empty subset of the frame of discernment.
 * <p>
 * The interface uses self-bounding which is why implementing classes must provide parameterizations for <code>H</code>.
 * <p> 
 * Implementing classes should override <code>equals</code>.
 * 
 * @author Thomas Reineking
 *
 */
public interface IHypothesis<H extends IHypothesis<H>> extends Cloneable, Comparable<H> {

	/**
	 * Intersects two hypotheses.
	 * 
	 * @param hypothesis another hypothesis
	 * @return The intersection of both hypotheses.
	 */
	public H intersect(H hypothesis);
	
	/**
	 * Unites two hypotheses.
	 * 
	 * @param hypothesis another hypothesis
	 * @return The union of both hypotheses.
	 */
	public H unite(H hypothesis);
	
	/**
	 * Determines whether this hypothesis represents an empty set.
	 * 
	 * @return <code>true</code> if and only if this hypothesis contains no singletons.
	 */
	public boolean isEmpty();
	
	/**
	 * Determines whether this hypothesis is a super set of <code>hypothesis</code>, i.e.,
	 * all singletons of <code>hypothesis</code> are also contained by this hypothesis.
	 * @param hypothesis another hypothesis
	 * @return <code>true</code> if and only if this hypothesis is a super set of <code>hypothesis</code>.
	 */
	public boolean isSuperSetOf(H hypothesis);
	
	/**
	 * Creates a shallow copy of this hypothesis.
	 * 
	 * @return A copied instance containing the same singletons.
	 */
	public H clone();
	
}
