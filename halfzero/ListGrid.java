/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harrison
 */
public class ListGrid <E> implements Grid<E> {
    private int w, h;
    private Class<? extends List> ty;
    private Constructor<? extends List> c;
    
    private List<List<E>> col;
    
    public ListGrid (int _w, int _h, Class<? extends List> type) {
        w = _w;
        h = _h;
        ty = type;
        
        try {
            c = type.getConstructor(Integer.TYPE);
        } catch (NoSuchMethodException ex) {
            try { 
                c = type.getConstructor();
            } catch (NoSuchMethodException ey) {
                throw new RuntimeException(ey);
            }
        }
        
        col = consList(w);

        for(int i = 0; i < w; i++)
            col.add(consList(h));
    }
    
    private List consList(int p) {
        try {
            if (c.getParameterTypes().length == 0) {
                return c.newInstance();
            } else {
                return c.newInstance(p);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int width() {
        return w;
    }

    @Override
    public int height() {
        return h;
    }

    @Override
    public boolean isEmpty() {
        return w==0 && h==0;
    }

    @Override
    public boolean contains(E o) {
        Iterator<List<E>> i = col.iterator();
        while(i.hasNext())
            if(i.next().contains(o))
                return true;
        return false;
    }

    @Override
    public E[][] toArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public E get(int x, int y) {
        return col.get(x).get(y);
    }

    @Override
    public void set(int x, int y, E _val) {
        col.get(x).set(y, _val);
    }

    @Override
    public Grid<E> subGrid(int x0, int x1, int y0, int y1) {
        if (x1 <  x0 
         || y1 <  y0
         || x0 <  0
         || y0 <  0
         || x1 >= w
         || y1 >= h)
            throw new ArrayIndexOutOfBoundsException();
        
        int nw = x1 - x0 + 1,
            nh = y1 - y0 + 1;
        Grid<E> gr = new ListGrid<E>(nw, nh, ty);
        for (int i = 0; i < nw; i++)
            for (int j = 0; j < nw; j++)
                gr.set(i, j, this.get(x0 + i, y0 + j));
        return gr;
    }

    @Override
    public void insert(Grid<E> sg, int x0, int y0) {
        if (x0 < 0
         || y0 < 0
         || x0 + sg.width() >= w
         || y0 + sg.height() >= h)
            throw new ArrayIndexOutOfBoundsException();
        
        for(int i = 0; i < sg.width(); i++)
            for(int j = 0; j < sg.height(); j++)
                this.set(x0 + i, y0 + j, sg.get(i, j));
    }

    @Override
    public void grow(int dx, int dy) {
        grow(dx,dy,null);
    }

    @Override
    public void grow(int dx, int dy, E o) {
        ListIterator<List<E>> li = col.listIterator();
        while(li.hasNext()) {
            List<E> l = li.next();
            for (int i = 0; i < dy; i++)
                l.add(o);
        }
        
        List<List<E>> nc = consList(dx);
        for(int i = 0; i < dx; i++) {
            List<E> n = consList(h + dy);
            for(int j = 0; j < h + dy; j++)
                n.add(o);
            nc.add(n);
        }
        col.addAll(nc);
        
        w += dx;
        h += dy;
    }
}
