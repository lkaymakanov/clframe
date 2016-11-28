package clframe;

import java.io.File;

interface TraverseDirsCallBack {
		
	public void OnForward(File node);
	public void OnReturnFromRecursion(File node);
}
