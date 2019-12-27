
/*
 * HbUtil.java
 *
 * Created on 9 de Fevereiro de 2006, 20:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package winthorDb.jpa.dao;


/**
 *
 * @author Eduardo
 */
public class HbUtil {
    
    public static boolean usable = false;
    

    
    public static void createSessionFactory() throws Exception {
  
    }
    
    public static final ThreadLocal session = new ThreadLocal();
    
    public static Object currentSession() {
        return null;
    }
    
    public static Object openSession() {
        return null;
    }
    
    public static void closeSession() {
 
    }
    
    public static void createHbSession()  {
        
    }
    
}
