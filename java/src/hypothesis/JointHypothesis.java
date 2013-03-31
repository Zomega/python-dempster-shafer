/**
 * Nov 11, 2008
 */
package hypothesis;


/**
 * Represents a hypothesis over the space <code>A x B</code>.
 * 
 * @author Thomas Reineking
 *
 */
public class JointHypothesis<A extends IHypothesis<A>, B extends IHypothesis<B>> implements IHypothesis<JointHypothesis<A, B>> {

	public final A h1;
	
	public final B h2;
	
	
	public JointHypothesis(A h1, B h2) {
		this.h1 = h1;
		this.h2 = h2;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public JointHypothesis<A, B> clone() {
		return new JointHypothesis<A, B>((A) h1.clone(), (B) h2.clone());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(JointHypothesis<A, B> o) {
		int diff1 = h1.compareTo(o.h1);
		if (diff1 != 0)
			return diff1;
		else
			return h2.compareTo(o.h2);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof JointHypothesis))
			return false;
		JointHypothesis<?, ?> j = (JointHypothesis<?, ?>) obj;
		return h1.equals(j.h1) && h2.equals(j.h2);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return h1.isEmpty() || h2.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#isSuperSetOf(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public boolean isSuperSetOf(JointHypothesis<A, B> hypothesis) {
		return h1.isSuperSetOf(hypothesis.h1) && h2.isSuperSetOf(hypothesis.h2);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#intersect(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public JointHypothesis<A, B> intersect(JointHypothesis<A, B> hypothesis) {
		return new JointHypothesis<A, B>((A) h1.intersect(hypothesis.h1), (B) h2.intersect(hypothesis.h2));
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#unite(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public JointHypothesis<A, B> unite(JointHypothesis<A, B> hypothesis) {
		return new JointHypothesis<A, B>((A) h1.unite(hypothesis.h1), (B) h2.unite(hypothesis.h2));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + h1 + " | " + h2 + ")";
	}
	
}
