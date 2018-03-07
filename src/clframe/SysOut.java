package clframe;

public class SysOut implements ILog{

	@Override
	public void log(String message) {
		System.out.println(message);
	}

}
