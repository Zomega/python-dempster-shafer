/**
 * 
 */
package mass.measure;

import hypothesis.IDiscreteHypothesis;

import java.util.Map.Entry;

import mass.IDiscreteMassFunction;


/**
 * Calculates the discord measure for a given mass function.
 * 
 * @author Thomas Reineking
 *
 */
public class DiscordMeasure implements IUncertaintyMeasure {
	
	public <S extends Comparable<S>, H extends IDiscreteHypothesis<S,H>, M extends IDiscreteMassFunction<S, H, M>> double measure(M m) {
		double discord = 0;
		for (Entry<H, Double> a : m.toMap().entrySet()) {
			double sizeA = a.getKey().size();
			double sum = 0;
			for (Entry<H, Double> b : m.toMap().entrySet()) {
				if (a.getKey().isSuperSetOf(b.getKey()))
					sum += b.getValue();
				else if (b.getKey().isSuperSetOf(a.getKey())) {
					double sizeB = b.getKey().size();
					sum += b.getValue() * sizeA / sizeB;
				}
			}
			discord += a.getValue() * Math.log(sum) / Math.log(2);
		}
		return -discord;
	};
	
}
