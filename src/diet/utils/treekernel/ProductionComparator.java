/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.treekernel;

import java.util.Comparator;

public class ProductionComparator implements Comparator<Production> {

	public int compare(Production p1, Production p2) {

		// System.out.println(p1.getBnf()+" compared to"+p2.getBnf());
		if (p1.getBnf().compareTo(p2.getBnf()) < 0) {
			return -1;
		} else if (p1.getBnf().equals(p2.getBnf())) {
			return 0;
		} else {
			return 1;
		}
	}
}
