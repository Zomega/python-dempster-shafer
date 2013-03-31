/**
 * Dec 16, 2008
 */
package mass.exact;

import hypothesis.IDiscreteHypothesis;

/**
 * Interface for the state transition belief in a first-order hidden Markov model.
 * 
 * @author Thomas Reineking
 *
 */
public interface ITransitionModel<S extends Comparable<S>, H extends IDiscreteHypothesis<S, H>, M extends AbstractDiscreteMassFunction<S, H, M>> {

	/**
	 * Generates the state transition belief distribution given that the current state is <code>state</code>.
	 * 
	 * @param state the current state
	 * @return The transition belief.
	 */
	public M predict(S state);
	
}
