package dev.dietermai.projectselect.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public enum ImageDescriptorRegistry {
	imageDescriptorRegistry;

	private Map<ImageDescriptor, Image> registry = new HashMap<>(10);
	private Display display;

	ImageDescriptorRegistry() {
		display = PlatformUI.getWorkbench().getDisplay();
		Objects.requireNonNull(display, "display is null");
		if(!display.isDisposed()) {
			display.asyncExec(() -> display.disposeExec(this::dispose));
		}
	}

	public Image get(ImageDescriptor descriptor) {
		if (descriptor == null) {
			return null;
		}

		Image result = registry.get(descriptor);
		if (result != null) {
			return result;
		}

		result = descriptor.createImage();
		if (result != null) {
			registry.put(descriptor, result);
		}
		return result;
	}

	public void dispose() {
		registry.values().forEach(image -> image.dispose());
		registry.clear();
	}
}