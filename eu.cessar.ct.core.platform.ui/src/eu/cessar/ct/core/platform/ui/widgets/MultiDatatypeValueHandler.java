/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 12, 2010 11:26:30 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler;
import eu.cessar.ct.sdk.IPostBuildContext;

/**
 * An IMultiValueHandler used with IMultiValueDatatypeEditor
 * 
 * @param <T>
 *
 */
public class MultiDatatypeValueHandler<T> implements IMultiValueHandler<T>
{

	private IMultiValueDatatypeEditor<T> editor;
	private IDatatypeEditor<T> singleEditor;
	private ILabelProvider labelProvider;
	private String pluralTypeName;
	private String singularTypeName;
	private T defaultData;

	/**
	 * @param labelProvider
	 * @param singularTypeName
	 * @param pluralTypeName
	 */
	public MultiDatatypeValueHandler(ILabelProvider labelProvider, String singularTypeName, String pluralTypeName)
	{
		this.labelProvider = labelProvider;
		this.singularTypeName = singularTypeName;
		this.pluralTypeName = pluralTypeName;
	}

	/**
	 * @param newEditor
	 */
	public void setMultiValueDatatypeEditor(IMultiValueDatatypeEditor<T> newEditor)
	{
		this.editor = newEditor;
	}

	/**
	 * @return
	 */
	private IDatatypeEditor<T> getSingleEditor()
	{
		if (singleEditor == null)
		{
			singleEditor = editor.createSingleDatatypeEditor();
		}
		return singleEditor;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#createSingleValueEditor(org.eclipse.swt.widgets.Composite
	 * )
	 */
	public Control createSingleValueEditor(Composite parent)
	{
		return createSingleValueEditor(parent, false, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getEditorValue()
	 */
	public List<T> getEditorValue()
	{
		List<T> returnList = new ArrayList<T>();
		T inputData = getSingleEditor().getInputData();
		returnList.add(inputData);
		return returnList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#setEditorValue(java.lang.Object)
	 */
	public void setEditorValue(T value)
	{
		getSingleEditor().setInputData(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#clearEditorValue()
	 */
	public void clearEditorValue()
	{
		getSingleEditor().setInputData(defaultData);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getMaxValues()
	 */
	public int getMaxValues()
	{
		// TODO Auto-generated method stub
		return editor.getMaxValues();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getValues()
	 */
	public List<T> getValues()
	{
		return editor.getInputData();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#acceptValues(java.util.List)
	 */
	public boolean acceptValues(List<T> newValues)
	{
		return editor.acceptNewValues(newValues);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getLabelProvider()
	 */
	public ILabelProvider getLabelProvider()
	{
		// TODO Auto-generated method stub
		return labelProvider;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getTypeName(boolean)
	 */
	public String getTypeName(boolean plural)
	{
		if (plural)
		{
			return pluralTypeName;
		}
		else
		{
			return singularTypeName;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#createSingleValueEditor(org.eclipse.swt.widgets.Composite
	 * , boolean)
	 */
	public Control createSingleValueEditor(Composite parent, boolean isPBFiltering, IPostBuildContext pBContext)
	{
		return getSingleEditor().createEditor(parent);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#setInputObject(org.eclipse.emf.ecore.EObject)
	 */
	public void setInputObject(EObject object)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#getInputObject()
	 */
	public EObject getInputObject()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler#computeCandidateList(boolean,
	 * eu.cessar.ct.sdk.IPostBuildContext)
	 */
	public Control computeCandidateList(boolean isPBFiltering, IPostBuildContext pBContext)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
