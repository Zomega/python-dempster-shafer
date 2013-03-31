/**
 * Nov 13, 2008
 */
package mass.sampled;

import hypothesis.IHypothesis;
import hypothesis.JointHypothesis;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import mass.IMassFunction;
import util.MutableInteger;
import util.Tuple;


/**
 * @author Thomas Reineking
 *
 */
public abstract class AbstractSampledMassFunction<H extends IHypothesis<H>, M extends AbstractSampledMassFunction<H, M>> implements IMassFunction<H, M> {

	protected final ArrayList<H> samples;
	
	protected final int sampleCount;
	
	protected final Random random;
	
	
	abstract protected M createMassFunction(int sampleCount, Random random);
	
	
	public AbstractSampledMassFunction(int sampleCount, Random random) {
		this.sampleCount = sampleCount;
		this.random = random;
		samples = new ArrayList<H>(sampleCount);
	}
	
	public AbstractSampledMassFunction(int sampleCount, Random random, H hypothesis) {
		this(sampleCount, random);
		add(hypothesis, 1.0);
	}
	
	
	/**
	 * Returns the number of samples used by this mass function.
	 */
	public int getSampleCount() {
		return sampleCount;
	}
	
	/**
	 * Returns the instance of <tt>Random</tt> used by this mass function.
	 */
	public Random getRandom() {
		return random;
	}
	
	
	@Override
	public M clone() {
		M cloned = createMassFunction(sampleCount, new Random(random.nextLong()));
		for (H h : samples)
			cloned.samples.add(h.clone());
		return cloned;
	}
	
	public void add(H hypothesis) {
		samples.add(hypothesis);
	}
	
	@Override
	public void add(H hypothesis, double mass) {
		for (int i = 0; i < mass * sampleCount; i++) {
			samples.add(hypothesis);
		}
	};
	
	@Override
	public void clear() {
		samples.clear();
	}

	@Override
	public double remove(H hypothesis) {
		LinkedList<Integer> indices = new LinkedList<Integer>();
		for (int i = 0; i < samples.size(); i++) {
			if (samples.get(i).compareTo(hypothesis) == 0)
				indices.add(i);
		}
		Iterator<Integer> it = indices.descendingIterator();
		while (it.hasNext())
			samples.remove(it.next().intValue());
		return ((double) indices.size()) / sampleCount;
	}


	@Override
	public Iterator<H> iterator() {
		TreeSet<H> hypotheses = new TreeSet<H>();
		for (H h : samples) {
			hypotheses.add(h);
		}
		return hypotheses.iterator();
	}
	
	public ArrayList<H> getSamples() {
		ArrayList<H> list = new ArrayList<H>(sampleCount);
		for (H h : samples)
			list.add(h.clone());
		return list;
	}

	@Override
	public double getBelief(H hypothesis) {
		int c = 0;
		for (H h : samples) {
			if (hypothesis.isSuperSetOf(h))
				c++;
		}
		return ((double) c) / sampleCount;
	}


	@Override
	public double getCommonality(H hypothesis) {
		int c = 0;
		for (H h : samples) {
			if (h.isSuperSetOf(hypothesis))
				c++;
		}
		return ((double) c) / sampleCount;
	}


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


	@Override
	public double getMass(H hypothesis) {
		int c = 0;
		for (H h : samples) {
			if (h.compareTo(hypothesis) == 0)
				c++;
		}
		return ((double) c) / sampleCount;
	}


	@Override
	public double getPlausibility(H hypothesis) {
		int c = 0;
		for (H h : samples) {
			if (!h.intersect(hypothesis).isEmpty())
				c++;
		}
		return ((double) c) / sampleCount;
	}

	
	@Override
	public double getMassSum() {
		return ((double) samples.size()) / sampleCount;
	}
	

	@Override
	public boolean isNormalized() {
		return samples.size() == sampleCount;
	}


	@Override
	public boolean isNormalized(double epsilon) {
		return 1.0 - (((double) samples.size()) / sampleCount) <= epsilon;
	}


	@Override
	public void normalize() {
		scale(sampleCount);
	}


	@Override
	public void prune(double minMass) {
		for (Entry<H, Double> entry : toMap().entrySet()) {
			if (entry.getValue() < minMass)
				remove(entry.getKey());
		}
	}
	
	
	@Override
	public M combineConjunctive(M m, boolean normalize) {
		M combination = createMassFunction(sampleCount, new Random(random.nextLong()));
		M clone = m.clone();
		clone.scale(samples.size());
		Collections.shuffle(samples, random);
		Collections.shuffle(clone.samples, clone.random);
		for (int i = 0; i < samples.size(); i++) {
			H intersection = samples.get(i).intersect(clone.samples.get(i));
			if (!intersection.isEmpty())
				combination.samples.add(intersection);
		}
		if (normalize)
			combination.normalize();
		return combination;
	};
	
	/**
	 * Performs a (normalized) conjunctive combination of this mass function and the given one based on importance sampling.
	 * Usually yields better a approximation compared to the default combination method, especially in case of highly conflicting evidence.
	 * However, it performs generally slower than the default combination.
	 * 
	 * @param m another mass function
	 * @return A normalized conjunctively combined mass function.
	 */
	
