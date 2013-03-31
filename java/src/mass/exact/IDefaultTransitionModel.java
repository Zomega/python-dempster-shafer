/**
 * Dec 16, 2008
 */
package mass.exact;

import hypothesis.Hypothesis;

/**
 * Interface for the state transition belief in a first-order hidden Markov model.
 * Acts as a simplification of the generic model <code>ITransitionModel</code> when using the <code>MassFunction</code> class.
 * 
 * @author Thomas Reineking
 *
 */
public interface IDefaultTransitionModel<S extends Comparable<S>> extends ITransitionModel<S, Hypothesis<S>, MassFunction<S>> {

	// empty
	
}
