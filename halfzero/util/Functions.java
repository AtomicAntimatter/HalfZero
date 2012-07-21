/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero.util;

/**
 *
 * @author harrison
 */
public abstract class Functions {
    public static int randInt(int max) {
        return (int)Math.floor(max*Math.random());
    }
}
