/**
 * Created on Nov 16, 2008
 */
package mass.sampled;

import hypothesis.IDiscreteHypothesis;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import mass.IDiscreteMassFunction;


/**
 * @author Thomas Reineking
 *
 */
public abstract class AbstractSampledDiscreteMassFunction<S extends Comparable<S>, H extends IDiscreteHypothesis<S, H>, M extends AbstractSampledDiscreteMassFunction<S, H, M>> extends AbstractSampledMassFunction<H, M> implements IDiscreteMassFunction<S, H, M> {

	abstract protected H createHypothesis(Collection<S> singletons);
	
	
	public AbstractSampledDiscreteMassFunction(int sampleCount, Random random) {
		super(sampleCount, random);
	}

	public AbstractSampledDiscreteMassFunction(int sampleCount, Random random, H hypothesis) {
		super(sampleCount, random, hypothesis);
	}
	
	
	@Override
	public M getPignisticTransformation() {
		M pignistic = clone();
		pignistic.clear();
		for (H h : samples) {
			int r = random.nextInt(h.size());
			for (S s : h) {
				if (r == 0) {
					pignistic.add(createHypothesis(Collections.singletonList(s)));
					break;
				} else
					r--;
			}
		}
		return pignistic;
	}
	
	
	/**
	 * Performs a first-order Markov update where this mass function expresses the belief about the current state and the model describes the state transition belief.
	 * 
	 * @param model the model describing the state transition belief
	 * @return The updated distribution.
	 */
	public M dynamicUpdate(ISampledTransitionModel<S, H, M> model) {
		M posterior = createMassFunction(sampleCount, new Random(random.nextLong()));
		for (H h : samples) {
			H union = createHypothesis(new LinkedList<S>());
			for (S s : h)
				union = union.unite(model.predict(s));
			posterior.samples.add(union);
		}
		return posterior;
	}
	
	@Override
	public H getMostPlausibleSingletons() {
		double maxPl = -1;
		H maxS = createHypothesis(new LinkedList<S>());
		for (S s : getFrameOfDiscernment()) {
			LinkedList<S> col = new LinkedList<S>();
			col.add(s);
			double pl = getPlausibility(createHypothesis(col));
			if (pl == maxPl)
				maxS.add(col);
			else if (pl > maxPl) {
				maxS = createHypothesis(col);
				maxPl = pl;
			}
		}
		return maxS;
	}
	
}
