/**
 * Mar 26, 2009
 */
package mass.measure;

import hypothesis.IDiscreteHypothesis;
import mass.IDiscreteMassFunction;

/**
 * Calculates the local conflict measure for a given mass function.
 * <p>
 * Based on Pal & Bezdek 1993: Uncertainty measures for evidential reasoning II: A new measure of total uncertainty.
 * 
 * @author Thomas Reineking
 * 
 */
public class LocalConflictMeasure implements IUncertaintyMeasure {

	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.measure.IUncertaintyMeasure#measure(net.sourceforge.jds.mass.IDiscreteMassFunction)
	 */
	@Override
	public <S extends Comparable<S>, H extends IDiscreteHypothesis<S, H>, M extends IDiscreteMassFunction<S, H, M>> double measure(M m) {
		double lc = 0.0;
		for (H h : m) {
			double mass = m.getMass(h);
			if (mass > 0)
				lc += mass * Math.log(h.size() / mass) / Math.log(2);
		}
		return lc;
	};
	
}
