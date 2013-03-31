/**
 * Nov 12, 2008
 */
package mass.exact;

import hypothesis.IHypothesis;
import hypothesis.JointHypothesis;

import java.util.Map;

import mass.IJointMassFunction;
import mass.IMassFunction;


/**
 * Represents a mass function defined over the space AxB.
 * 
 * @author Thomas Reineking
 *
 */
public class JointMassFunction<A extends IHypothesis<A>, B extends IHypothesis<B>> extends AbstractMassFunction<JointHypothesis<A, B>, JointMassFunction<A, B>> implements IJointMassFunction<A, B, JointMassFunction<A, B>> {

	public JointMassFunction() {
		super();
	}
	
	public JointMassFunction(JointHypothesis<A, B> hypothesis) {
		super(hypothesis);
	}
	
	public JointMassFunction(A hypothesis1, B hypothesis2) {
		super(new JointHypothesis<A, B>(hypothesis1, hypothesis2));
	}

	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.exact.AbstractMassFunction#createMassFunction()
	 */
	@Override
	protected JointMassFunction<A, B> createMassFunction() {
		return new JointMassFunction<A, B>();
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IJointMassFunction#projectLeft(net.sourceforge.jds.mass.IMassFunction)
	 */
	@Override
	public void projectLeft(IMassFunction<A, ?> dest) {
		for (Map.Entry<JointHypothesis<A, B>, Double> entry : entries.entrySet())
			dest.add(entry.getKey().h1, entry.getValue());
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IJointMassFunction#projectRight(net.sourceforge.jds.mass.IMassFunction)
	 */
	@Override
	public void projectRight(IMassFunction<B, ?> dest) {
		for (Map.Entry<JointHypothesis<A, B>, Double> entry : entries.entrySet())
			dest.add(entry.getKey().h2, entry.getValue());
	}
	
}
