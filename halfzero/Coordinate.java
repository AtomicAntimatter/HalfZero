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
    public final int x, y;
    /*
    private static final int GRID_SIZE = 10;
    
    private static final int GRID_MOD;
    static {
        int gmmin = GRID_SIZE*GRID_SIZE;
        int gm = 1;
        while (gm < gmmin)
            gm <<= 1;
        GRID_MOD = gm;
    }
    */
    public Coordinate (int _x, int _y) {
        x = _x; y = _y;
    }
    /*
    @Override
    public int hashCode() {  //designed to group small areas together in hashcode.
        int xd = x/GRID_SIZE, yd = y/GRID_SIZE,
            xm = x%GRID_SIZE, ym = y/GRID_SIZE;
        return GRID_MOD * (xd + yd) + (xm + ym);
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
     */
}
