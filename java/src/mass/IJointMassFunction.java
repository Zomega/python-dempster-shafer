/**
 * Created on Nov 16, 2008
 */
package mass;

import hypothesis.IHypothesis;
import hypothesis.JointHypothesis;

/**
 * Represents a mass function defined over the space AxB.
 * 
 * @author Thomas Reineking
 *
 */
public interface IJointMassFunction<A extends IHypothesis<A>, B extends IHypothesis<B>, M extends IJointMassFunction<A, B, M>> extends IMassFunction<JointHypothesis<A, B>, M> {

	/**
	 * Projects the joint belief over AxB to A and writes the result to <code>dest</code>.
	 * 
	 * @param dest a mass function defined over space A
	 */
	public void projectLeft(IMassFunction<A, ?> dest);
	
	/**
	 * Projects the joint belief over AxB to B and writes the result to <code>dest</code>.
	 * 
	 * @param dest a mass function defined over space B
	 */
	public void projectRight(IMassFunction<B, ?> dest);
	
}
