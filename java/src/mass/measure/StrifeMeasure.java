/**
 * 
 */
package mass.measure;

import hypothesis.IDiscreteHypothesis;

import java.util.Map.Entry;

import mass.IDiscreteMassFunction;


/**
 * Calculates the strife measure for a given mass function.
 * 
 * @author Thomas Reineking
 *
 */
public class StrifeMeasure implements IUncertaintyMeasure {
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.measure.IUncertaintyMeasure#measure(net.sourceforge.jds.mass.IDiscreteMassFunction)
	 */
	@Override
	public <S extends Comparable<S>, H extends IDiscreteHypothesis<S,H>, M extends IDiscreteMassFunction<S, H, M>> double measure(M m) {
		double strife = 0;
		for (Entry<H, Double> a : m.toMap().entrySet()) {
			double sizeA = a.getKey().size();
			double sum = 0;
			for (Entry<H, Double> b : m.toMap().entrySet()) {
				a.getKey().isSuperSetOf(null);
				if (a.getKey().isSuperSetOf(b.getKey())) {
					double sizeB = b.getKey().size();
					sum += b.getValue() * sizeB / sizeA;
				} else if (b.getKey().isSuperSetOf(a.getKey()))
					sum += b.getValue();
			}
			strife += a.getValue() * Math.log(sum) / Math.log(2);
		}
		return -strife;
	}

}
