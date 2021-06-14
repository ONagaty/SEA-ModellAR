/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 31.05.2013 14:05:14
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.runtime.ecuc.internal.Messages;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAErrorHandler;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.sea.util.SEAWriteOperationException;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * Base implementation of a list used by the SEA to operate on ECUC features of a particular container: child
 * containers, parameters or references.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:53 2015 %
 * 
 *         %version: 10 %
 * @param <E>
 * @param <T>
 * 
 */
public abstract class AbstractSeaEList<E, T> extends AbstractEList<E> implements ISEAList<E>
{

	private final ISeaOptions optionsHolder;
	private final ISEAModel seaModel;
	private final ISEAContainerParent parent;

	/**
	 * @param parent
	 *        SEA wrapper around the parent of the backing store list
	 * @param optionsHolder
	 *        the used SEA options store
	 */
	public AbstractSeaEList(ISEAContainerParent parent, ISeaOptions optionsHolder)
	{
		this.parent = parent;
		this.seaModel = parent.getSEAModel();
		this.optionsHolder = optionsHolder;
	}

	@Override
	@Requirement(
		reqID = "REQ_API#SEA#SPLIT#1")
	public boolean add(E e)
	{
		checkArgument(e);
		checkSplitAndNoActiveResource();
		updateSettingsForSplitList();

		boolean b = false;
		try
		{
			b = add(e, !optionsHolder.isReadOnly());
		}
		finally
		{
			checkAutoSave();
			resetActiveResourceForSplitList();
		}

		return b;

	}

