package dev.dietermai.projectselect;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

import dev.dietermai.projectselect.internal.DialogOperator;
import dev.dietermai.projectselect.internal.DialogResult;
import dev.dietermai.projectselect.internal.PackageExplorerProxy;
import dev.dietermai.projectselect.internal.ProjectViewIds;
import dev.dietermai.projectselect.internal.RequiredReferences;

/**
 * Handler for opening the project select Dialog.
 */
public class OpenDialogHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		RequiredReferences references = RequiredReferences.fetchReferences();
		if (!references.allThere()) {
			return null;
		}

		DialogOperator dialogOperator = new DialogOperator(references.getShell(), references.getProjects());
		DialogResult dialogResult = dialogOperator.showDialog();
		if (!dialogResult.selectableResult()) {
			return null;
		}

		for (IWorkbenchPart part : references.getViews()) {
			selectProjectsInView(part, dialogResult.getSelection());
		}

		return null;
	}

	private void selectProjectsInView(IWorkbenchPart part, Object[] projects) {
		final String ID = getSiteId(part);
		if (ProjectViewIds.isProject(ID)) {
			selectProjectsInProjectExplorer((ProjectExplorer) part, projects);
		} else if (ProjectViewIds.isPackage(ID)) {
			new PackageExplorerProxy(part).select(projects);
		}
	}

	private String getSiteId(IWorkbenchPart part) {
		if (part != null) {
			IWorkbenchPartSite site = part.getSite();
			if (site != null) {
				return site.getId();
			}
		}
		return "";
	}

	private void selectProjectsInProjectExplorer(ProjectExplorer explorer, Object[] projects) {
		explorer.selectReveal(new StructuredSelection(projects));
	}
}
