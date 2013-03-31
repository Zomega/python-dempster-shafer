/**
 * Nov 11, 2008
 */
package mass.exact;

import hypothesis.IHypothesis;
import hypothesis.JointHypothesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import mass.IMassFunction;



/**
 * @author Thomas Reineking
 *
 */
public abstract class AbstractMassFunction<H extends IHypothesis<H>, M extends AbstractMassFunction<H, M>> implements IMassFunction<H, M> {

	protected final TreeMap<H, Double> entries = new TreeMap<H, Double>();
	
	
	abstract protected M createMassFunction();
	
	
	public AbstractMassFunction() {
		// empty
	}
	
	public AbstractMassFunction(H hypothesis) {
		add(hypothesis, 1.0);
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<H> iterator() {
		return entries.keySet().iterator();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#copy(net.sourceforge.jds.mass.IMassFunction)
	 */
	@Override
	public void copy(IMassFunction<H, ?> m) {
		m.clear();
		for (Map.Entry<H, Double> entry : entries.entrySet())
			m.add(entry.getKey(), entry.getValue());
	};
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public M clone() {
		M cloned = createMassFunction();
		for (Map.Entry<H, Double> entry : entries.entrySet())
			cloned.entries.put(entry.getKey().clone(), entry.getValue());
		return cloned;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ArrayList<Entry<H, Double>> temp = new ArrayList<Entry<H, Double>>(entries.entrySet());
		Collections.sort(temp, new Comparator<Entry<H, Double>>() {
			
			@Override
			public int compare(Entry<H, Double> o1, Entry<H, Double> o2) {
				double diff = o2.getValue() - o1.getValue();
				if (diff != 0)
					return (int) Math.signum(diff);
				else
					return o1.getKey().compareTo(o2.getKey());
			}
			
		});
		return temp.toString();
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#add(net.sourceforge.jds.hypothesis.IHypothesis, double)
	 */
	@Override
	public void add(H hypothesis, double mass) {
		if (hypothesis.isEmpty())
			throw new IllegalArgumentException("empty hypotheses are not allowed");
		if (Double.isNaN(mass))
			throw new IllegalArgumentException("mass value is not a number");
//		if (mass < 0)
//			throw new IllegalArgumentException("mass value must not be negative");
//		if (getMassSum() + mass > 1.0)
//			throw new IllegalArgumentException("sum of all mass values must not exceed 1");
		// add possible existing belief
		Double b = entries.get(hypothesis);
		entries.put(hypothesis, mass + (b != null ? b : 0.0));
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#remove(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public double remove(H hypothesis) {
		Double mass = entries.remove(hypothesis);
		if (mass != null)
			return mass;
		else
			return 0;
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#clear()
	 */
	@Override
	public void clear() {
		entries.clear();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#getMass(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public double getMass(H hypothesis) {
		Double mass = entries.get(hypothesis);
		if (mass == null)
			return 0;
		else
			return mass;
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#getBelief(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public double getBelief(H hypothesis) {
		double belief = 0.0;
		for (Map.Entry<H, Double> entry : entries.entrySet()) {
			if (hypothesis.isSuperSetOf(entry.getKey()))
				belief += entry.getValue();
		}
		return belief;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#getCommonality(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public double getCommonality(H hypothesis) {
		double commonality = 0.0;
		for (Map.Entry<H, Double> entry : entries.entrySet()) {
			if (entry.getKey().isSuperSetOf(hypothesis))
				commonality += entry.getValue();
		}
		return commonality;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#getPlausibility(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public double getPlausibility(H hypothesis) {
		double plausibility = 0.0;
		for (Map.Entry<H, Double> entry : entries.entrySet()) {
			if (!entry.getKey().intersect(hypothesis).isEmpty())
				plausibility += entry.getValue();
		}
		return plausibility;
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#getMassSum()
	 */
	@Override
	public double getMassSum() {
		double sum = 0;
		for (double mass : entries.values())
			sum += mass;
		return sum;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#getFrameOfDiscernment()
	 */
	@Override
	public H getFrameOfDiscernment() {
		H theta = null;
		for (H h : this) {
			if (theta == null)
				theta = h;
			else
				theta = theta.unite(h);
		}
		return theta;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#condition(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public M condition(H condition) {
		return condition(condition, true);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#condition(net.sourceforge.jds.hypothesis.IHypothesis, boolean)
	 */
	@Override
	public M condition(H condition, boolean normalize) {
		M copy = createMassFunction();
		copy.add(condition, 1.0);
		return combineConjunctive(copy, normalize);
	};
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#combineConjunctive(net.sourceforge.jds.mass.IMassFunction, boolean)
	 */
	@Override
	public M combineConjunctive(M m, boolean normalize) {
		M combination = createMassFunction();
		for (Map.Entry<H, Double> entry1 : entries.entrySet()) {
			for (H h2 : m) {
				H combinedHypothesis = entry1.getKey().intersect(h2);
				if (!combinedHypothesis.isEmpty()) {
					double mass = entry1.getValue() * m.getMass(h2);
					Double previous = combination.entries.get(combinedHypothesis);
					if (previous != null)
						mass += previous;
					combination.entries.put(combinedHypothesis, mass);
				}
			}
		}
		if (normalize)
			combination.normalize();
		return combination;
	};
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#combineConjunctive(net.sourceforge.jds.mass.IMassFunction)
	 */
	@Override
	public M combineConjunctive(M m) {
		return combineConjunctive(m, true);
	};
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#combineDisjunctive(net.sourceforge.jds.mass.IMassFunction)
	 */
	@Override
	public M combineDisjunctive(M m) {
		M combination = createMassFunction();
		for (Map.Entry<H, Double> entry1 : entries.entrySet()) {
			for (H h2 : m) {
				H combinedHypothesis = entry1.getKey().unite(h2);
				double mass = entry1.getValue() * m.getMass(h2);
				Double previous = combination.entries.get(combinedHypothesis);
				if (previous != null)
					mass += previous;
				combination.entries.put(combinedHypothesis, mass);
			}
		}
		return combination;
	};
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#getWeightOfConflict(net.sourceforge.jds.mass.IMassFunction)
	 */
	@Override
	public double getWeightOfConflict(M m) {
		double emptyMass = 0;
		for (Map.Entry<H, Double> entry1 : entries.entrySet()) {
			for (H h2 : m) {
				if (entry1.getKey().intersect(h2).isEmpty())
					emptyMass += entry1.getValue() * m.getMass(h2);
			}
		}
		return -Math.log(1.0 - emptyMass);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#isNormalized()
	 */
	@Override
	public boolean isNormalized() {
		return isNormalized(0);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#isNormalized(double)
	 */
	@Override
	public boolean isNormalized(double epsilon) {
		double sum = 0.0;
		for (Double mass : entries.values())
			sum += mass;
		return Math.abs(sum - 1.0) <= epsilon;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#normalize()
	 */
	@Override
	public void normalize() {
		double sum = 0.0;
		for (Double mass : entries.values())
			sum += mass;
		if (sum != 1.0) {
			M cloned = clone();
			clear();
			for (Map.Entry<H, Double> entry : cloned.entries.entrySet())
				entries.put(entry.getKey(), entry.getValue() / sum);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#prune(double)
	 */
	@Override
	public void prune(double minMass) {
		M cloned = clone();
		for (Map.Entry<H, Double> entry : cloned.entries.entrySet()) {
			if (entry.getValue() < minMass)
				remove(entry.getKey());
		}
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#extendRight(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public <E extends IHypothesis<E>> JointMassFunction<H, E> extendRight(E space) {
		JointMassFunction<H, E> extended = new JointMassFunction<H, E>();
		for (Map.Entry<H, Double> entry : entries.entrySet())
			extended.add(new JointHypothesis<H, E>(entry.getKey(), space), entry.getValue());
		return extended;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#extendLeft(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public <E extends IHypothesis<E>> JointMassFunction<E, H> extendLeft(E space) {
		JointMassFunction<E, H> extended = new JointMassFunction<E, H>();
		for (Map.Entry<H, Double> entry : entries.entrySet())
			extended.add(new JointHypothesis<E, H>(space, entry.getKey()), entry.getValue());
		return extended;
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#toMap()
	 */
	@Override
	public TreeMap<H, Double> toMap() {
		TreeMap<H, Double> map = new TreeMap<H, Double>();
		for (Map.Entry<H, Double> entry : entries.entrySet())
			map.put(entry.getKey().clone(), entry.getValue());
		return map;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#sample(java.util.Random)
	 */
	@Override
	public H sample(Random random) {
		double r = random.nextDouble() * getMassSum();
		double sum = 0;
		for (Map.Entry<H, Double> entry : entries.entrySet()) {
			sum += entry.getValue();
			if (sum >= r)
				return entry.getKey();
		}
		return null;
	}
	
	
	/**
	 * Generates a list containing <code>number</code> samples.
	 * 
	 * @param count the number of samples to be generated
	 * @param random source of randomness
	 * @return The generated sample list.
	 */
	public ArrayList<H> sample(int count, Random random) {
		ArrayList<H> samples = new ArrayList<H>();
		for (int i = 0; i < count; i++) {
			double r = random.nextDouble();
			for (Entry<H, Double> e : entries.entrySet()) {
				r -= e.getValue();
				if (r < 0) {
					samples.add(e.getKey());
					break;
				}
			}
		}
		return samples;
	}
	
	
}
