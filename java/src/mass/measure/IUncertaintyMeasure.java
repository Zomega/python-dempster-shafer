/**
 * Created on Nov 15, 2008
 */
package mass.measure;

import hypothesis.IDiscreteHypothesis;
import mass.IDiscreteMassFunction;

/**
 * Interface for uncertainty measures of mass functions over a discrete frame of discernment.
 * 
 * @author Thomas Reineking
 *
 */
public interface IUncertaintyMeasure {

	/**
	 * Calculates an uncertainty measure for the given mass function.
	 * 
	 * @param m mass function over a discrete frame of discernment
	 * @return The calculated uncertainty measure.
	 */
	public <S extends Comparable<S>, H extends IDiscreteHypothesis<S, H>, M extends IDiscreteMassFunction<S, H, M>> double measure(M m);
	
}
