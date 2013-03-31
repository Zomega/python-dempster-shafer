/**
 * Created on Jun 21, 2007
 */
package util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Compares two <code>TreeSet</code>s based on their size and elements.
 * 
 * @author Thomas Reineking
 *
 */
public class SetComparator<S extends Comparable<? super S>> implements Comparator<TreeSet<S>> {

	public int compare(TreeSet<S> set1, TreeSet<S> set2) {
		// smaller sets first
		if (set1.size() != set2.size())
			return set1.size() - set2.size();
		
		Iterator<S> it1 = set1.iterator();
		Iterator<S> it2 = set2.iterator();
		while (it1.hasNext()) {
			int c = it1.next().compareTo(it2.next());
			if (c != 0)
				return c;
		}
		
		return 0;
	}
	
}
