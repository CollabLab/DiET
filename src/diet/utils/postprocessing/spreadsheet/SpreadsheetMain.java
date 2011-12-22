/*
 * SpreadsheetMain.java
 *
 * Created on 19 January 2008, 17:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.utils.postprocessing.spreadsheet;
import java.io.File;

import diet.utils.stringsimilarity.StringSimilarityMeasure;
/**
 *
 * @author user
 */
public class SpreadsheetMain {
    
    String [][] spreadsheet;
    
    /** Creates a new instance of SpreadsheetMain */
    public SpreadsheetMain() {
        float[] a = StringSimilarityMeasure.getProportionOfWordsOfAInBandBinA("HELLO hello ......HELLO i", "maybe not so much is what am saying HELLO..HELLO...HELLO");
        System.out.println("AAAAAA "+a[0]);
        System.out.println("BBBBBB "+a[1]);
        //System.exit(-1);
    }
    
    
    
    public void compareResponsesToInterventions(){
        Spreadsheet s = new Spreadsheet(new File("testspreadsheet.csv"));
        s.addNewColumnOfContiguousTurns();
        s.addInterventionTXTColumn();
        s.addTSTColumn();
        s.addInterventionLexicalMatchingScores();
        File output = new File("testspreadsheetout.csv");
        s.writeSpreadsheetToFile(output);    
        
    }
    
    
    
    public void compareContiguousTurns(){
        Spreadsheet s = new Spreadsheet(new File("testspreadsheet.csv"));
        s.addNewColumnOfContiguousTurns();
        s.testThatEachRowIsOfADifferentParticipant();
        s.compareWholeSpreadsheet();
        File output = new File("testspreadsheetout.csv");
        s.writeSpreadsheetToFile(output);    
    }
    
    public static void main(String[] args) {
        SpreadsheetMain ssm = new SpreadsheetMain();
        //ssm.compareContiguousTurns();
        //ssm.compareResponsesToInterventions();
        Spreadsheet s = new Spreadsheet(new File("input.csv"));
    }
    
}
