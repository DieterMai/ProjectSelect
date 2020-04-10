package dev.dietermai.projectselect.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


public class RequiredReferences {
	private static final RequiredReferences NULL = new RequiredReferences();
	
	private Shell shell;
	private List<IWorkbenchPart> views;
	private List<IProject> projects;
	
	public static RequiredReferences fetchReferences() {
		
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if(workbenchWindow == null) {
			return NULL;
		}
		
		IWorkbenchPage activePage = workbenchWindow.getActivePage();
		if(activePage == null) {
			return NULL;
		}
		
		IWorkspaceRoot workspaceRoot = getWorkspaceRoot();
		if(workspaceRoot == null) {
			return NULL;
		}
		
		RequiredReferences requiredReferences = new RequiredReferences();
		requiredReferences.shell = workbenchWindow.getShell();
		requiredReferences.views = findViewToUse(activePage);
		requiredReferences.projects = new ArrayList<>(Arrays.asList(workspaceRoot.getProjects()));
		
		return requiredReferences;
	}
	
	public boolean allThere() {
		return Objects.nonNull(shell) && !views.isEmpty() && !projects.isEmpty();
	}
	
	private static IWorkspaceRoot getWorkspaceRoot() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if(workspace == null) {
			return null;
		}
		
		return workspace.getRoot();
	}
	
	private static List<IWorkbenchPart> findViewToUse(IWorkbenchPage activePage) {
		IWorkbenchPart activePart = activePage.getActivePart();
		String activeViewId = getPartViewId(activePart);
		
		if(ProjectViewIds.is(activeViewId)){
			return Collections.singletonList(activePart);
		}
		
		List<IWorkbenchPart> validViews = new ArrayList<>(2);
		for(IViewReference viewReference : activePage.getViewReferences()) {
			if(ProjectViewIds.is(viewReference.getId())) {
				validViews.add(viewReference.getPart(false));
			}
		}
		
		return validViews;
	}
	
	private static String getPartViewId(IWorkbenchPart part) {
		IWorkbenchPartSite workbenchPartSide = part.getSite();
		if(workbenchPartSide != null) {
			return workbenchPartSide.getId();
		}else {
			return "";
		}
	}

	public Shell getShell() {
		return shell;
	}

	public List<IWorkbenchPart> getViews() {
		return views;
	}

	public List<IProject> getProjects() {
		return projects;
	}
}
