/**
 * Nov 13, 2008
 */
package mass;

import hypothesis.IHypothesis;

import java.util.Random;
import java.util.TreeMap;



/**
 * Interface for all mass functions.
 * <p>
 * The interface uses self-bounding which is why implementing classes must provide parameterizations for <code>M</code> and <code>H</code>.
 *  
 * @author Thomas Reineking
 *
 */
public interface IMassFunction<H extends IHypothesis<H>, M extends IMassFunction<H, M>> extends Cloneable, Iterable<H> {
	
	/**
	 * Creates a deep copy of this mass function, i.e., a copy of each hypothesis is created.
	 * 
	 * @return A deep copy of this mass function.
	 */
	public M clone();
	
	/**
	 * Adds a hypothesis to this mass function with the specified mass value. In case the hypothesis already exists, the new mass value is added to the previous mass.
	 * 
	 * @param hypothesis a non-empty hypothesis
	 * @param mass the hypothesis' mass value (values must non-negative and the sum of all mass values must not exceed 1)
	 */
	public void add(H hypothesis, double mass);
	
	/**
	 * Removes a hypothesis and its mass from this mass function.
	 * 
	 * @param hypothesis a hypothesis
	 * @return The mass value associated with <code>hypothesis</code>. Returns 0 if <code>hypothesis</code> is not contained.
	 */
	public double remove(H hypothesis);
	
	/**
	 * Removes all hypotheses and mass values from this mass function.
	 */
	public void clear();
	
	
	/**
	 * Returns the mass value associated with <code>hypothesis</code>.
	 * 
	 * @param hypothesis a hypothesis
	 * @return The associated mass value in the interval [0,1]. Returns 0 if <code>hypothesis</code> is not contained.
	 */
	public double getMass(H hypothesis);
	
	/**
	 * Calculates the belief associated with <code>hypothesis</code>. The belief is defined as <latex> bel(A) = \sum_{B \subseteq A} m(B) </latex>.
	 * 
	 * @param hypothesis a hypothesis
	 * @return The calculated belief value in the interval [0,1]. Returns 0 if <code>hypothesis</code> is not contained.
	 */
	public double getBelief(H hypothesis);
	
	/**
	 * Calculates the commonality associated with <code>hypothesis</code>. The commonality is defined as <latex> q(A) = \sum_{A \subseteq B} m(B) </latex>.
	 * 
	 * @param hypothesis a hypothesis
	 * @return The calculated commonality value in the interval [0,1]. Returns 0 if <code>hypothesis</code> is not contained.
	 */
	public double getCommonality(H hypothesis);
	
	/**
	 * Calculates the plausibility associated with <code>hypothesis</code>. The plausibility is defined as <latex> pl(A) = \sum_{A \cap B \neq \emptyset} m(B) </latex>.
	 * 
	 * @param hypothesis a hypothesis
	 * @return The calculated plausibility value in the interval [0,1]. Returns 0 if <code>hypothesis</code> is not contained.
	 */
	public double getPlausibility(H hypothesis);
	
	/**
	 * Calculates the sum of all mass values. A sum of 1 indicates a normalized mass function.
	 * 
	 * @return The sum of all mass values in the interval [0,1].
	 * @see IMassFunction.isNormalized()
	 */
	public double getMassSum();
	
	
	/**
	 * Constructs the frame of discernment formed by all the hypotheses contained by this mass function. It is obtained by unifying all contained hypotheses.
	 * 
	 * @return The frame of discernment corresponding to the hypotheses contained by this mass function.
	 * @see IHypothesis.unite(IHypothesis)
	 */
	public H getFrameOfDiscernment();
	
	
	/**
	 * Conditions this mass function with <code>condition</code> based on Dempster's rule of conditioning.
	 * 
	 * @param condition a hypothesis
	 * @param normalize perform a normalization after combining the two mass functions
	 * @return The conditioned mass function.
	 */
	public M condition(H condition, boolean normalize);
	
