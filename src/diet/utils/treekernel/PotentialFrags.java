/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.treekernel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import diet.task.tangram2D1M.SlotUtterance;


/**
 *
 * @author Arash
 */
public class PotentialFrags extends Thread{
    
    Map<Integer, ArrayList<Fragment>> slotFragsMap;
    int slotFragsToHold;
    SlotUtterance possiblyAmbiguousSource=null;
    public PotentialFrags(int slotFragsToHold)
    {
        slotFragsMap=Collections.synchronizedMap(new HashMap<Integer, ArrayList<Fragment>>());
        this.slotFragsToHold=slotFragsToHold;
        this.possiblyAmbiguousSource=null;
        this.start();
    }
    
    public synchronized void addPotentialFrags(int slotUD, ArrayList<Fragment> frags)
    {
        Integer key=new Integer(slotUD);
         
             if (slotFragsMap.containsKey(key))
             {
                 slotFragsMap.get(key).addAll(frags);
            
            
             }
             else slotFragsMap.put(key, frags);
             
            
         
        
    }
    public synchronized void removeOldFrags()
    {
        
    }
    
    public synchronized List<Fragment> getFragsForSlot(int slotID)
    {
        Integer id=new Integer(slotID);
        return slotFragsMap.get(id);
    }
    
    
    public synchronized void removeAmbiguousFragments(SlotUtterance possiblyAmbiguousSource)
    {
        this.possiblyAmbiguousSource=possiblyAmbiguousSource;
        notify();
        
        
    }
    
    public static boolean ambiguous(String utt, Fragment frag)
    {
        /* Criteria: 
         *
         * 
         * 
         */
        int fragLength=FragmentIdentifier.phraseLength(frag.content);
        String phraseType=frag.phraseType;
        String[] sentences=utt.split("[?.!]");
        String[] words=frag.content.split(" ");
        for(String sentence:sentences)
        {
            
            Pattern p=Pattern.compile(words[words.length-1]);
            Matcher m=p.matcher(sentence);
            //the criteria below need to be fine tuned
            //at the moment the criteria for ambiguity are the same for all phrases
            //namely if the head (last) word of the fragment occurs in the utterance.
            if (m.find()) return true;
            
            /*
            if (phraseType.equalsIgnoreCase("NP"))
            {
                return m.find();
            
            
            }
            else if (phraseType.equalsIgnoreCase("NN"))
            {
                return m.find();
            
            }
            else if (phraseType.equalsIgnoreCase("JJ"))
            {
                return m.find();
            }
            else if (phraseType.equalsIgnoreCase("PP"))
            {
                return m.find();
            
            }*/
        }
        return false;
        
    }
    
    public void run()
    {
        while(true)
        {
            
            if (this.possiblyAmbiguousSource==null)
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
            else
            {
                int slotUD=possiblyAmbiguousSource.slotUD;
                String utt=possiblyAmbiguousSource.utterance;
                if (slotUD==0) 
                {
                    this.possiblyAmbiguousSource=null;
                    continue;
                }
                 synchronized(slotFragsMap){
                    List<Fragment> fragmentsFromPreviousSlot=getFragsForSlot(slotUD-1);
                    if (fragmentsFromPreviousSlot==null)
                    {
                        this.possiblyAmbiguousSource=null;
                        continue;
                    }
                    ListIterator<Fragment> i=fragmentsFromPreviousSlot.listIterator();
                    while(i.hasNext())
                    {
                        Fragment f=i.next();
                        //System.out.print("Removing ambiguous Frags.");
                       
                        if (ambiguous(utt, f)) 
                        {
                            System.out.println("utt:" +utt);
                            System.out.println("ambiguous frag:"+f.content);
                            i.remove();
                        }
                    }
                }
                this.possiblyAmbiguousSource=null;
                
            }
            
        }
    }

}
