/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero;

import java.util.Iterator;

/**
 *
 * @author harrison
 */
public interface Grid <E extends java.io.Serializable> extends java.io.Serializable {
    int width();
    int height();
    
    boolean isEmpty();
    
    boolean contains(E o);
    
    E[][] toArray();
    
    E get(int x, int y);
    void set(int x, int y, E _val);
    
    Grid<E> subGrid(int x0, int x1, int y0, int y1);
    void insert(Grid<E> sg, int x0, int y0);
    void grow(int dx, int dy);
    void grow(int dx, int dy, E o);
    
    public static class DimensionMismatchException extends Exception {}
    void adjoinH(Grid<E> n) throws DimensionMismatchException;
    void adjoinV(Grid<E> n) throws DimensionMismatchException;
    
    Iterator<E> iterator();
}
