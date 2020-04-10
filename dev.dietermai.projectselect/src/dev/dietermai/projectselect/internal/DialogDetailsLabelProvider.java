package dev.dietermai.projectselect.internal;

import static dev.dietermai.projectselect.internal.ImageDescriptorRegistry.imageDescriptorRegistry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.IWorkbenchAdapter;


enum DialogDetailsLabelProvider implements ILabelProvider {
	dialogDetailsLabelProvider;

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IAdaptable) {
			return getAdaptableImage((IAdaptable) element);
		}

		return null;
	}

	private Image getAdaptableImage(IAdaptable adaptable) {
		return imageDescriptorRegistry.get(getImageDescriptor(adaptable));
	}

	private ImageDescriptor getImageDescriptor(IAdaptable adaptable) {
		IWorkbenchAdapter wbAdapter= adaptable.getAdapter(IWorkbenchAdapter.class);
		if (wbAdapter == null) {
			return null;
		}
		
		ImageDescriptor descriptor= wbAdapter.getImageDescriptor(adaptable);
		if (descriptor == null) {
			return null;
		}
		return descriptor;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IProject) {
			return ((IProject) element).getName();
		}

		return null;
	}

}
