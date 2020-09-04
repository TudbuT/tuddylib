package tudbut.tools;

public class Stack<T> extends Queue<T> {
    
    @Override
    public synchronized T next() {
        return popTop();
    }
    
    @Override
    public synchronized T peek() {
        return getTop();
    }
}