	@Override
	public void add(int index, E e)
	{
		checkArgument(e);

		// no active configuration needed
		// checkSplitStatus();
		updateSettingsForSplitList();

		try
		{
			add(index, e, !optionsHolder.isReadOnly());
		}
		finally
		{
			checkAutoSave();
			resetActiveResourceForSplitList();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		checkSplitAndNoActiveResource();
		updateSettingsForSplitList();

		boolean b = false;
		try
		{
			b = addAll(c, !optionsHolder.isReadOnly());
		}
		finally
		{
			checkAutoSave();
			updateSettingsForSplitList();
		}

		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#remove(int)
	 */
	@Override
	public E remove(int index)
	{
		// active configuration not needed
		// checkSplitStatus();
		updateSettingsForSplitList();

		E removed = null;
		try
		{
			removed = remove(index, !optionsHolder.isReadOnly());
		}
		finally
		{
			checkAutoSave();
			updateSettingsForSplitList();
		}

		return removed;
	}

	/**
	 * @param index
	 * @param readWrite
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private E remove(final int index, boolean readWrite)
	{
		final Object[] o = new Object[1];
		if (!readWrite)
		{
			o[0] = get(index);
			doRemove(index);
		}
		else
		{
			try
			{
				updateModel(new Runnable()
				{
					@Override
					public void run()
					{
						o[0] = get(index);
						doRemove(index);
					}
				});
			}
			catch (ExecutionException e)
			{
				handleException(e);
			}
		}

		return (E) o[0];
	}

	/**
	 * @param index
	 * @return the element previously at the specified position
	 */
	protected abstract Object doRemove(int index);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	@Override
	public E set(int index, E e)
	{
		// no active configuration needed
		// checkSplitStatus();
		updateSettingsForSplitList();

		E old = null;
		try
		{
			old = set(index, e, !optionsHolder.isReadOnly());
		}
		finally
		{
			checkAutoSave();
			updateSettingsForSplitList();
		}

		return old;
	}

	@SuppressWarnings("unchecked")
	private E set(final int index, final E e, boolean readWrite)
	{
		final Object[] o = new Object[1];

		if (readWrite)
		{
			try
			{
				updateModel(new Runnable()
				{
					@Override
					public void run()
					{
						o[0] = doSet(index, e);
					}
				});
			}
			catch (ExecutionException e1)
			{
				handleException(e1);
			}
		}
		else
		{
			o[0] = super.set(index, e);
		}

		return (E) o[0];

	}

	/**
	 * @param index
	 * @param e
	 * @return
	 */
	private E doSet(final int index, final E e)
	{
		return super.set(index, e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o)
	{
		return indexOf(o) != -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#clear()
	 */
	@Override
	public void clear()
	{
		updateSettingsForSplitList();

		boolean readOnly = optionsHolder.isReadOnly();
		try
		{
			clear(!readOnly);
		}
		finally
		{
			checkAutoSave();
			resetActiveResourceForSplitList();
		}
	}

	/**
	 * @param readWrite
	 */
	private void clear(boolean readWrite)
	{
		if (!readWrite)
		{
			doClear();
		}
		else
		{
			try
			{
				updateModel(new Runnable()
				{
					@Override
					public void run()
					{
						doClear();
					}
				});
			}
			catch (ExecutionException e)
			{
				handleException(e);
			}
		}
	}

	/**
	 * 
	 */
	protected abstract void doClear();

	/**
	 * @param index
	 * @param e
	 * @param b
	 */
	private void add(final int index, final E e, boolean readWrite)
	{
		if (readWrite)
		{
			try
			{
				updateModel(new Runnable()
				{
					@Override
					public void run()
					{
						doAdd(index, e);
					}
				});
			}
			catch (ExecutionException e1)
			{
				handleException(e1);
			}
		}
		else
		{
			doAdd(index, e);
		}
	}

	/**
	 * Depending on the <code>readWrite</code> flag, will execute the operation in write transaction or not. In case
	 * <code>readWrite</code> is true, but the write transaction could not be completed, an unchecked exception is
	 * thrown indicating the cause.
	 * 
	 * @param e
	 *        element to be added
	 * @param readWrite
	 *        if <code>true</code>, the operation will be done in a write transaction.
	 */
	private boolean add(final E e, boolean readWrite)
	{
		final boolean[] b = new boolean[] {false};
		if (readWrite)
		{
			try
			{
				updateModel(new Runnable()
				{
					@Override
					public void run()
					{
						b[0] = doAdd(e);
					}
				});
			}
			catch (ExecutionException e1)
			{
				handleException(e1);
			}
		}
		else
		{
			b[0] = doAdd(e);
		}

		return b[0];
	}

	/**
	 * @param c
	 * @param b
	 * @return
	 */
	private boolean addAll(final Collection<? extends E> c, boolean readWrite)
	{
		final boolean[] b = new boolean[] {false};
		if (readWrite)
		{
			try
			{
				updateModel(new Runnable()
				{
					@Override
					public void run()
					{
						b[0] = doAddAll(c);
					}
				});
			}
			catch (ExecutionException e1)
			{
				handleException(e1);
			}
		}
		else
		{
			b[0] = doAddAll(c);
		}

		return b[0];
	}

	/**
	 * @param c
	 * @return true if this list changed as a result of the call
	 */
	protected boolean doAddAll(Collection<? extends E> c)
	{
		boolean b = false;
		for (E g: c)
		{
			b = doAdd(g);
		}

		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#isUnique()
	 */
	@Override
	protected boolean isUnique()
	{
		return false;
	}

	/**
	 * @param e
	 * @return true if this list changed as a result of the call
	 */
	protected abstract boolean doAdd(E e);

	private void doAdd(int index, E e)
	{
		super.add(index, e);

	}

	/**
	 * Executes the runnable in a write transaction.
	 * 
	 * @param runnable
	 * @throws ExecutionException
	 */
	protected void updateModel(Runnable runnable) throws ExecutionException
	{
		WorkspaceTransactionUtil.executeInWriteTransaction(getEditingDomain(), runnable, "update model"); //$NON-NLS-1$
	}

	/**
	 * Throws a runtime exception wrapping the cause of the given execution exception.
	 * 
	 * @param e
	 *        execution exception whose cause will be wrapped
	 */
	private static void handleException(ExecutionException e)
	{
		throw new SEAWriteOperationException(e.getCause());

	}

	/**
	 * Checks argument for <code>null</code>. If so, throws an unchecked exception.
	 * 
	 * @param e
	 */
	protected void checkArgument(E e)
	{
		if (e == null)
		{
			throw new IllegalArgumentException(Messages.Invalid_null_argument);
		}
	}

	/**
	 * @return the SEA used options store
	 */
	protected ISeaOptions getOptionsHolder()
	{
		return optionsHolder;
	}

	/**
	 * @return the used SEA error handler
	 */
	protected ISEAErrorHandler getErrorHandler()
	{
		return getOptionsHolder().getErrorHandler();
	}

	/**
	 * If autoSave flag is enabled, performs saving of the dirty resources
	 */
	private void checkAutoSave()
	{
		if (optionsHolder.isAutoSave())
		{
			performSave();
		}
	}

	private void performSave()
	{
		Collection<Resource> dirtyResources = ModelUtils.getDirtyResources(seaModel.getProject());
		ModelUtils.saveResources(dirtyResources, new NullProgressMonitor());
	}

	/**
	 * @return the used SEA model
	 */
	protected ISEAModel getSEAModel()
	{
		return seaModel;
	}

	/**
	 * Throws an unchecked exception if the storage list comes from a split parent and no active resource (temporary or
	 * global) is set
	 */
	protected abstract void checkSplitAndNoActiveResource();

	/**
	 * @return the active resource
	 */
	protected Resource getActiveResource()
	{
		ISEAConfig configuration = SeaUtils.getConfiguration(getParent());
		return SeaActiveConfigurationManager.INSTANCE.getActualActiveResource(configuration);
	}

	/**
	 * If the backing store list is split, it makes it read-write and updates its active resource
	 */
	protected abstract void updateSettingsForSplitList();

	/**
	 * If the backing store list is split, sets to <code>null</code> its active resource
	 */
	protected abstract void resetActiveResourceForSplitList();

	/**
	 * @return the parent of the storage list
	 */
	protected ISEAContainerParent getParent()
	{
		return parent;
	}

	/**
	 * @return the editing domain to be used
	 */
	protected TransactionalEditingDomain getEditingDomain()
	{

		GModuleDef moduleDef = SeaUtils.getConfiguration(parent).arGetDefinition();
		TransactionalEditingDomain domain = WorkspaceEditingDomainUtil.getEditingDomain(moduleDef);
		return domain;

	}

	/**
	 * @return the meta-model service
	 */
	protected IMetaModelService getMMService()
	{
		GModuleDef moduleDef = SeaUtils.getConfiguration(parent).arGetDefinition();
		return MMSRegistry.INSTANCE.getMMService(moduleDef.eClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		return super.addAll(index, c);
		// throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return super.listIterator();
		// throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return super.listIterator(index);
		// throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return super.subList(fromIndex, toIndex);
		// throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		return super.retainAll(c);
		// throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray()
	{
		return super.toArray();
		// throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.EList#move(int, int)
	 */
	@Override
	public E move(int newPosition, int oldPosition)
	{
		SeaUtils.checkSplitStatus(getParent());
		// updateSettingsForSplitList();

		E e = null;
		try
		{
			e = move(newPosition, oldPosition, !optionsHolder.isReadOnly());
		}
		finally
		{
			checkAutoSave();
			resetActiveResourceForSplitList();
		}

		return e;

	}

	/**
	 * @param newPosition
	 * @param oldPosition
	 * @param readWrite
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private E move(final int newPosition, final int oldPosition, boolean readWrite)
	{
		final Object[] o = new Object[1];
		if (readWrite)
		{
			try
			{
				updateModel(new Runnable()
				{
					@Override
					public void run()
					{
						o[0] = doMove(newPosition, oldPosition);
					}
				});
			}
			catch (ExecutionException e1)
			{
				handleException(e1);
			}
		}
		else
		{
			o[0] = doMove(newPosition, oldPosition);
		}

		return (E) o[0];
	}

	/**
	 * @param newPosition
	 * @param oldPosition
	 * @return the moved element
	 */
	protected abstract E doMove(int newPosition, int oldPosition);

	@Override
	public void move(int newPosition, E object)
	{
		SeaUtils.checkSplitStatus(getParent());

		updateSettingsForSplitList();

		super.move(newPosition, object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object o)
	{
		return super.indexOf(o);
		// throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#iterator()
	 */
	@Override
	public Iterator<E> iterator()
	{
		return super.iterator();
		// throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(Object o)
	{
		return super.lastIndexOf(o);
		// throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o)
	{
		return super.remove(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		updateSettingsForSplitList();

		return super.removeAll(c);
	}

	// =======================================================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addAllUnique(java.util.Collection)
	 */
	@Override
	public boolean addAllUnique(Collection<? extends E> collection)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addAllUnique(int, java.util.Collection)
	 */
	@Override
	public boolean addAllUnique(int index, Collection<? extends E> collection)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addAllUnique(int, java.lang.Object[], int, int)
	 */
	@Override
	public boolean addAllUnique(int index, Object[] objects, int start, int end)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#addAllUnique(java.lang.Object[], int, int)
	 */
	@Override
	public boolean addAllUnique(Object[] objects, int start, int end)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void addUnique(E object)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void addUnique(int index, E object)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#basicList()
	 */
	@Override
	protected List<E> basicList()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.util.AbstractEList#primitiveGet(int)
	 */
	@Override
	protected E primitiveGet(int index)
	{
		return get(index);
	}

	@Override
	public E setUnique(int index, E object)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 */
	protected void handleNoActiveResource()
	{
		String qualifiedName = SeaUtils.getQualifiedName(getParent());
		throw new SEAWriteOperationException(NLS.bind(Messages.NoActiveConfiguration, qualifiedName));
	}

}
