package dev.dietermai.projectselect;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import dev.dietermai.projectselect.internal.ImageDescriptorRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class ProjectSelectActivator extends AbstractUIPlugin {

	/** Unique id of this plug-in. */
	public static final String PLUGIN_ID = "dev.dietermai.projectselect"; //$NON-NLS-1$

	/** Default instance of this plug-in. */
	private static ProjectSelectActivator plugin;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		ImageDescriptorRegistry.imageDescriptorRegistry.dispose();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static ProjectSelectActivator getDefault() {
		return plugin;
	}

}
