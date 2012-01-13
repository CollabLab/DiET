/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.treekernel;

/**
 *
 * @author Arash
 */

public class Fragment {
    
    public int length;
    public String phraseType;
    public String content;
    
    public Fragment(String phraseType, String content)
    {
        this.phraseType=phraseType.trim();
        
        this.content=content.trim();
        this.length=content.split(" ").length;
    }
    public boolean equals(Object frag)
    {
        
        Fragment fragment;
        if (frag instanceof Fragment)
        {
            fragment=(Fragment)frag;
        }
        else return false;
        return fragment.content.equalsIgnoreCase(this.content)&&fragment.phraseType.equalsIgnoreCase(this.phraseType);
    }
    public String toString()
    {
        return phraseType+"-->"+content;
    }
    

}
