/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server;

import java.io.File;

/**
 *
 * @author Greg
 */
public class ServerStartup {

    static public void initializeDirectories(){
        String r = System.getProperty("user.dir");
        File f = new File(r+File.separator+"data"+File.separator+"Saved experimental data");
        if(!f.exists()){
            f.mkdir();
        }
    }
}
