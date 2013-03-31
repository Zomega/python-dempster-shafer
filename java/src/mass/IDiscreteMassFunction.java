/**
 * Created on Nov 15, 2008
 */
package mass;

import hypothesis.IDiscreteHypothesis;



/**
 * Interface for mass functions defined over a discrete frame of discernment.
 * 
 * @author Thomas Reineking
 *
 */
public interface IDiscreteMassFunction<S extends Comparable<S>, H extends IDiscreteHypothesis<S, H>, M extends IDiscreteMassFunction<S, H, M>> extends IMassFunction<H, M> {
	
	/**
	 * Constructs the pignistic transformation of this mass function. The result is a probability distribution over singletons.
	 * 
	 * @return The pignistic transformation.
	 */
	public M getPignisticTransformation();
	
	/**
	 * Predicts the distribution for the next state in dynamic system when the belief about the current state is expressed by this mass function.
	 * 
	 * @param model the model describing the state transition belief
	 * @return The predicted distribution.
	 */
//	public M dynamicUpdate(IDynamicStateModel<S, H, M> model);
	
	/**
	 * Determines the set of singletons with the highest plausibility values.
	 * 
	 * @return The set of singletons with the highest plausibility values.
	 */
	public H getMostPlausibleSingletons();
	
}