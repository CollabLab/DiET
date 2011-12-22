/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.treekernel;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import diet.server.ParserWrapper;
import diet.task.tangram2D1M.SlotUtterance;
import diet.task.tangram2D1M.SlotUtteranceQueue;
import edu.stanford.nlp.trees.Tree;
/**
 *
 * @author Arash
 */
public class FragmentIdentifierThread extends Thread{
    
    public static final int slotFragsToHold=3;
    FragmentIdentifier fi;
    ParserWrapper pw;
    PotentialFrags frags;
    SlotUtteranceQueue utterancesToBeAnalysed;
    boolean resetting=false;
    
    
    public FragmentIdentifierThread(ParserWrapper pw)
    {
        this.pw=new ParserWrapper(System.getProperty("user.dir")+File.separator+"utils"+File.separator+"englishPCFG.ser.gz");
        this.setPriority(NORM_PRIORITY);
        this.fi=new FragmentIdentifier(System.getProperty("user.dir")+File.separator+"fragmentFilters"+File.separator+"filtersTangramDistantCR.txt");
        utterancesToBeAnalysed=new SlotUtteranceQueue();
        frags=new PotentialFrags(slotFragsToHold);
        resetting=false;
        
    }
    public synchronized void reset()
    {
        this.resetting=true;
        notify();
    }
   
    public synchronized void enqueueSlotUtterance(SlotUtterance u)
    
    {
        System.out.println("enqueue called");
        if (!resetting)
            
        {
            System.out.println("Adding utterance :"+u.utterance+ " for an analysis.");
            utterancesToBeAnalysed.offer(u);
        
            frags.removeAmbiguousFragments(u);
            this.notify();
        }
    }
    public Fragment chooseRandFragFromSlot(int slotUD)
    {
        Random r=new Random();
        List<Fragment> fragments=this.getFragsForSlot(slotUD);
        if (fragments.size()==0) return null;
        else 
        {
            
            return fragments.get(r.nextInt(fragments.size()));
        }
    }
    
    public Fragment chooseBestRandFragFromSlot(int slotUD, int bestLength)
    {
        Random r=new Random();
        List<Fragment> fragments=this.getFragsForSlot(slotUD);
        if (fragments.size()==0) return null;
        List<Fragment> curFrags=new ArrayList<Fragment>();
        for(int i=bestLength;i>1;i--)
        {
            for(Fragment frag:fragments)
            {
                if (frag.length==i) curFrags.add(frag);
            }
            if (!curFrags.isEmpty()) 
                return curFrags.get(r.nextInt(curFrags.size()));
            else
                curFrags=new ArrayList<Fragment>();
                
            
        }
        return null;
    }
    
    public synchronized List<Fragment> getFragsForSlot(int id)
    {
        
        List<Fragment> result=frags.getFragsForSlot(id);
        if (result!=null) return result;
        else return new ArrayList<Fragment>();
        
    }
    
    
    
    private synchronized Tree parse(SlotUtterance s)
    {
        Vector v=pw.parseTextTimeOut(s.utterance, s.parseTimeOut);
        if (v.elementAt(1)==null) return null;
        else return (Tree)v.elementAt(1);
        
    }
    
    public void run()
    {
        
        while(true)
        {
            if (resetting)
            {
                utterancesToBeAnalysed=null;
                utterancesToBeAnalysed=new SlotUtteranceQueue();
                frags=null;
                frags=new PotentialFrags(slotFragsToHold);
                System.out.println("fragmentIdentifier thread reset successful");
                resetting=false;
                continue;
            }
            if (!utterancesToBeAnalysed.isEmpty())
            {
                SlotUtterance cur=utterancesToBeAnalysed.peek();
                int slotUD=cur.slotUD;
                
                Tree curTree=parse(cur);
                if (curTree!=null)
                {
                    System.out.println("parsing: "+cur);
                    curTree.pennPrint();
                    ArrayList<Fragment> fragments=fi.getFilteredFragments(curTree);
                    frags.addPotentialFrags(slotUD, fragments);
                    utterancesToBeAnalysed.remove(cur);
                    System.out.println("Adding frags:"+slotUD+"-->"+fragments);
                }
                else 
                {
                    
                    cur.increaseTimeOut(500);
                }
                
                    
                
            }
            else
            {
                synchronized(this){
                    try{
                        wait();
                    
                    
                    }
                    catch(InterruptedException e)
                    {
                       e.printStackTrace();
                    
                    }
                }
        
            }
        
         }
    
  

    }
}
