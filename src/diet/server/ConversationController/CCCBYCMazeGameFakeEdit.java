/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.parameters.IntParameter;

/**
 *
 * @author Greg
 */
public class CCCBYCMazeGameFakeEdit extends CCCBYCDefaultController{

      public IntParameter baselineDelay = new IntParameter("Baseline delay between characters (msecs)",150);
      public IntParameter variationInDelay = new IntParameter("Variation in delay (msescs)",1000);
      public IntParameter interventionEvery = new IntParameter("InterventionEvery",3);
      public IntParameter isTypingTimeout = new IntParameter("IsTypingTimeout",6500);

      
}