	public M combineConjunctiveImportanceSampling(M m) {
		ArrayList<Tuple<H, Double>> weightedSamples = new ArrayList<Tuple<H, Double>>();
		TreeMap<H, Double> plCache = new TreeMap<H, Double>();
		double plSum = 0;
		for (H h1 : samples) {
			Collections.shuffle(m.samples, m.random);
			for (H h2 : m.samples) {
				H intersection = h1.intersect(h2);
				if (!intersection.isEmpty()) {
					Double pl = plCache.get(h1);
					if (pl == null) {
						pl = m.getPlausibility(h1);
						plCache.put(h1, pl);
					}
					plSum += pl;
					weightedSamples.add(new Tuple<H, Double>(intersection, pl));
					break;
				}
			}
		}
		if (weightedSamples.isEmpty())
			return null;
		M combination = createMassFunction(sampleCount, new Random(random.nextLong()));
		for (int i = 0; i < sampleCount; i++) {
			double r = random.nextDouble() * plSum;
			double c = 0;
			for (Tuple<H, Double> entry : weightedSamples) {
				c += entry.b;
				if (c >= r) {
					combination.samples.add(entry.a);
					break;
				}
			}
		}
		return combination;
	};
	
	
	@Override
	public M combineConjunctive(M m) {
		return combineConjunctive(m, true);
	};
	
	
	@Override
	public M combineDisjunctive(M m) {
		M combination = createMassFunction(sampleCount, new Random(random.nextLong()));
		M clone = m.clone();
		clone.scale(samples.size());
		Collections.shuffle(samples, random);
		Collections.shuffle(clone.samples, clone.random);
		for (int i = 0; i < samples.size(); i++)
			combination.samples.add(samples.get(i).unite(clone.samples.get(i)));
		return combination;
	};
	
	@Override
	public M condition(H condition, boolean normalize) {
		M m = createMassFunction(samples.size(), new Random(random.nextLong()));
		m.add(condition, 1.0);
		return combineConjunctive(m, normalize);
	};
	
	@Override
	public M condition(H condition) {
		return condition(condition, true);
	};
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.mass.IMassFunction#getWeightOfConflict(net.sourceforge.jds.mass.IMassFunction)
	 */
	@Override
	public double getWeightOfConflict(M m) {
		double emptyMass = 0;
		M clone = m.clone();
		clone.scale(samples.size());
		Collections.shuffle(samples, random);
		Collections.shuffle(clone.samples, clone.random);
		for (int i = 0; i < samples.size(); i++) {
			H intersection = samples.get(i).intersect(clone.samples.get(i));
			if (intersection.isEmpty())
				emptyMass += 1.0 / sampleCount;
		}
		return -Math.log(1.0 - emptyMass);
	};
	
	@Override
	public <E extends IHypothesis<E>> JointSampledMassFunction<E, H> extendLeft(E space) {
		JointSampledMassFunction<E, H> extended = new JointSampledMassFunction<E, H>(sampleCount, random);
		for (H h : samples)
			extended.samples.add(new JointHypothesis<E, H>(space, h));
		return extended;
	};
	
	@Override
	public <E extends IHypothesis<E>> JointSampledMassFunction<H, E> extendRight(E space) {
		JointSampledMassFunction<H, E> extended = new JointSampledMassFunction<H, E>(sampleCount, random);
		for (H h : samples)
			extended.samples.add(new JointHypothesis<H, E>(h, space));
		return extended;
	};

	
	@Override
	public void copy(IMassFunction<H, ?> m) {
		m.clear();
		if (m instanceof AbstractSampledMassFunction) {
			for (H h: samples)
				((AbstractSampledMassFunction<H, ?>) m).samples.add(h);
		} else {
			for (H h: samples)
				m.add(h, 1.0 / sampleCount);
		}
	};
	
	@Override
	public String toString() {
		final TreeMap<H, MutableInteger> frequencies = new TreeMap<H, MutableInteger>();
		for (H h : samples) {
			MutableInteger frequency = frequencies.get(h);
			if (frequency == null)
				frequencies.put(h, new MutableInteger(1));
			else
				frequency.value++;
		}
		ArrayList<H> sorted = new ArrayList<H>(frequencies.keySet());
		Collections.sort(sorted, new Comparator<H>() {
			
			@Override
			public int compare(H o1, H o2) {
				int diff = frequencies.get(o2).value - frequencies.get(o1).value;
				if (diff != 0)
					return diff;
				else
					return o1.compareTo(o2);
			};
			
		});
		StringBuilder s = new StringBuilder();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		for (H h : sorted)
			s.append(h + ":" + ((double) frequencies.get(h).value) / sampleCount + ", ");
		return s.toString();
	}
	
	
	@Override
	public TreeMap<H, Double> toMap() {
		TreeMap<H, MutableInteger> counters = new TreeMap<H, MutableInteger>();
		for (H h : samples) {
			MutableInteger counter = counters.get(h);
			if (counter == null)
				counters.put(h, counter = new MutableInteger(0));
			counter.value++;
		}
		TreeMap<H, Double> massMap = new TreeMap<H, Double>();
		for (Entry<H, MutableInteger> entry : counters.entrySet())
			massMap.put(entry.getKey(), ((double) entry.getValue().value) / sampleCount);
		return massMap;
	}
	
	
	@Override
	public H sample(Random random) {
		return samples.get(random.nextInt(samples.size()));
	}
	
	
	protected void scale(int count) {
		if (samples.size() > count) {
			// shrink
			Collections.shuffle(samples, random);
			for (int i = samples.size() - 1; i >= count; i--)
				samples.remove(i);
			samples.trimToSize();
		} else if (samples.size() < count && !samples.isEmpty()) {
			// enlarge
			LinkedList<H> add = new LinkedList<H>();
			Collections.shuffle(samples, random);
			int missing = count - samples.size();
			for (int i = 0; i < missing; i++)
				add.add(samples.get(i % samples.size()));
			samples.addAll(add);
		}
	}
	
}
