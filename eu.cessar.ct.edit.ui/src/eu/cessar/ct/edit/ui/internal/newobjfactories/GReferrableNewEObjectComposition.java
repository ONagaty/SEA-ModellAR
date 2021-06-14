/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 11.04.2013 18:02:55
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.newobjfactories;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Apr 16 19:14:58 2013 %
 * 
 *         %version: 1 %
 */
public class GReferrableNewEObjectComposition extends AbstractNewEObjectComposition
{

	/**
	 * @param owner
	 * @param feature
	 * @param children
	 */
	public GReferrableNewEObjectComposition(EObject owner, EStructuralFeature feature, EObject children)
	{
		super(owner, feature, children);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.newobjfactories.INewEObjectComposition#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControls(Composite parent)
	{
		Composite client = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		client.setLayout(layout);
		Label label = new Label(client, SWT.NONE);
		label.setText("ShortName");
	}
}
