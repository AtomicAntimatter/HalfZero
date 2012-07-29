/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero;

import halfzero.CachedList.State;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harrison
 */
public class CachedList<E> extends java.util.AbstractList<E> {
    public static enum State { CACHED, PAGED }
    
    private class SubFile {
        
        public final int x0;
        private List<E> list;
        private File loc;
        private State state;
        
        public SubFile (List<E> contents, int _x0) {
            x0 = _x0;
            list = contents;
            loc = null;
            state = State.CACHED;
        }
        
        public List<E> get() {
            try {
                if(state == State.CACHED)
                    return list;
                cache();
                List<E> g = list;
                page();
                return g;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        
        public boolean cached() {
            return state == State.CACHED;
        }
        
        public void page() throws IOException {
            if(state == State.PAGED) return;
            loc = File.createTempFile("HalfZero-", null);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(loc));
            oos.writeObject(list);
            oos.close();
            list = null;
            state = State.PAGED;
        }
        
        public void cache() throws IOException {
            if(state == State.CACHED) return;
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(loc));
                list = List.class.cast(ois.readObject());
                ois.close();
                
                loc.delete();
                loc = null;
                state = State.CACHED;
            } catch (ClassNotFoundException ex) {
                throw new IOException(ex);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SubFile other = (SubFile) obj;
            if (this.x0 != other.x0) {
                return false;
            }
            if (this.state != other.state) {
                return false;
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return x0;
        }
    }
    
    private List<SubFile> pages;
    private Queue<SubFile> cache;
    private final int cacheSize, cacheWidth;
    private int size = 0;
    
    public CachedList (int cacheWidth, int cacheSize) {
        this.cacheWidth = cacheWidth;
        this.cacheSize = cacheSize;
        cache = new LinkedList<SubFile>();
        pages = new ArrayList<SubFile>(1);
        SubFile init = new SubFile(new ArrayList(cacheSize), 0);
        pages.add(init);
        cache.add(init);
    }

    @Override
    public E get(int index) {
        SubFile page = pages.get(index / cacheSize);
        if(!page.cached()) {
            try {
                cache(page);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return page.get().get(index % cacheSize);
    }
    
    private void cache(SubFile page) throws java.io.IOException {
        if(page.cached()) return;
        page.cache();
        if(cache.size() > cacheWidth)
            cache.poll().page();
        cache.offer(page);
    }

    @Override
    public int size() {
        return size;
    }
    
    @Override
    public E set(int index, E element) {
        E old = get(index);
        SubFile page = pages.get(index / cacheSize);
        page.get().set(index % cacheSize, element);
        return old;
    }
    
    @Override
    public int indexOf(Object o) {
        Iterator<SubFile> i = cache.iterator();
        int j;
        while(i.hasNext())
            if((j = i.next().get().indexOf(o)) >= 0)
                return j;
        return super.indexOf(o);
    }
    
    @Override
    public boolean add(E element) {
        try {
            SubFile last = pages.get(pages.size()-1);
            cache(last);
            if(last.get().size() < cacheSize)
                last.get().add(element);
            else {
                last = new SubFile(new ArrayList<E>(cacheSize), cacheSize * pages.size());
                last.page();
                pages.add(last);
            }
            return true;
        } catch (IOException ex) {
            throw new RuntimeException(ex); 
        }
    }
}