	/**
	 * Conditions this mass function with <code>condition</code> based on Dempster's rule of conditioning.
	 * This is a convenience method for calling <code>condition(condition, true)</code>.
	 * 
	 * @param condition a hypothesis
	 * @param normalize perform a normalization after combining the two mass functions
	 * @return The conditioned mass function.
	 */
	public M condition(H condition);
	
	/**
	 * Performs a conjunctive combination of this mass function and the given one.
	 * If <code>normalized==true</code> then this corresponds to Dempster's combination rule. 
	 * 
	 * @param m another mass function
	 * @param normalize perform a normalization after combining the two mass functions
	 * @return A conjunctively combined mass function.
	 */
	public M combineConjunctive(M m, boolean normalize);
	
	/**
	 * Performs a conjunctive combination of this mass function and the given one followed by a normalization.
	 * This is a convenience method for calling <code>combineConjunctive(m, true)</code>.
	 * 
	 * @param m another mass function
	 * @return A normalized conjunctively combined mass function.
	 */
	public M combineConjunctive(M m);
	
	/**
	 * Performs a disjunctive combination of this mass function and the specified one. The result is normalized if the two original functions are normalized.
	 * 
	 * @param m another mass function
	 * @return A disjunctively combined mass function.
	 */
	public M combineDisjunctive(M m);
	
	/**
	 * Calculates the weight of conflict between this mass function and the given one.
	 * It is defined as the logarithm of the normalization constant in Dempster's rule of combination. 
	 *  
	 * @param m another mass function
	 * @return The calculated weight of conflict.
	 */
	public double getWeightOfConflict(M m);
	
	
	/**
	 * Returns <code>true</code> if and only if the sum of all mass values equals 1.
	 * 
	 * @return <code>true</code> if and only if the sum of all mass values equals 1.
	 */
	public boolean isNormalized();
	
	/**
	 * Returns <code>true</code> if and only if the absolute difference of the sum of all mass values and 1 is equal or less than <code>epsilon</code>.
	 * 
	 * @param epsilon tolerance
	 * @return <code>true</code> if and only if the absolute difference of the sum of all mass values and 1 is equal or less than <code>epsilon</code>.
	 */
	public boolean isNormalized(double epsilon);
	
	/**
	 * Normalizes this mass function so that the sum of all mass values equals 1.
	 */
	public void normalize();
	
	/**
	 * Prunes this mass function by removing all hypotheses whose mass value is less than <code>minMass</code>.
	 * No normalization is performed.
	 * 
	 * @param minMass the minimum mass value each hypothesis must have to remain
	 */
	public void prune(double minMass);
	
	
	/**
	 * Extends this mass function vacuously to <code>getFrameOfDiscernment() x space</code>.
	 * 
	 * @param space another space
	 * @return The vacuously extended mass function.
	 */
	public <E extends IHypothesis<E>> IJointMassFunction<H, E, ?> extendRight(E space);
	
	/**
	 * Extends this mass function vacuously to <code>space x getFrameOfDiscernment()</code>.
	 * 
	 * @param space another space
	 * @return The vacuously extended mass function.
	 */
	public <E extends IHypothesis<E>> IJointMassFunction<E, H, ?> extendLeft(E space);
	
	
	/**
	 * Copies all hypotheses and mass values to <code>dest</code>.
	 * 
	 * @param dest another mass function
	 */
	public void copy(IMassFunction<H, ?> dest);
	
	/**
	 * Creates a map of all hypotheses and their mass values.
	 * 
	 * @return The created map.
	 */
	public TreeMap<H, Double> toMap();
	
	/**
	 * Generates a random sample from all contained hypotheses.
	 * The sampling probability is equal to the mass of each hypothesis.
	 * 
	 * @param random source of randomness
	 * @return The generated sample.
	 */
	public H sample(Random random);
	
}
