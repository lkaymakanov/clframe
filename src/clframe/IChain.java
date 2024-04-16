package clframe;

public interface IChain<T> {
	public IChain<T> getNext();
	public T getData();
	
}
