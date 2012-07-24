/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author harrison
 */
public class GridList<E> implements Collection<E> {

    public class Entry {
        E item;
        Entry u, d, l, r;
        
        public final int x, y;

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + (this.item != null ? this.item.hashCode() : 0);
            hash = 37 * hash + this.x;
            hash = 37 * hash + this.y;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj.getClass().equals(getClass()))
                return obj.hashCode() == hashCode();
            else return false;
        }
        
        /*package*/ Entry(int _x, int _y, E _item) {
            item = _item;
            u = d = l = r = null;
            x = _x; y = _y;
        }
        
        public E getValue() {
            return item;
        }
        
        public void setValue(E _val) {
            item = _val;
        }
        
        public Iterator2D<E> iterator2D() {
            return new Iterator2DImpl(this);
        }
        
        private class Iterator2DImpl implements Iterator2D<E> {
            Entry curr;

            public Iterator2DImpl (Entry e) {
                curr = e;
            }

            @Override
            public E get() {
                return curr.item;
            }

            @Override
            public E next() {
                if(curr.r != null) {    
                    curr = curr.r;
                    return curr.item;
                }
                else throw new ArrayIndexOutOfBoundsException();
            }

            @Override
            public E prev() {
                if(curr.l != null) {    
                    curr = curr.l;
                    return curr.item;
                }
                else throw new ArrayIndexOutOfBoundsException();
            }

            @Override
            public E up() {
                if(curr.u != null) {    
                    curr = curr.u;
                    return curr.item;
                }
                else throw new ArrayIndexOutOfBoundsException();
            }

            @Override
            public E down() {
                if(curr.d != null) {    
                    curr = curr.d;
                    return curr.item;
                }
                else throw new ArrayIndexOutOfBoundsException();
            }

            @Override
            public boolean hasNext() {
                return curr.r != null;
            }

            @Override
            public boolean hasPrev() {
                return curr.l != null;
            }

            @Override
            public boolean hasUp() {
                return curr.u != null;
            }

            @Override
            public boolean hasDown() {
                return curr.d != null;
            }

            @Override
            public void set(final E _val) {
                curr.item = _val;
            }
        }    
    }
    
    int w, h;
    Entry head;
    List<Entry> r, c;
    
    public GridList() { //O(1)
        this(1,1);
    }
    
    public GridList(final int _w, final int _h) { //O(wh^2+w^2h)
        this.w = this.h = 1;
        r = new LinkedList<Entry>();
        c = new LinkedList<Entry>();
        
        head = new Entry(0,0,null);
        r.add(head);
        c.add(head);
        
        for(int i = 1; i < _w; i++)
            addCol();
        for(int j = 1; j < _h; j++)
            addRow();
    }
    
    //Utility methods
    
    private void addCol() {  //O(h^2+wh)
        Entry entry = new Entry(w,0,null), 
              oldEntry = null,
              lastEntry = get(w-1,0);
        entry.l = lastEntry;
        lastEntry.r = entry;
        c.add(entry);
        
        for(int j = 1; j < h; j++) {
            oldEntry = entry;
            entry = new Entry(w,j,null);
            entry.u = oldEntry;
            oldEntry.d = entry;
            lastEntry = get(w-1, j);
            entry.l = lastEntry;
            lastEntry.r = entry;
        }
        
        w++;
    }
    
    private void addRow() { //O(w^2+wh)
        Entry entry = new Entry(0,h,null), 
              oldEntry = null,
              lastEntry = get(0, h-1);
        entry.u = lastEntry;
        lastEntry.d = entry;
        r.add(entry);
        
        for(int i = 1; i < w; i++) {
            oldEntry = entry;
            entry = new Entry(i,h,null);
            entry.l = oldEntry;
            oldEntry.r = entry;
            lastEntry = get(i, h-1);
            entry.u = lastEntry;
            lastEntry.d = entry;
        }
        
        h++;
    }
    
    //Read methods
    
    public Entry get(final int x, final int y) {  //O(w+h)
        if (x >= w) 
            throw new ArrayIndexOutOfBoundsException(x);
        if (y >= h)
            throw new ArrayIndexOutOfBoundsException(y);
        
        Entry curr = head;
        
        for (int i = 0; i < x; i++)
            curr = curr.r;
        for (int j = 0; j < y; j++)
            curr = curr.d;
        
        return curr;
    }
    
    public enum IterationOrder {
        ROW_MAJOR,
        COL_MAJOR
    }
    
    private class EIterator implements Iterator<Entry> {
        
        private Iterator<Entry> i;
        private final IterationOrder or;
        private Entry e = head;
        
        public EIterator () {
            this(IterationOrder.COL_MAJOR);
        }
        
        public EIterator (IterationOrder order) {
            or = order;
            if(or == IterationOrder.COL_MAJOR) {
                i = c.iterator();
            }
            else
                i = r.iterator();
        }

        @Override
        public boolean hasNext() {
            return (or == IterationOrder.COL_MAJOR
                    ?   e.d
                    :   e.r) != null
                 || i.hasNext();
        }

        @Override
        public Entry next() {
            Entry n = (or == IterationOrder.COL_MAJOR
                       ?    e.d
                       :    e.r);
            if(n != null)
                e = n;
            else if(i.hasNext())
                e = i.next();
            else throw new NoSuchElementException();
            return e;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot delete via Iterator.");
        }
        
    }
    
    public Iterator<GridList<E>.Entry> entryIterator() {
        return new EIterator();
    }
    public Iterator<GridList<E>.Entry> entryIterator(IterationOrder order) {
        return new EIterator(order);
    }
    
    private class VIterator implements Iterator<E> {
        
        EIterator i;
        
        public VIterator() {
            this(IterationOrder.COL_MAJOR);
        }
        
        public VIterator(IterationOrder order) {
            i = new EIterator(order);
        } 

        @Override
        public boolean hasNext() {
            return i.hasNext();
        }

        @Override
        public E next() {
            return i.next().item;
        }

        @Override
        public void remove() {
            i.remove();
        }
        
    }
    
    public Iterator<E> iterator() {
        return new VIterator();
    }
    public Iterator<E> iterator(IterationOrder order) {
        return new VIterator(order);
    }
    
    //Write methods
    
    public void set(final int x, final int y, final E _val) {
        get(x,y).item = _val;
    }
    
    //Collection<E> methods
    
    @Override
    public int size() {
        return w*h;
    }

    @Override
    public boolean isEmpty() {
        return w==0 || h==0;
    }

    @Override
    public boolean contains(Object o) {
        Iterator i = this.iterator();
        while(i.hasNext())
            if(i.next().equals(o))
                return true;
        return false;
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("Not supported, ever.");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addAll(Collection<? extends E> clctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}
