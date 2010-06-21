public interface Queryable {
    public Iterable<? extends Queryable> query();
}
