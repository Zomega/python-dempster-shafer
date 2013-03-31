/**
 * Nov 11, 2008
 */
package hypothesis;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;



/**
 * Abstract class for a hypothesis that represents a set of singletons.
 * 
 * @author Thomas Reineking
 *
 */
public abstract class AbstractDiscreteHypothesis<S extends Comparable<S>, H extends AbstractDiscreteHypothesis<S, H>> implements IDiscreteHypothesis<S, H> {
	
	abstract protected H createHypothesis(Collection<? extends S> singletons);
	
	abstract protected Collection<S> exposeSingletons();
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IDiscreteHypothesis#add(java.util.Collection)
	 */
	@Override
	public void add(Collection<? extends S> singletons) {
		exposeSingletons().addAll(singletons);
	};
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IDiscreteHypothesis#remove(java.util.Collection)
	 */
	@Override
	public boolean remove(Collection<? extends S> singletons) {
		return exposeSingletons().removeAll(singletons);
	};
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IDiscreteHypothesis#clear()
	 */
	@Override
	public void clear() {
		exposeSingletons().clear();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IDiscreteHypothesis#contains(java.util.Collection)
	 */
	@Override
	public boolean contains(Collection<? extends S> singletons) {
		return exposeSingletons().containsAll(singletons);
	};
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return exposeSingletons().isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IDiscreteHypothesis#size()
	 */
	@Override
	public int size() {
		return exposeSingletons().size();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<S> iterator() {
		return exposeSingletons().iterator();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#isSuperSetOf(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public boolean isSuperSetOf(H hypothesis) {
		if (hypothesis instanceof AbstractDiscreteHypothesis)
			return exposeSingletons().containsAll(hypothesis.exposeSingletons());
		else
			return false;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#intersect(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public H intersect(H hypothesis) {
		H intersection = clone();
		intersection.exposeSingletons().retainAll(hypothesis.exposeSingletons());
		return intersection;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#unite(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public H unite(H hypothesis) {
		H union = clone();
		union.exposeSingletons().addAll(hypothesis.exposeSingletons());
		return union;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public H clone() {
		return createHypothesis(exposeSingletons());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return exposeSingletons().toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IDiscreteHypothesis))
			return false;
		Iterator<?> it1 = iterator();
		Iterator<?> it2 = ((IDiscreteHypothesis<?, ?>) obj).iterator();
		while (it1.hasNext() && it2.hasNext()) {
			if (!it1.next().equals(it2.next()))
				return false;
		}
		return !it1.hasNext() && !it2.hasNext();
	}
	
	
	/**
	 * Adds the singleton to this hypothesis.
	 * 
	 * @param singleton a singleton
	 */
	public void add(S singleton) {
		exposeSingletons().add(singleton);
	};
	
	/**
	 * Adds all singletons to this hypothesis.
	 * 
	 * @param singletons an array of singletons
	 */
	public void add(S... singletons) {
		for (S s : singletons)
			exposeSingletons().add(s);
	};
	
	
	/**
	 * Returns <code>true</code> if and only if <code>singleton</code> is contained by this hypothesis.
	 * 
	 * @param singletons a singleton
	 * 
	 * @return <code>true</code> if and only if <code>singleton</code> is contained by this hypothesis.
	 */
	public boolean contains(S singleton) {
		return exposeSingletons().contains(singleton);
	}
	
	/**
	 * Returns <code>true</code> if and only if all elements of <code>singletons</code> are contained
	 * by this hypothesis.
	 * 
	 * @param singletons an array of singletons
	 * 
	 * @return <code>true</code> if and only if all elements of <code>singletons</code> are contained
	 * by this hypothesis.
	 */
	public boolean contains(S... singletons) {
		for (S s : singletons)
			if (!exposeSingletons().contains(s))
				return false;
		return true;
	};
	
	
	/**
	 * Remove the singleton from this hypothesis.
	 * 
	 * @param singleton a singleton
	 * @return <code>true</code> if and only if the singleton was removed.
	 */
	public boolean remove(S singleton) {
		return exposeSingletons().remove(singleton);
	}
	
	/**
	 * Removes an array of singletons from this hypothesis.
	 * 
	 * @param singletons an array of singletons
	 * @return <code>true</code> if and only if at least one singleton was removed.
	 */
	public boolean remove(S... singletons) {
		boolean result = false;
		for (S s : singletons)
			result |= exposeSingletons().remove(s);
		return result;
	};
	
	
	/**
	 * Constructs the power set of the set represented by this hypothesis.
	 * 
	 * @return The constructed power set.
	 */
	public LinkedList<H> getPowerSet() {
		LinkedList<H> powerSet = new LinkedList<H>();
		for (S element : this) {
			LinkedList<H> extended = new LinkedList<H>();
			for (H base : powerSet) {
				H extendedHyp = base.clone();
				extendedHyp.exposeSingletons().add(element);
				extended.add(extendedHyp);
			}
			powerSet.addAll(extended);
			H singleton = createHypothesis(Collections.singleton(element));
			powerSet.add(singleton);
		}
		return powerSet;
	}
	
}
