/**
 * Nov 12, 2008
 */
package mass.exact;

import hypothesis.Hypothesis;

import java.util.Collection;




/**
 * Represents a mass function over a discrete frame of discernment.
 * 
 * @author Thomas Reineking
 *
 */
public class MassFunction<S extends Comparable<S>> extends AbstractDiscreteMassFunction<S, Hypothesis<S>, MassFunction<S>> {

	/**
	 * Creates an empty mass function.
	 */
	public MassFunction() {
		super();
	}
	
	/**
	 * Creates a mass function that assigns all mass to <code>hypothesis</code>.
	 * 
	 * @param hypothesis a hypothesis
	 */
	public MassFunction(Hypothesis<S> hypothesis) {
		super(hypothesis);
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.exact.AbstractMassFunction#createMassFunction()
	 */
	@Override
	protected MassFunction<S> createMassFunction() {
		return new MassFunction<S>();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.exact.AbstractDiscreteMassFunction#createHypothesis(java.util.Collection)
	 */
	@Override
	protected Hypothesis<S> createHypothesis(Collection<S> singletons) {
		return new Hypothesis<S>(singletons);
	};
	
}
