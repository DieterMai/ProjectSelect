package dev.dietermai.projectselect.internal;

import org.eclipse.jface.window.Window;

public class DialogResult {
	private Object[] selection;
	private int resultCode;
	
	void setSelection(final Object[] selection) {
		this.selection = selection;
	}
	
	void setResultCode(final int resultCode) {
		this.resultCode = resultCode;
	}
	
	public Object[] getSelection() {
		return selection;
	}
	
	public int getResultCode() {
		return resultCode;
	}
	
	public boolean selectableResult() {
		return resultCode == Window.OK && selection != null && selection.length > 0;
	}
}
