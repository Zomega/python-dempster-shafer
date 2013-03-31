/**
 * Dec 16, 2008
 */
package mass.sampled;

import hypothesis.IDiscreteHypothesis;

/**
 * Interface for the state transition belief in a first-order hidden Markov model.
 * 
 * @author Thomas Reineking
 *
 */
public interface ISampledTransitionModel<S extends Comparable<S>, H extends IDiscreteHypothesis<S, H>, M extends AbstractSampledDiscreteMassFunction<S, H, M>> {

	/**
	 * Randomly draws a sample from the state transition belief distribution given that the current state is <code>state</code>.
	 * 
	 * @param state the current state
	 * @return The drawn sample.
	 */
	public H predict(S state);
	
}
