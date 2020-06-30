package utils;

public class Pair<T, E> {
    private T first;
    private E second;

    public Pair(T t, E e) {
        this.first = t;
        this.second = e;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public E getSecond() {
        return second;
    }

    public void setSecond(E second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        Pair pair = (Pair) obj;
        return getFirst().equals(pair.getFirst()) &&
                getSecond().equals(pair.getSecond());
    }
}
