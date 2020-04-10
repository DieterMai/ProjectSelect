package dev.dietermai.projectselect.internal;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;

public class DialogOperator {
	
	private final ProjectSelectDialog dialog;
	
	public DialogOperator(Shell shell, List<IProject> projects) {
		dialog = new ProjectSelectDialog(shell, projects);
	}
	
	public DialogResult showDialog() {
		int resultCode = dialog.open();
		
		DialogResult dialogResult = new DialogResult();
		dialogResult.setResultCode(resultCode);
		dialogResult.setSelection(dialog.getResult());
		
		return dialogResult;
	}
}
