/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Aug 29, 2011 6:09:28 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.composition;

import eu.cessar.ct.edit.ui.facility.composition.ECompositionType;
import eu.cessar.ct.edit.ui.facility.composition.EcucCategory;
import eu.cessar.ct.edit.ui.facility.composition.IEcucCompostion;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * @author uidl6870
 * 
 */
public class ECUCComposition extends AbstractEditorComposition<EcucCategory> implements
	IEcucCompostion
{
	/**
	 * @param compositionProvider
	 */
	public ECUCComposition(IEditorCompositionProvider compositionProvider)
	{
		super(compositionProvider);
	}

	private EcucCategory category;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#getType()
	 */
	public ECompositionType getType()
	{
		return ECompositionType.ECUC;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#getCategory()
	 */
	public EcucCategory getCategory()
	{
		return category;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.IEditorComposition#setCategory(eu.cessar.ct.edit.ui.ICompositionCategory)
	 */
	public void setCategory(EcucCategory category)
	{
		this.category = category;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append(super.toString());

		sb.append("\nInput type: "); //$NON-NLS-1$

		GParamConfContainerDef value = getCategory().getInput();
		sb.append(ModelUtils.getAbsoluteQualifiedName(value));

		return sb.toString();
	}

}
