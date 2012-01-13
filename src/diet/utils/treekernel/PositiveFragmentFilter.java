/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.treekernel;

/**
 *
 * @author Arash
 */
public class PositiveFragmentFilter {
    
    public String phraseTypeFilter;
    public int lengthFilter;
    // could add others
    public PositiveFragmentFilter(String a, int b)
    {
        this.phraseTypeFilter=a;
        this.lengthFilter=b;
        
    }

}
