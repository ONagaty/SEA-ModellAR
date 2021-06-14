/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Jan 30, 2012 3:14:24 PM </copyright>
 */
package eu.cessar.ct.runtime.ui.internal.classpath;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.pde.core.plugin.IPluginModelBase;

/**
 * @author uidu0944
 * 
 */

/**
 * @author uidu0944
 * 
 */
public class PluginModelBaseWrapper
{
	private IPluginModelBase pluginModelBase;
	private boolean readOnly;
	private boolean checked;
	private boolean changed;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	/**
	 * @param pluginModelBase
	 */
	public PluginModelBaseWrapper(IPluginModelBase pluginModelBase)
	{
		this.pluginModelBase = pluginModelBase;
	}

	/**
	 * @return the changed
	 */
	public boolean isChanged()
	{
		return changed;
	}

	/**
	 * @param propertyName
	 * @param listener
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * @param changed
	 *        the changed to set
	 */
	public void setChanged(boolean changed)
	{
		this.changed = changed;
	}

	/**
	 * @return the pluginModelBase
	 */
	public IPluginModelBase getPluginModelBase()
	{
		return pluginModelBase;
	}

	/**
	 * @param pluginModelBase
	 *        the pluginModelBase to set
	 */
	public void setPluginModelBase(IPluginModelBase pluginModelBase)
	{
		this.pluginModelBase = pluginModelBase;
	}

	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}

	/**
	 * @param readOnly
	 *        the readOnly to set
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked()
	{
		return checked;
	}

	/**
	 * @param checked
	 *        the checked to set
	 */
	public void setChecked(boolean checked)
	{
		boolean oldValue = this.checked;
		this.checked = checked;
		propertyChangeSupport.firePropertyChange("checked", oldValue, this.checked); //$NON-NLS-1$

	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final PluginModelBaseWrapper other = (PluginModelBaseWrapper) obj;
		if (pluginModelBase.getPluginBase().getId() != other.getPluginModelBase().getPluginBase().getId())
		{
			return false;
		}
		return true;
	}
}
