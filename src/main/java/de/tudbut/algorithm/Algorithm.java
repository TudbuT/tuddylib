package de.tudbut.algorithm;

public abstract class Algorithm<T> {
    
    public abstract T solve(Input<T>[] inputs);
    
    
    public static class Input<T> {
        protected T t;
        protected String[] data;
        
        public Input(T t, String[] data) {
            this.t = t;
            this.data = data;
        }
    }
}
