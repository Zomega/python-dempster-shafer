/**
 * Nov 11, 2008
 */
package hypothesis;

import java.util.Collection;
import java.util.TreeSet;

import util.SetComparator;


/**
 * Implements a hypothesis as a set of singletons.
 *  
 * @author Thomas Reineking
 *
 */
public class Hypothesis<S extends Comparable<S>> extends AbstractDiscreteHypothesis<S, Hypothesis<S>> {

	private SetComparator<S> comparator;
	
	private final TreeSet<S> singletons = new TreeSet<S>();

	
	/**
	 * Creates an empty hypothesis.
	 */
	public Hypothesis() {
		// empty
	}
	
	/**
	 * Creates a hypothesis consisting of a single element.
	 * 
	 * @param singleton a singleton
	 */
	public Hypothesis(S singleton) {
		add(singleton);
	}
	
	/**
	 * Creates a hypothesis representing the set <code>singletons</code>.
	 * 
	 * @param singletons an array of singletons
	 */
	public Hypothesis(S... singletons) {
		for (S s : singletons)
			add(s);
	}
	
	/**
	 * Creates a hypothesis representing the set <code>singletons</code>.
	 * 
	 * @param singletons a collection of singletons
	 */
	public Hypothesis(Collection<? extends S> singletons) {
		this.singletons.addAll(singletons);
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Hypothesis<S> o) {
		if (comparator == null)
			comparator = new SetComparator<S>();
		return comparator.compare(singletons, ((Hypothesis<S>) o).singletons);
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.AbstractDiscreteHypothesis#exposeSingletons()
	 */
	@Override
	protected Collection<S> exposeSingletons() {
		return singletons;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.AbstractDiscreteHypothesis#clone()
	 */
	@Override
	public Hypothesis<S> clone() {
		return (Hypothesis<S>) super.clone();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.AbstractDiscreteHypothesis#unite(net.sourceforge.jds.hypothesis.AbstractDiscreteHypothesis)
	 */
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.AbstractDiscreteHypothesis#unite(net.sourceforge.jds.hypothesis.AbstractDiscreteHypothesis)
	 */
	@Override
	public Hypothesis<S> unite(Hypothesis<S> hypothesis) {
		return (Hypothesis<S>) super.unite(hypothesis);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.AbstractDiscreteHypothesis#intersect(net.sourceforge.jds.hypothesis.AbstractDiscreteHypothesis)
	 */
	@Override
	public Hypothesis<S> intersect(Hypothesis<S> hypothesis) {
		return (Hypothesis<S>) super.intersect(hypothesis);
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.AbstractDiscreteHypothesis#createHypothesis(java.util.Collection)
	 */
	@Override
	protected Hypothesis<S> createHypothesis(Collection<? extends S> singletons) {
		return new Hypothesis<S>(singletons);
	}
	
}
