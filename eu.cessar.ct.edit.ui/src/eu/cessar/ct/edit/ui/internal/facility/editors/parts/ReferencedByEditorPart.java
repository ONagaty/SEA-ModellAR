/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jun 18, 2010 3:17:45 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors.parts;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.ui.provider.TransactionalAdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import eu.cessar.ct.core.mms.EResourceUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.adapter.CessarAdapterFactoryFactory;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.internal.Messages;
import eu.cessar.ct.edit.ui.utils.EditUtils;
import eu.cessar.req.Requirement;

/**
 * @author uidl6458
 * 
 */
@Requirement(
	reqID = "REQ_EDIT_PROP#REFERENCED_BY#1")
public class ReferencedByEditorPart implements IEditorPart, ISearchReferencesMonitor
{
	/**
	 * @author uidu3379
	 * 
	 */
	private final class ReferencedByEditorLabelProvider extends TransactionalAdapterFactoryLabelProvider
	{
		/**
		 * @param domain
		 * @param adapterFactory
		 */
		private ReferencedByEditorLabelProvider(TransactionalEditingDomain domain, AdapterFactory adapterFactory)
		{
			super(domain, adapterFactory);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.emf.transaction.ui.provider.TransactionalAdapterFactoryLabelProvider#getColumnText(java.lang.
		 * Object, int)
		 */
		@Override
		public String getColumnText(Object object, int columnIndex)
		{
			return getText(object);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.emf.transaction.ui.provider.TransactionalAdapterFactoryLabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object object)
		{
			if (object instanceof EObject)
			{
				EObject obj = (EObject) object;
				Resource eRes = obj.eResource();
				if (eRes != null)
				{
					String fragment = eRes.getURIFragment(obj);
					if (fragment != null)
					{
						return fragment;
					}
				}
			}
			return super.getText(object);
		}
	}

	private IModelFragmentEditor editor;
	private TableViewer tableViewer;
	private Composite contentParent;

	/**
	 * @param editor
	 * @param referencedByEditor
	 */
	public ReferencedByEditorPart(IModelFragmentEditor editor)
	{
		// super(editor);
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEditor(eu.cessar.ct.edit.ui.facility.
	 * IModelFragmentEditor)
	 */
	public void setEditor(IModelFragmentEditor editor)
	{
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#getEditor()
	 */
	public IModelFragmentEditor getEditor()
	{
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent)
	{
		contentParent = parent;
		Composite composite = editor.getFormToolkit().createComposite(parent);

		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 5;
		layout.marginWidth = 2;
		composite.setLayout(layout);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(composite, SWT.WRAP);
		label.setText(Messages.ReferenceLabel);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		tableViewer = new TableViewer(composite, SWT.FILL | SWT.NO_SCROLL | SWT.BORDER);

		Table control = (Table) tableViewer.getControl();
		GridData gridDataTable = new GridData(SWT.FILL, SWT.FILL, true, true);
		control.setLayoutData(gridDataTable);

		tableViewer.addDoubleClickListener(new IDoubleClickListener()
		{

			public void doubleClick(DoubleClickEvent event)
			{
				EObject eObject = PlatformUIUtils.getObjectFromSelection(EObject.class, event.getSelection());
				if (eObject != null)
				{
					EditUtils.openEditor(eObject, true);
				}
			}
		});

		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	public void dispose()
	{
		tableViewer = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	public void refresh()
	{
		EObject input = editor.getInput();
		if (input != null && tableViewer != null && !tableViewer.getControl().isDisposed())
		{
			IProject project = MetaModelUtils.getProject(input);
			if (project == null)
			{
				return;
			}
			TransactionalEditingDomain editingDomain = MetaModelUtils.getEditingDomain(project);
			AdapterFactory adapterFactory = CessarAdapterFactoryFactory.eINSTANCE.getAdapterFactory(editingDomain);
			tableViewer.setLabelProvider(getLabelProvider(editingDomain, adapterFactory));
			tableViewer.setContentProvider(ArrayContentProvider.getInstance());

			Set<String> refs = new HashSet<String>();
			refs.add(Messages.ReferenceLoading_message);
			tableViewer.setInput(refs);

			LoadReferenceTask refLoadTask = new LoadReferenceTask(this, editor.getInput(),
				EResourceUtils.getProjectResources(project));
			refLoadTask.start();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.internal.facility.editors.parts.CallbackInterface#returnResult(java.lang.Object)
	 */
	public void setReferences(final Collection<EObject> refs)
	{
		Display display = Display.getDefault();
		if (null != display)
		{
			display.asyncExec(new Runnable()
			{
				public void run()
				{
					// return if TableViewr is null
					if (tableViewer == null)
					{
						return;
					}

					Control control = tableViewer.getControl();

					// return is control is null or disposed
					if (control == null || control.isDisposed())
					{
						return;
					}

					tableViewer.setInput(refs);

					// Resizes the ScrolledComposite to display the scroll bars corectly in the Property page.
					// Get the ScrolledComposite to send the resize event
					// the composite with label and table cannot be null.
					// the parent cannot be null or an exception is thrown when the current widget is created
					Composite sc = contentParent.getParent();
					// SC might be null.

					// skip the notifyListeners
					if (sc == null)
					{
						return;
					}
					if (!(sc instanceof ScrolledComposite))
					{
						return;
					}
					sc.notifyListeners(SWT.Resize, new Event());
				}
			});
		}
	}

	/**
	 * @param editingDomain
	 * @param adapterFactory
	 * @return
	 */
	private ILabelProvider getLabelProvider(TransactionalEditingDomain editingDomain, AdapterFactory adapterFactory)
	{
		return new ReferencedByEditorLabelProvider(editingDomain, adapterFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.internal.facility.editors.parts.ISearchReferencesMonitor#isCanceled()
	 */
	public boolean isCanceled()
	{
		if (tableViewer != null)
		{
			return tableViewer.getControl().isDisposed();
		}
		else
		{
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled)
	{
		if (tableViewer != null)
		{
			Control control = tableViewer.getControl();
			control.setEnabled(enabled);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setFocus()
	 */
	public void setFocus()
	{
		// TODO Auto-generated method stub

	}
}
