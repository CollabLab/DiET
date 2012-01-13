/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.message;

/**
 *
 * @author Greg
 */
public class MessageDisplayChangeWebpage extends Message{

    String id;
    String url;

    String prepend="";
    String append="";
    
    public MessageDisplayChangeWebpage(String id, String url) {
          super("server","server");
          this.id=id;
          this.url=url;
    }

    public MessageDisplayChangeWebpage(String id, String url, String prepend, String append) {
          super("server","server");
          this.id=id;
          this.url=url;
          this.prepend = prepend;
          this.append = append;
    }


    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getAppend(){
        return this.append;
    }

    public String getPrepend(){
        return this.prepend;
    }
}
