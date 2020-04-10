package dev.dietermai.projectselect.internal;

import java.util.Objects;

public enum ProjectViewIds {
	;

	static final String VIEW_ID_PROJECT_EXPLORER = "org.eclipse.ui.navigator.ProjectExplorer";
	static final String VIEW_ID_PACKAGE_EXPLORER = "org.eclipse.jdt.ui.PackageExplorer";

	/**
	 * @param id The id of the view.
	 * @return true if the given id is one of an view that is supported
	 */
	public static boolean is(final String id) {
		return isPackage(id) || isProject(id);
	}

	/**
	 * @param id The view id that is checked.
	 * @return true if given view id is the id of the packaged explorer
	 */
	public static boolean isPackage(final String id) {
		return Objects.equals(VIEW_ID_PACKAGE_EXPLORER, id);
	}

	/**
	 * @param id The view id that is checked.
	 * @return true if given view id is the id of the project explorer
	 */
	public static boolean isProject(final String id) {
		return Objects.equals(VIEW_ID_PROJECT_EXPLORER, id);
	}
}
