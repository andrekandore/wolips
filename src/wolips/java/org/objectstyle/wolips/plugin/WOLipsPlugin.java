/* ====================================================================
 * 
 * The ObjectStyle Group Software License, Version 1.0 
 *
 * Copyright (c) 2002 The ObjectStyle Group 
 * and individual authors of the software.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        ObjectStyle Group (http://objectstyle.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "ObjectStyle Group" and "Cayenne" 
 *    must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact andrus@objectstyle.org.
 *
 * 5. Products derived from this software may not be called "ObjectStyle"
 *    nor may "ObjectStyle" appear in their names without prior written
 *    permission of the ObjectStyle Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE OBJECTSTYLE GROUP OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the ObjectStyle Group.  For more
 * information on the ObjectStyle Group, please see
 * <http://objectstyle.org/>.
 *
 */
package org.objectstyle.wolips.plugin;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.objectstyle.wolips.logging.WOLipsLog;
import org.objectstyle.wolips.logging.WOLipsLogFactory;
import org.objectstyle.wolips.preferences.Preferences;
import org.objectstyle.wolips.wo.Foundation;
import org.objectstyle.woproject.env.WOVariables;
/**
 * The main plugin class to be used in the desktop.
 * 
 * @author uli
 * @author markus
 */
public class WOLipsPlugin extends AbstractUIPlugin implements IStartup {
	//The plugin.
	private static WOLipsPlugin plugin;
	/**
	 * Set this variable to true to get debug output
	 */
	public static final boolean debug = true;
	/**
	 * The constructor.
	 */
	public WOLipsPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		// set log factory
		System.setProperty(
			"org.apache.commons.logging.LogFactory",
			"org.objectstyle.wolips.logging.WOLipsLogFactory");
		LogFactory.getFactory().setAttribute(WOLipsLogFactory.ATTR_GLOBAL_LOG_LEVEL,new Integer(WOLipsLog.TRACE));
		WOVariables.log.debug("test");
		Foundation.loadFoundationClasses();
		Preferences.setDefaults();
	}
	/**
	 * Calls EarlyStartup.earlyStartup().
	 * <br>
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		EarlyStartup.earlyStartup();
	}
	/**
	 * Returns an ImageDescriptor.
	 */
	public ImageDescriptor getImageDescriptor(String name) {
		try {
			URL url = new URL(getDescriptor().getInstallURL(), name);
			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}
	/**
	 * Returns the shared instance.
	 */
	public static WOLipsPlugin getDefault() {
		if (plugin == null) {
			// ensure plugin instance is always available using id
			return new WOLipsPlugin(
				Platform
					.getPlugin(IWOLipsPluginConstants.PLUGIN_ID)
					.getDescriptor());
		}
		return plugin;
	}
	/**
	 * Method baseURL.
	 * @return URL
	 */
	public static URL baseURL() {
		return WOLipsPlugin.getDefault().getDescriptor().getInstallURL();
	}
	/**
	 * Method handleException.
	 * @param shell
	 * @param target
	 * @param message
	 */
	public static void handleException(
		Shell shell,
		Throwable target,
		String message) {
		WOLipsLog.debug(target);
		String title = "Error";
		if (message == null) {
			message = target.getMessage();
		}
		if (target instanceof CoreException) {
			IStatus status = ((CoreException) target).getStatus();
			ErrorDialog.openError(shell, title, message, status);
			WOLipsLog.log(status);
		} else {
			MessageDialog.openError(shell, title, target.getMessage());
			WOLipsLog.log(target);
		}
	}
	/**
	 * Returns the PluginID.
	 */
	public static String getPluginId() {
		if (plugin != null) {
			return getDefault().getDescriptor().getUniqueIdentifier();
		} else
			return IWOLipsPluginConstants.PLUGIN_ID;
	}
}
