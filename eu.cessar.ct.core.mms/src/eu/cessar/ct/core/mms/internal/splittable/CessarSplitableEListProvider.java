/**
 * <copyright>
 *
 * Copyright (c) BMW Car IT and others. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Artop Software License Based on AUTOSAR Released Material (ASLR) which accompanies
 * this distribution, and is available at http://www.artop.org/aslr.html
 *
 * Contributors: BMW Car IT - Initial API and implementation
 *
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.splittable;

import static org.artop.aal.gautosar.services.splitting.Splitables.getESplitableObject;
import static org.artop.aal.gautosar.services.splitting.Splitables.getSplitable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.artop.aal.gautosar.services.splitting.Splitables;
import org.artop.aal.gautosar.services.splitting.handler.SplitableEListsProvider;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.google.common.collect.Lists;

import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.core.platform.util.Pair;
import eu.cessar.ct.sdk.utils.SplitUtils;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

public class CessarSplitableEListProvider extends SplitableEListsProvider.Default
{

	@Override
	public EList<Object> createSplitableEListFor(EObject proxy, EStructuralFeature eReference, List<Object> data)
	{
		return new CessarSplitableEObjectResolvingEList(getType(eReference), (InternalEObject) proxy,
			(EReference) eReference, data, true);
	}

	@Override
	public EList<Object> createNonSplitableEListFor(EObject proxy, EStructuralFeature eReference, List<Object> data)
	{
		return new CessarSplitableEObjectResolvingEList(getType(eReference), (InternalEObject) proxy,
			(EReference) eReference, data, false);
	}

	private Class<?> getType(EStructuralFeature eFeature)
	{
		return eFeature.getEType().getInstanceClass();
	}

	@SuppressWarnings("serial")
	public static final class CessarSplitableEObjectResolvingEList extends EObjectResolvingEList<Object>
	{
		private final boolean isSplitable;
		private EReference eReference;

		private CessarSplitableEObjectResolvingEList(Class<?> dataClass, InternalEObject owner, EReference eReference,
			List<Object> data, boolean isSplitable)
		{
			super(dataClass, owner, eReference.getFeatureID());
			this.eReference = eReference;
			this.isSplitable = isSplitable;
			Object[] values;
			if (data == null)
			{
				values = new Object[0];
			}
			else
			{
				values = data.toArray();
			}
			setData(values.length, values);
		}

		@Override
		public void clear()
		{
			if (isSplitable)
			{
				clearChildFragments();
				setData(0, new Object[] {});
			}
			else
			{
				EList<Object> listOfElements = getWrappedList();
				listOfElements.clear();
				setData(listOfElements.size(), listOfElements.toArray());
			}
		}

		@Override
		public NotificationChain inverseAdd(Object object, NotificationChain notifications)
		{
			return notifications;
		}

		@Override
		protected boolean isNotificationRequired()
		{
			return false;
		}

		@Override
		public boolean contains(Object object)
		{
			return indexOf(object) != -1; // do not perform the check for duplicates
		}

		@Override
		public int indexOf(Object object)
		{
			GARObject garObject = (GARObject) object;
			if (SplitUtils.isMergedInstace(garObject))
			{
				return super.indexOf(object);
			}

			Object mergedInstance = SplitUtils.getMergedInstance(garObject);
			return super.indexOf(mergedInstance);
		}

		@Override
		public void addUnique(Object object)
		{
			addUnique(size(), object);
		}

		/**
		 * Find the fragment of the owner in the active resource.
		 *
		 * @return the active owner fragment
		 */
		private EObject getActiveFragmentOfOwner()
		{
			return SplitUtils.getActiveInstance((GARObject) owner);
		}

		/**
		 * Find the target fragment and the index inside it.
		 *
		 * @param index
		 *        the target (global) index
		 * @return a {@linkplain Pair} of the fragment and fragment-relative index
		 */
		private Pair<EList<Object>, Integer> findFragmentAndIndexForAdd(int index)
		{
			int cSize = size();

			List<EObject> allFragmentsOfOwner = SplitableUtils.INSTANCE.getAllFragments((Splitable) owner);

			EList<Object> targetFragment = null;
			EList<Object> currentFragment = null;

			int indexInFragment = -1;

			if (cSize == 0)
			{
				// if not set in any fragment, obtain target based on active resource.
				targetFragment = (EList<Object>) getActiveFragmentOfOwner().eGet(eReference);
				indexInFragment = 0;
			}
			// optimize for additions at the end
			else if (cSize == index)
			{
				// choose the last fragment containing elements
				for (EObject fragmentOfOwner: allFragmentsOfOwner)
				{
					currentFragment = (EList<Object>) fragmentOfOwner.eGet(eReference);
					if (!currentFragment.isEmpty())
					{
						targetFragment = currentFragment;
						indexInFragment = targetFragment.size();
					}
				}
			} // add on an index inside the list; determine its corresponding fragment
			else
			{
				GARObject targetIndexObj = (GARObject) get(index);
				Splitable targetMergedInstance = (Splitable) SplitUtils.getMergedInstance(targetIndexObj);
				List<EObject> allFragments = SplitableUtils.INSTANCE.getAllFragments(targetMergedInstance);
				EObject firstTargetFragment = allFragments.get(0);

				// Place the new object before the first fragment of the object that is currently on the given index.
				for (EObject fragmentOfOwner: allFragmentsOfOwner)
				{
					currentFragment = (EList<Object>) fragmentOfOwner.eGet(eReference);
					int firstTargetFragmentIdx = currentFragment.indexOf(firstTargetFragment);
					if (firstTargetFragmentIdx > -1)
					{
						targetFragment = currentFragment;
						indexInFragment = firstTargetFragmentIdx;
						break;
					}
				}
			}

			return new Pair<EList<Object>, Integer>(targetFragment, indexInFragment);
		}

		/**
		 * Check validity of target index for add operation.
		 *
		 * @param index
		 *        the target index
		 */
		private void checkIndex(int index)
		{
			if (index < 0)
			{
				throw new IndexOutOfBoundsException("Add at negative index: " + index); //$NON-NLS-1$
			}

			int cSize = size();
			if (cSize < index)
			{
				throw new IndexOutOfBoundsException("Add at index " + index + ", but list size is: " + cSize); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		@Override
		public void addUnique(int index, Object object)
		{
			addAllUnique(index, Arrays.asList(object));
		}

		@Override
		public boolean addAllUnique(Collection<? extends Object> collection)
		{
			return addAllUnique(size(), collection);
		}

		private Collection<? extends Object> unwrapCollection(Collection<? extends Object> collection)
		{
			if (collection == null)
			{
				return Collections.emptyList();
			}
			List<Object> unwrapped = Lists.newArrayList();
			for (Object o: collection)
			{
				unwrapped.add(unwrap(o));
			}
			return unwrapped;
		}

		@Override
		public boolean addAllUnique(int index, Collection<? extends Object> collection)
		{
			checkIndex(index);

			EList<Object> listOfElements = getWrappedList();
			Collection<? extends Object> unwrappedCollection = unwrapCollection(collection);

			if (isSplitable)
			{
				Pair<EList<Object>, Integer> fragIndex = findFragmentAndIndexForAdd(index);
				EList<Object> currentFragment = fragIndex.getKey();
				int indexInFragment = fragIndex.getValue();
				currentFragment.addAll(indexInFragment, collection);
				List<Splitable> listOfSplitedChildrenOfFeature = (List<Splitable>) owner.eGet(eReference);
				int sizeBefore = size();
				setData(listOfSplitedChildrenOfFeature.size(), listOfSplitedChildrenOfFeature.toArray());
				int sizeAfter = size();
				return sizeBefore != sizeAfter;
			}

			boolean addAll = listOfElements.addAll(index, unwrappedCollection);
			setData(listOfElements.size(), listOfElements.toArray());
			return addAll;
		}

		private EList<Object> getWrappedList()
		{
			EObject orginalOrFragment;
			if (isSplitable)
			{
				orginalOrFragment = Splitables.unwrap(owner);
			}
			else
			{
				orginalOrFragment = getESplitableObject(getSplitable(owner)).getOriginal();
			}
			return (EList<Object>) orginalOrFragment.eGet(eReference);
		}

		/**
		 * Clear all fragments that make up this list.
		 */
		private void clearChildFragments()
		{
			List<EObject> allFragmentsOfOwner = SplitableUtils.INSTANCE.getAllFragments((Splitable) owner);

			for (EObject fragmentOfOwner: allFragmentsOfOwner)
			{
				EList<Object> objsInFragment = (EList<Object>) fragmentOfOwner.eGet(eReference);
				objsInFragment.clear();
			}
		}

		/**
		 * Clear all fragments that make up this merged object.
		 */
		private void clearObjectFragments(Splitable object)
		{
			List<EObject> allFragmentsOfObject = SplitableUtils.INSTANCE.getAllFragments(object);

			for (EObject fragmentOfObject: allFragmentsOfObject)
			{
				EObject parent = fragmentOfObject.eContainer();
				EList<Object> objsInFragment = (EList<Object>) parent.eGet(eReference);
				objsInFragment.remove(fragmentOfObject);
			}
		}

		/**
		 * Remove the element at position {@code index} from its {@linkplain owner}.
		 *
		 * @param index
		 *        the index of the object inside its merged containing feature
		 * @return the updated value of the {@code owner}'s containing feature
		 */
		private List<Splitable> removeChildFragments(Object toRemove, int index)
		{
			if (!(toRemove instanceof EObject))
			{
				return (List<Splitable>) owner.eGet(eReference);
			}

			if (toRemove instanceof Splitable)
			{
				clearObjectFragments((Splitable) toRemove);
				return (List<Splitable>) owner.eGet(eReference);
			}

			EObject eToRemove = (EObject) toRemove;
			EObject parent = eToRemove.eContainer();
			List<EObject> containmentRef = (List<EObject>) parent.eGet(eReference);
			containmentRef.remove(index);

			return (List<Splitable>) owner.eGet(eReference);
		}

		@Override
		public boolean remove(Object object)
		{
			if (isSplitable)
			{
				List<Splitable> listOfSplitedChildrenOfFeature = (List<Splitable>) owner.eGet(eReference);
				int index = listOfSplitedChildrenOfFeature.indexOf(object);
				if (0 > index)
				{
					return false;
				}
				listOfSplitedChildrenOfFeature = removeChildFragments(object, index);
				setData(listOfSplitedChildrenOfFeature.size(), listOfSplitedChildrenOfFeature.toArray());
				return true;
			}
			EList<Object> listOfElements = getWrappedList();
			Object unwrapped = unwrap(object);
			boolean result = listOfElements.remove(unwrapped);
			setData(listOfElements.size(), listOfElements.toArray());
			return result;
		}

		@Override
		public Object remove(int index)
		{
			if (isSplitable)
			{
				List<Splitable> listOfSplitedChildrenOfFeature = (List<Splitable>) owner.eGet(eReference);
				Splitable elemToBeRemoved = listOfSplitedChildrenOfFeature.get(index);
				listOfSplitedChildrenOfFeature = removeChildFragments(elemToBeRemoved, index);
				setData(listOfSplitedChildrenOfFeature.size(), listOfSplitedChildrenOfFeature.toArray());
				return elemToBeRemoved;
			}

			EList<Object> listOfElements = getWrappedList();
			Object result = listOfElements.remove(index);
			setData(listOfElements.size(), listOfElements.toArray());
			return result;
		}

		@Override
		public boolean removeAll(Collection<?> collection)
		{
			// If the argument is the list itself, delegate to the faster clear.
			if (this == collection)
			{
				clear();
				return true;
			}

			// aux list for the case when collection is a sublist of this one.
			List<Object> aux = new ArrayList<Object>(collection.size());
			aux.addAll(collection);

			boolean result = true;
			for (Object obj: aux)
			{
				result &= remove(obj);
			}

			return result;
		}

		private Object unwrap(Object object)
		{
			if (object instanceof EObject)
			{
				return Splitables.unwrap((EObject) object);
			}
			return object;
		}
	}
}
