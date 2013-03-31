/**
 * Dec 16, 2008
 */
package mass.sampled;

import hypothesis.ContinuousHypothesis;

import java.util.Random;

/**
 * @author Thomas Reineking
 *
 */
public class ContinuousSampledMassFunction extends AbstractSampledMassFunction<ContinuousHypothesis, ContinuousSampledMassFunction> {

	public ContinuousSampledMassFunction(int sampleCount, Random random) {
		super(sampleCount, random);
	}
	
	public ContinuousSampledMassFunction(int sampleCount, Random random, ContinuousHypothesis hypothesis) {
		super(sampleCount, random, hypothesis);
	}
	
	@Override
	protected ContinuousSampledMassFunction createMassFunction(int sampleCount, Random random) {
		return new ContinuousSampledMassFunction(sampleCount, random);
	}

}
