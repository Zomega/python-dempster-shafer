/**
 * Dec 16, 2008
 */
package hypothesis;


/**
 * Represents a hypothesis in the continuous frame of discernment R^n (real numbers) as a hyper-cuboid.
 * 
 * Note that this representation is generally not closed for the union operation.
 * 
 * @author Thomas Reineking
 *
 */
public class ContinuousHypothesis implements IHypothesis<ContinuousHypothesis> {

	private double[] lowerBounds, upperBounds;	// both inclusive
	
	
	/**
	 * Creates a one-dimensional interval hypothesis.
	 * 
	 * @param lowerBound lower interval bound
	 * @param upperBound upper interval bound
	 */
	public ContinuousHypothesis(double lowerBound, double upperBound) {
		this(new double[] {lowerBound}, new double[] {upperBound});
	}
	
	/**
	 * Creates an n-dimensional hyper-cuboid hypothesis from the intervals that are specified for each dimension.
	 * 
	 * @param lowerBounds the lower bounds for each dimension
	 * @param upperBounds the upper bounds for each dimension
	 */
	public ContinuousHypothesis(double[] lowerBounds, double[] upperBounds) {
		if (lowerBounds.length != upperBounds.length)
			throw new IllegalArgumentException("number of lower and upper bounds must be equal");
		if (lowerBounds.length == 0)
			throw new IllegalArgumentException("no bounds specified");
		this.lowerBounds = new double[lowerBounds.length];
		this.upperBounds = new double[upperBounds.length];
		for (int i = 0; i < lowerBounds.length; i++) {
			if (Double.isNaN(lowerBounds[i]) || Double.isNaN(upperBounds[i]))
				throw new IllegalArgumentException("bounds must be real numbers");
//			if (lowerBounds[i] > upperBounds[i])
//				throw new IllegalArgumentException("lower bounds must not be greater than upper bounds");
			this.lowerBounds[i] = lowerBounds[i];
			this.upperBounds[i] = upperBounds[i];
		}
	}
	
	
	public int getDimensions() {
		return lowerBounds.length;
	}
	
	public double getLowerBound(int dim) {
		return lowerBounds[dim];
	}
	
	public double getUpperBound(int dim) {
		return upperBounds[dim];
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		for (int i = 0; i < lowerBounds.length; i++) {
			if (upperBounds[i] < lowerBounds[i])
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#isSuperSetOf(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public boolean isSuperSetOf(ContinuousHypothesis hypothesis) {
		checkDimensions(this, hypothesis);
		for (int i = 0; i < lowerBounds.length; i++) {
			if (lowerBounds[i] > hypothesis.lowerBounds[i] || upperBounds[i] < hypothesis.upperBounds[i])
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#intersect(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public ContinuousHypothesis intersect(ContinuousHypothesis hypothesis) {
		checkDimensions(this, hypothesis);
		double[] intLowerBounds = new double[lowerBounds.length];
		double[] intUpperBounds = new double[upperBounds.length];
		for (int i = 0; i < lowerBounds.length; i++) {
			intLowerBounds[i] = Math.max(lowerBounds[i], hypothesis.lowerBounds[i]);
			intUpperBounds[i] = Math.min(upperBounds[i], hypothesis.upperBounds[i]);
		}
		return new ContinuousHypothesis(intLowerBounds, intUpperBounds);
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jds.hypothesis.IHypothesis#unite(net.sourceforge.jds.hypothesis.IHypothesis)
	 */
	@Override
	public ContinuousHypothesis unite(ContinuousHypothesis hypothesis) {
		checkDimensions(this, hypothesis);
		double[] unitedLowerBounds = new double[lowerBounds.length];
		double[] unitedUpperBounds = new double[upperBounds.length];
		for (int i = 0; i < lowerBounds.length; i++) {
			unitedLowerBounds[i] = Math.min(lowerBounds[i], hypothesis.lowerBounds[i]);
			unitedUpperBounds[i] = Math.max(upperBounds[i], hypothesis.upperBounds[i]);
		}
		return new ContinuousHypothesis(unitedLowerBounds, unitedUpperBounds);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ContinuousHypothesis o) {
		int diff = compareArrays(lowerBounds, o.lowerBounds);
		if (diff != 0)
			return diff;
		return compareArrays(upperBounds, o.upperBounds);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ContinuousHypothesis))
			return false;
		return compareTo((ContinuousHypothesis) obj) == 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ContinuousHypothesis clone() {
		return new ContinuousHypothesis(lowerBounds, upperBounds);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < lowerBounds.length; i++) {
			if (i > 0)
				s.append(";");
			s.append("[" + lowerBounds[i] + "," + upperBounds[i] + "]");
		}
		return s.toString();
	}
	
	
	private static int compareArrays(double[] a1, double[] a2) {
		int sizeDiff = a1.length - a2.length;
		if (sizeDiff != 0)
			return sizeDiff;
		for (int i = 0; i < a1.length; i++) {
			double diff = a1[i] - a2[i];
			if (diff < 0d)
				return -1;
			else if (diff > 0d)
				return 1;
		}
		return 0;
	}
	
	private static void checkDimensions(ContinuousHypothesis h1, ContinuousHypothesis h2) {
		if (h1.getDimensions() != h2.getDimensions())
			throw new RuntimeException("dimensions must be equal");
	}
	
}
