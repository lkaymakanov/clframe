package clframe;

import java.io.InputStream;


class PaddingInpustStreamConverter implements IStreamConverter {
	private InputStream orgstream, padding;
	private boolean left;
	
	public PaddingInpustStreamConverter(InputStream orgstream, InputStream padding, boolean left) {
		if(padding == null) return;
		if(orgstream == null) return;
		this.padding = padding;
		this.orgstream = orgstream;
		this.left= left;
	}

	@Override
	public InputStream convert() {
		try {
			if(left) return StreamUtils.toInputStream(StreamUtils.merge(padding, orgstream));
			else  return StreamUtils.toInputStream(StreamUtils.merge(orgstream, padding));
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

}
