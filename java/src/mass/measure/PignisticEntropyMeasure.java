/**
 * Created on Nov 15, 2008
 */
package mass.measure;

import hypothesis.IDiscreteHypothesis;

import java.util.Map.Entry;

import mass.IDiscreteMassFunction;



/**
 * Calculates the entropy of the pignistic transformation for a given mass function.
 * 
 * @author Thomas Reineking
 *
 */
public class PignisticEntropyMeasure implements IUncertaintyMeasure {

	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.measure.IUncertaintyMeasure#measure(net.sourceforge.jds.mass.IDiscreteMassFunction)
	 */
	@Override
	public <S extends Comparable<S>, H extends IDiscreteHypothesis<S,H>, M extends IDiscreteMassFunction<S, H, M>> double measure(M m) {
		double entropy = 0;
		for (Entry<?, Double> entry : m.getPignisticTransformation().toMap().entrySet()) {
			entropy -= entry.getValue() * Math.log(entry.getValue()) / Math.log(2);
		}
		return entropy;
	}
	
}
