/**
 * Dec 16, 2008
 */
package mass.exact;

import hypothesis.ContinuousHypothesis;

/**
 * Represents of mass function over a continuous frame of discernment.
 * 
 * 
 * @author Thomas Reineking
 *
 */
public class ContinuousMassFunction extends AbstractMassFunction<ContinuousHypothesis, ContinuousMassFunction> {

	public ContinuousMassFunction() {
		super();
	}
	
	public ContinuousMassFunction(ContinuousHypothesis hypothesis) {
		super(hypothesis);
	}
	
	
	@Override
	protected ContinuousMassFunction createMassFunction() {
		return new ContinuousMassFunction();
	}

}
