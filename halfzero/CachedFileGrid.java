/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero;

import java.util.Map;
import java.util.Set;
import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harrison
 */
public class CachedFileGrid <E extends java.io.Serializable> implements GridIO<E> {

    private class SubGrid implements Serializable {
        public int x0, y0;
    }
    public class CachedSubGrid extends SubGrid {
        public Grid<E> grid;
        
        public PagedSubGrid page() throws IOException {
            PagedSubGrid psg = new PagedSubGrid();
            psg.x0 = x0; psg.y0 = y0;
            
            psg.loc = File.createTempFile("HalfZero-", null);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(psg.loc));
            oos.writeObject(grid);
            oos.close();
            
            grid = null;
            
            return psg;
        }
    }
    public class PagedSubGrid extends SubGrid {
        public File loc;
        
        public CachedSubGrid cache() throws IOException {
            try {
                CachedSubGrid csg = new CachedSubGrid();
                csg.x0 = x0; csg.y0 = y0;
                
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(loc));
                csg.grid = Grid.class.cast(ois.readObject());
                ois.close();
                
                loc.delete();
                loc = null;
                
                return csg;
            } catch (ClassNotFoundException ex) {
                throw new IOException(ex);
            }
        }
    }
    
    private int wm, hm, px, py, xn, yn;
    
    Set<SubGrid> subgrids;
    Queue<CachedSubGrid> cache;
    
    public CachedFileGrid (Grid<E> backing, int wmax, int hmax, int x0, int y0) throws IOException {
        wm = wmax; hm = hmax;
        px = x0; py = y0;
        
        subgrids = new HashSet<SubGrid>();
        
        int i, j;
        for(i = 0; i < backing.width(); i += wmax)              
            for(j = 0; j < backing.height(); j += hmax) {    
                int x1 = i + wmax - 1,
                    y1 = j + hmax - 1;
                x1 = x1<backing.width()?x1:(backing.width()-1);
                y1 = y1<backing.height()?y1:(backing.height()-1);
                
                CachedSubGrid csg = new CachedSubGrid();
                csg.grid = backing.subGrid(i, x1, j, y1);
                
                SubGrid sg = csg.page();
                
                sg.x0 = i;
                sg.y0 = j;
                subgrids.add(sg);
            }
        
        cache = new LinkedList<SubGrid>();
    }

    @Override
    public Grid<E> get(int x0, int x1, int y0, int y1) {
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
