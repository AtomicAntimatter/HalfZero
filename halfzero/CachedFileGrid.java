/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero;

import halfzero.Grid.DimensionMismatchException;
import java.util.Map;
import java.util.Set;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harrison
 */
public class CachedFileGrid <E extends java.io.Serializable> implements CachedGrid<E> {

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private static class SubGrid<E extends java.io.Serializable> {
        public static enum State {
            CACHED,
            PAGED
        }
        
        public final int x0, y0;
        private Grid<E> grid;
        private File loc;
        private State state;
        
        public SubGrid (Grid<E> contents, int _x0, int _y0) {
            x0 = _x0; y0 = _y0;
            grid = contents;
            loc = null;
            state = State.CACHED;
        }
        
        public Grid<E> get() {
            try {
                if(state == State.CACHED)
                    return grid;
                
                cache();
                Grid<E> g = grid;
                page();
                
                return g;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        
        public State state() {
            return state;
        }
        
        public void page() throws IOException {
            if(state == State.PAGED) return;
            
            loc = File.createTempFile("HalfZero-", null);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(loc));
            oos.writeObject(grid);
            oos.close();
            
            grid = null;
            state = State.PAGED;
        }
        
        public void cache() throws IOException {
            if(state == State.CACHED) return;
            
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(loc));
                grid = Grid.class.cast(ois.readObject());
                ois.close();
                
                loc.delete();
                loc = null;
                state = State.CACHED;
            } catch (ClassNotFoundException ex) {
                throw new IOException(ex);
            }
        }
    }
    
    private int w, h, wm, hm, px, py, xn, yn;
    
    Set<SubGrid<E>> subgrids;
    Queue<SubGrid<E>> cache;
    
    public CachedFileGrid (Grid<E> backing, int wmax, int hmax, int x0, int y0) throws IOException {
        wm = wmax; hm = hmax;
        px = x0; py = y0;
        w = backing.width();
        h = backing.height();
        
        subgrids = new HashSet<SubGrid<E>>();
        
        int i, j;
        for(i = 0; i < w; i += wmax)              
            for(j = 0; j < h; j += hmax) {    
                int x1 = i + wmax - 1,
                    y1 = j + hmax - 1;
                x1 = x1<w?x1:(w-1);
                y1 = y1<h?y1:(h-1);
                
                SubGrid csg = new SubGrid(backing.subGrid(i, x1, j, y1), i/wmax, j/hmax);
                
                csg.page();
                subgrids.add(csg);
            }
        
        cache = new LinkedList<SubGrid<E>>();
    }
    
    private SubGrid<E> locate(int x, int y) {
        int xx = (x/wm)*wm, yy = (y/hm)*hm;
        
        SubGrid<E> res;
        Iterator<SubGrid<E>> i = subgrids.iterator();
        while(i.hasNext()) {
            res = i.next();
            if(res.x0 == xx && res.y0 == yy)
                return res;
        }
        return null;
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
        return w == 0 && h == 0;
    }

    @Override
    public boolean contains(E o) {
        Iterator<SubGrid<E>> i = subgrids.iterator();
        while(i.hasNext())
            if(i.next().get().contains(o))
                return true;
        return false;
    }

    @Override
    public E[][] toArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public E get(int x, int y) {
        try {
            SubGrid<E> sg = locate(x,y);
            
            if(!cache.contains(sg)) {
                if(cache.element() != null)
                    cache.poll().page();
                sg.cache();
                cache.add(sg);
            }
            
            return sg.get().get(x%wm, y%hm);
            
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
    }

    @Override
    public void set(int x, int y, E _val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Grid<E> subGrid(int x0, int x1, int y0, int y1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(Grid<E> sg, int x0, int y0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void grow(int dx, int dy) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void grow(int dx, int dy, E o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void adjoinH(Grid<E> n) throws DimensionMismatchException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void adjoinV(Grid<E> n) throws DimensionMismatchException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void cleanCache() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCacheSize(int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void preloadCache(int movement, int x0, int x1, int y0, int y1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void load(Map columns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
