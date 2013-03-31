/**
 * Created on Nov 16, 2008
 */
package mass.sampled;

import hypothesis.IHypothesis;
import hypothesis.JointHypothesis;

import java.util.Random;

import mass.IJointMassFunction;
import mass.IMassFunction;


/**
 * @author Thomas Reineking
 *
 */
public class JointSampledMassFunction<A extends IHypothesis<A>, B extends IHypothesis<B>> extends AbstractSampledMassFunction<JointHypothesis<A, B>, JointSampledMassFunction<A, B>> implements IJointMassFunction<A, B, JointSampledMassFunction<A, B>> {

	public JointSampledMassFunction(int sampleCount, Random random) {
		super(sampleCount, random);
	}

	
	@Override
	protected JointSampledMassFunction<A, B> createMassFunction(int sampleCount, Random random) {
		return new JointSampledMassFunction<A, B>(sampleCount, random);
	}
	
	
	public void projectLeft(IMassFunction<A, ?> dest) {
		if (dest instanceof AbstractSampledMassFunction) {
			for (JointHypothesis<A, B> h : samples)
				((AbstractSampledMassFunction<A, ?>) dest).samples.add(h.h1);
		} else {
			for (JointHypothesis<A, B> h : samples)
				dest.add(h.h1, 1.0 / sampleCount);
		}
	}
	
	public void projectRight(IMassFunction<B, ?> dest) {
		if (dest instanceof AbstractSampledMassFunction) {
			for (JointHypothesis<A, B> h : samples)
				((AbstractSampledMassFunction<B, ?>) dest).samples.add(h.h2);
		} else {
			for (JointHypothesis<A, B> h : samples)
				dest.add(h.h2, 1.0 / sampleCount);
		}
	}

}
