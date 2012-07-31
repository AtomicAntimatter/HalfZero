/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero;

import java.io.Serializable;

/**
 *
 * @author harrison
 */
public class Coordinate implements Serializable, Cloneable {
    public final int x, y, h;
    
    public static long XB = 233l, YB = 241l, XM = 433494437l, YM = 2971215073l;
    
    public Coordinate (int _x, int _y) {
        x = _x; y = _y;
        h = (int) ((((XB^x)%XM) + ((YB^y)%YM)) % Integer.MAX_VALUE);
    }
    
    @Override
    public int hashCode() {  
        return h;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordinate other = (Coordinate) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
}
