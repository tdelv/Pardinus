package kodkod.engine;

public interface PrimitiveFactory<S extends PrimitiveSolver> {

	public S instance();

}
