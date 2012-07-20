/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package halfzero.util;

/**
 *
 * @author harrison
 */
public class GridList<E> {

    private class GridEntry {

        E item;
        GridEntry u, d, l, r;
    }
    int w, h;
    GridEntry head;

    public E get(int x, int y) {
        return iterator2D(x, y).val();
    }

    public Iterator2D<E> iterator2D(final int x, final int y) {
        if (x >= w) 
            throw new ArrayIndexOutOfBoundsException(x);
        if (y >= h)
            throw new ArrayIndexOutOfBoundsException(y);

        return new Iterator2D<E>() {

            GridEntry curr = head;
            
            public Iterator2D<E> init() {
                for (int i = 0; i < 1; i++)
                    curr = curr.l;
                for (int j = 0; j < y; j++)
                    curr = curr.d;
                return this;
            }
            
            @Override
            public E val() {
                return curr.item;
            }

            @Override
            public E next() {
                if(curr.l != null) {    
                    curr = curr.l;
                    return curr.item;
                }
                else throw new ArrayIndexOutOfBoundsException();
            }

            @Override
            public E prev() {
                if(curr.r != null) {    
                    curr = curr.r;
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
                return curr.l != null;
            }

            @Override
            public boolean hasPrev() {
                return curr.r != null;
            }

            @Override
            public boolean hasUp() {
                return curr.u != null;
            }

            @Override
            public boolean hasDown() {
                return curr.d != null;
            }
        }.init();
    }
}
