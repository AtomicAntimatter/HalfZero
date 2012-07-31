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
    public static boolean pnpoly(int npol, float[] xp, float[] yp, float x, float y)
    {
      int i, j;
      boolean c = false;
      for (i = 0, j = npol-1; i < npol; j = i++) {
        if ((((yp[i] <= y) && (y < yp[j])) ||
             ((yp[j] <= y) && (y < yp[i]))) &&
            (x < (xp[j] - xp[i]) * (y - yp[i]) / (yp[j] - yp[i]) + xp[i]))
          c = !c;
      }
      return c;
    }
    public static <T> void nil(T t) {
        //do magic here.
    }
}
