/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero.util;

/**
 *
 * @author harrison
 */
public interface Iterator2D <E> {
    E val();
    
    E next();
    E prev();
    E up  ();
    E down();
    
    boolean hasNext();
    boolean hasPrev();
    boolean hasUp  ();
    boolean hasDown();
}
