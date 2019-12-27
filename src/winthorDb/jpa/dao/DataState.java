/*
 * DataState.java
 *
 * Created on 14 de Fevereiro de 2006, 02:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package winthorDb.jpa.dao;

/**
 *
 * @author Eduardo
 */
public interface DataState {
    
    int BROWSE = 1;
    int INSERT = 2;
    int EDIT = 3;
    int NULL = 4;   

    int PRINT = 5;
}
