/**
 * Created on Nov 16, 2008
 */
package mass.sampled;

import hypothesis.Hypothesis;

import java.util.Collection;
import java.util.Random;


/**
 * Represents a sampling-based mass function over a discrete frame of discernment.
 * The number of samples assigned to a hypothesis is approximately proportional to that hypothesis' mass.
 * 
 * @author Thomas Reineking
 *
 */
public class SampledMassFunction<S extends Comparable<S>> extends AbstractSampledDiscreteMassFunction<S, Hypothesis<S>, SampledMassFunction<S>> {

	/**
	 * Creates an empty mass function using <code>sampleCount</code> samples.
	 * 
	 * @param sampleCount the number of samples
	 * @param random source of randomness
	 */
	public SampledMassFunction(int sampleCount, Random random) {
		super(sampleCount, random);
	}
	
	/**
	 * Creates a mass function using <code>sampleCount</code> samples that assigns all mass to <code>hypothesis</code>.
	 * 
	 * @param sampleCount the number of samples
	 * @param random source of randomness
	 * @param hypothesis a hypothesis
	 */
	public SampledMassFunction(int sampleCount, Random random, Hypothesis<S> hypothesis) {
		super(sampleCount, random, hypothesis);
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.sampled.AbstractSampledMassFunction#createMassFunction(int, java.util.Random)
	 */
	@Override
	protected SampledMassFunction<S> createMassFunction(int sampleCount, Random random) {
		return new SampledMassFunction<S>(sampleCount, random);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.sampled.AbstractSampledDiscreteMassFunction#createHypothesis(java.util.Collection)
	 */
	@Override
	protected Hypothesis<S> createHypothesis(Collection<S> singletons) {
		return new Hypothesis<S>(singletons);
	};
	
}
