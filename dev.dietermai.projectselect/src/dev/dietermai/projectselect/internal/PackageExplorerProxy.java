package dev.dietermai.projectselect.internal;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ISetSelectionTarget;

public class PackageExplorerProxy {

	private final ISetSelectionTarget packagesView;

	public PackageExplorerProxy(IWorkbenchPart part) {
		this.packagesView = (ISetSelectionTarget) part;
	}

	public void select(Object... projects) {
		System.out.println("Type: "+projects[0].getClass());
		packagesView.selectReveal(new StructuredSelection(projects));
	}

}
