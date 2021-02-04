package tudbut.tools;

public class Stack<T> extends Queue<T> {
    
    public Stack() { }
    
    protected Stack(Stack<T> stack) {
        super(stack);
    }
    
    @Override
    public synchronized T next() {
        return popTop();
    }
    
    @Override
    public synchronized T peek() {
        return getTop();
    }
    
    @Override
    public Stack<T> clone() {
        return new Stack<>(this);
    }
}
