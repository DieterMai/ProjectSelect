package dev.dietermai.projectselect.internal;

import static dev.dietermai.projectselect.internal.ImageDescriptorRegistry.imageDescriptorRegistry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.IWorkbenchAdapter;

public enum ProjectListLabelProvider implements ILabelProvider{

	projectListLabelProvider;
	
	@Override
	public void addListener(ILabelProviderListener listener) {
	}
	
	@Override
	public void removeListener(ILabelProviderListener listener) {
	}


	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
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
		if(element == null) {
			return "invalid (null)";
		}else if(element instanceof IProject) {
			IProject project = (IProject) element;
			if(project.isOpen()) {
				return project.getName();
			}else {
				return project.getName()+" [CLOSED]";
			}
		}else if(element instanceof String)
			return (String) element;
		else{
			return "invalid (class: "+element.getClass()+") "+element;
		}
	}

}
