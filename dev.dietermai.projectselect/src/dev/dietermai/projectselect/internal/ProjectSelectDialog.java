package dev.dietermai.projectselect.internal;

import static dev.dietermai.projectselect.internal.ProjectListLabelProvider.projectListLabelProvider;

import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import dev.dietermai.projectselect.ProjectSelectActivator;

/**
 * The dialog for the project selection. This implementation uses the
 * {@link FilteredItemsSelectionDialog} provided by eclipse.ui. This is the same
 * base class that is used for Resource Select and Type Select.
 */
public class ProjectSelectDialog extends FilteredItemsSelectionDialog {

	/** ID that is used for persisting the dialog settings. */
	private static final String DIALOG_SETTINGS = "dev.dietermai.projectselect.dialog.settings";

	/** Convenience constant for ids that start with the plug-in id prefix. */
	private static final String PLUGIN_PREFIX = ProjectSelectActivator.PLUGIN_ID + ".";
	/** Id for the help contribution. */
	private static final String DIALOG_HELP = PLUGIN_PREFIX + "ProjectSelectDialogHelp";

	/** Projects of the workspace. */
	private List<IProject> projects;

	ProjectSelectDialog(Shell shell, List<IProject> projects) {
		super(shell, true);
		super.setTitle("Select Project");
		super.setMessage("Enter project name prefix or pattern (*, ?, camel case or initials):");
		super.setListLabelProvider(projectListLabelProvider);
		super.setDetailsLabelProvider(projectListLabelProvider);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(shell, DIALOG_HELP);

		this.setData(projects);
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		return null; // no need for this
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = getDialogSettings_internal().getSection(DIALOG_SETTINGS);
		if (settings == null) {
			settings = getDialogSettings_internal().addNewSection(DIALOG_SETTINGS);
		}
		return settings;
	}

	private IDialogSettings getDialogSettings_internal() {
		return ProjectSelectActivator.getDefault().getDialogSettings();
	}

	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	@Override
	protected ItemsFilter createFilter() {
		return new ProjectSelectItemsFilter();
	}

	@Override
	protected Comparator<?> getItemsComparator() {
		return (arg0, arg1) -> arg0.toString().compareTo(arg1.toString());
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter, IProgressMonitor progressMonitor) throws CoreException {
		projects.forEach(project -> contentProvider.add(project, itemsFilter));
	}

	@Override
	public String getElementName(Object item) {
		if (item instanceof IProject) {
			return ((IProject) item).getName();
		}
		return null;
	}

	public void setData(List<IProject> projects) {
		this.projects = projects;
	}

	private class ProjectSelectItemsFilter extends ItemsFilter {

		private InitialsSearchPattern initalSearchPattern = new InitialsSearchPattern();

		@Override
		public boolean matchItem(Object item) {
			if (item == null) {
				return false;
			} else if (item instanceof IProject) {
				boolean match = matches(((IProject) item).getName());
				match |= initalSearchPattern.checkForInitalMatch(patternMatcher.getPattern(), ((IProject) item).getName());
				return match;
			} else {
				return false;
			}
		}

		@Override
		public boolean isConsistentItem(Object item) {
			return true;
		}
	}
}
