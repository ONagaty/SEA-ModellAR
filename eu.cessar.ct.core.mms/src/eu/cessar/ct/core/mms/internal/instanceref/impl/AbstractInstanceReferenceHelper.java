/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 23.04.2012 13:22:44 </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.model.IModelDescriptor;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.instanceref.IContextType;
import eu.cessar.ct.core.mms.instanceref.IContextValue;
import eu.cessar.ct.core.mms.instanceref.IInstanceReferenceCandidate;
import eu.cessar.ct.core.mms.instanceref.IInstanceReferenceHelper;
import eu.cessar.ct.core.mms.instanceref.InstanceReferenceUtils;
import eu.cessar.ct.core.mms.internal.instanceref.IContextToken;
import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;
import gautosar.gswcomponents.gcomponents.GRPortPrototype;

/**
 *
 * @author uidl6870
 *
 */
public abstract class AbstractInstanceReferenceHelper implements IInstanceReferenceHelper
{
	protected static final String STAR = "*"; //$NON-NLS-1$
	protected static final String QUESTION = "?"; //$NON-NLS-1$
	protected static final String PLUS = "+"; //$NON-NLS-1$

	private IModelDescriptor modelDescriptor;
	private IMetaModelService mmService;
	private EClass targetEClass;
	private IContextToken[] array;

	/* the token corresponding to the root (first) context type */
	private IContextToken rootToken;

	private List<IContextType> contextTypeWrappers;

	private Stack<GIdentifiable> contextStack = new Stack<GIdentifiable>();

	private Map<IContextToken, List<GIdentifiable>> contextTypeToValuesMap = new LinkedHashMap<IContextToken, List<GIdentifiable>>();

	private Map<GIdentifiable, IContextToken> map = new HashMap<GIdentifiable, IContextToken>();

	private Map<GIdentifiable, List<List<GIdentifiable>>> result = new HashMap<GIdentifiable, List<List<GIdentifiable>>>();

	private List<IInstanceReferenceCandidate> candidates = new ArrayList<IInstanceReferenceCandidate>();

	private int level = -1;

	private int same;

	private int sameOnLevel[];

	/**
	 * a flag indicating whether there are candidates for partial(incomplete) configuration
	 */
	private boolean hasCandidatesForIncompleteConfig;

	/**
	 * a flag indicating whether there are candidates for complete configuration of the instance reference
	 */
	private boolean hasCandidatesForCompleteConfig;

	/**
	 * @param modelDescriptor
	 */
	public AbstractInstanceReferenceHelper(IModelDescriptor modelDescriptor)
	{
		this.modelDescriptor = modelDescriptor;
		mmService = MMSRegistry.INSTANCE.getMMService(modelDescriptor.getMetaModelDescriptor());
	}

	/**
	 * @return the used meta-model service
	 */
	protected IMetaModelService getMetaModelService()
	{
		return mmService;
	}

	/**
	 * @param type
	 */
	protected void setTargetType(EClass type)
	{
		targetEClass = type;
	}

	/**
	 * @param contextTokens
	 * @throws InstanceRefConfigurationException
	 */
	protected void init(String[] contextTokens) throws InstanceRefConfigurationException
	{
		array = new IContextToken[contextTokens.length];

		computeContextTokens(contextTokens);

		computeContextTypeWrappers();
	}

	private static IInstanceReferenceCandidate createCandidate(boolean complete, List<IContextValue> contextValues,
		GIdentifiable target)
	{

		return new InstanceReferenceCandidate(target, contextValues, complete);
	}

	private void computeContextTypeWrappers() throws InstanceRefConfigurationException
	{
		contextTypeWrappers = new ArrayList<IContextType>();
		if (rootToken == null)
		{
			return;
		}

		IContextToken currentToken = rootToken;
		do
		{
			String context = currentToken.getContext();

			if (context != null)
			{
				boolean isRoot = rootToken == currentToken;
				IContextType wrapper = new ContextType(getEClass(context), isMany(currentToken), isRoot);

				currentToken.setContextType(wrapper);
				contextTypeWrappers.add(wrapper);

				currentToken = currentToken.getNext();
			}
		}
		while (currentToken != null);

	}

	/**
	 * @param name
	 * @throws InstanceRefConfigurationException
	 *
	 * @return the EClass with the given name
	 */
	protected EClass getEClass(String name) throws InstanceRefConfigurationException
	{
		String correctName = name;
		if (name.endsWith(STAR) || name.endsWith(QUESTION) || name.endsWith(PLUS))
		{
			correctName = name.substring(0, name.length() - 1);
		}

		if ("".equals(correctName)) //$NON-NLS-1$
		{
			return null;
		}
		EClass eClass = mmService.findEClass(correctName);
		if (eClass == null)
		{
			final String message = "Invalid context! Could not locate the EClass for <{0}>  type"; //$NON-NLS-1$
			throw new InstanceRefConfigurationException(NLS.bind(message, new Object[] {correctName}));
		}
		return eClass;
	}

	private void computeContextTokens(String[] contextTokens)
	{
		IContextToken prev = null;
		for (int i = 0; i < contextTokens.length; i++)
		{
			IContextToken curent = createContextToken(i, contextTokens[i]);

			array[i] = curent;
			if (i == 0)
			{
				rootToken = curent;
			}
			else
			{
				prev.setNext(curent);
			}
			prev = curent;
		}
		if (contextTokens.length > 0)
		{
			prev.setNext(null);
		}
	}

	/**
	 * @param index
	 * @param string
	 * @return the created token
	 */
	protected abstract IContextToken createContextToken(int index, String string);

	/**
	 * @return the first context token
	 */
	protected IContextToken getRootToken()
	{
		return rootToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IInstanceReferenceHelper#computeCandidates()
	 */
	public void computeCandidates() throws InstanceRefConfigurationException
	{
		resetDataFromPreviousComputation();

		if (targetEClass == null)
		{
			return;
		}
		if (rootToken == null)
		{
			// no context, result<- all instances of the target type
			List<GIdentifiable> targets = new ArrayList<GIdentifiable>();
			List<EObject> list = ModelUtils.getInstancesOfType((IProject) modelDescriptor.getRoot(), targetEClass,
				false, true);
			for (EObject eObject: list)
			{
				targets.add((GIdentifiable) eObject);
			}

			for (GIdentifiable target: targets)
			{
				IInstanceReferenceCandidate candidate = createCandidate(false, Collections.EMPTY_LIST, target);
				candidates.add(candidate);

				addCandidate(target, Collections.EMPTY_LIST, true);
			}
		}
		else
		{
			startCollecting(rootToken, true);

		}

		// printMap(result);
	}

	private void collect(List<GIdentifiable> contextList, IContextToken currentToken, boolean complete)
		throws InstanceRefConfigurationException
	{
		if (contextList.size() == 0)
		{
			// reached the finish
			startCollecting(currentToken, complete);
		}
		else
		{
			collectWhenContextHasMoreElements(contextList, currentToken, complete);
		}

	}

	private void startCollecting(IContextToken currentToken, boolean complete) throws InstanceRefConfigurationException
	{
		if (currentToken == null)
		{
			List<GIdentifiable> targetInstances = getAllInstancesOf(targetEClass);
			for (GIdentifiable target: targetInstances)
			{
				addCandidate(target, Collections.<GIdentifiable> emptyList(), complete);
			}
		}
		else
		{
			if (isOptional(currentToken))
			{
				level++;
				collect(new ArrayList<GIdentifiable>(), currentToken.getNext(), false);
				level--;
			}
			List<GIdentifiable> currentCtxInstances = getAllInstancesOf(currentToken.getContextType().getType());

			collectIfNotEmpty(currentCtxInstances, currentToken, complete);
		}
	}

	private void collectIfNotEmpty(List<GIdentifiable> filteredChidren, IContextToken currentToken, boolean complete)
		throws InstanceRefConfigurationException
	{
		if (filteredChidren.size() > 0)
		{
			collect(filteredChidren, currentToken, complete);
		}
	}

	private void collectWhenContextHasMoreElements(List<GIdentifiable> contextList, IContextToken currentToken,
		boolean complete) throws InstanceRefConfigurationException
	{
		for (GIdentifiable context: contextList)
		{
			List<EObject> children = getChildrenOfPrototype(context);

			if (children.size() == 0)
			{
				continue;
			}

			if (contextStack.contains(context))
			{
				Stack<GIdentifiable> copy = new Stack<GIdentifiable>();
				copy.addAll(contextList);
				reset();

				throw new InstanceRefConfigurationException(getLoopMsg(copy, copy.indexOf(context)));
			}

			if (!shouldAdd(currentToken, context))
			{
				continue;
			}

			if (!sameLevel(currentToken))
			{
				level++;
			}
			else
			{
				same++;
				sameOnLevel[level]++;
			}

			contextStack.push(context);

			if (!contextTypeToValuesMap.containsKey(currentToken))
			{
				contextTypeToValuesMap.put(currentToken, new ArrayList<GIdentifiable>());
			}

			contextTypeToValuesMap.get(currentToken).add(context);

			map.put(context, currentToken);

			// reached the finish
			IContextToken newToken = currentToken.getNext();
			if (newToken == null)
			{
				collectWhenReachedFinish(currentToken, complete, children);
			}
			else
			{
				collectIfNotReachedFinish(currentToken, complete, children, newToken);
			}
		}
	}

	/**
	 * @param currentToken
	 *        the context token for which the <code>context</code> element is about to be added on the context stack
	 * @param context
	 *        the element to be added on the stack
	 * @return whether given <code>context</code> element should be added on the context stack
	 */
	@SuppressWarnings("static-method")
	protected boolean shouldAdd(@SuppressWarnings("unused") IContextToken currentToken,
		@SuppressWarnings("unused") GIdentifiable context)
	{
		// default
		return true;
	}

	private void collectWhenReachedFinish(IContextToken currentToken, boolean complete, List<EObject> children)
		throws InstanceRefConfigurationException
	{
		lookupTarget(children, contextStack, complete);

		boolean isMany = false;
		if (isMany(array[level]))
		{
			isMany = true;

			List<GIdentifiable> filteredChidren = filterChildren(children, currentToken);
			collectIfNotEmpty(filteredChidren, currentToken, complete);
		}

		if (!sameLevel(currentToken) || !areManyOnSameLevel(level) || !isMany)
		{
			level--;
		}

		if (isMany && areManyOnSameLevel(level))
		{
			same--;
			sameOnLevel[level]--;
		}

		contextStack.pop();

		List<GIdentifiable> list = contextTypeToValuesMap.get(currentToken);
		list.remove(list.get(list.size() - 1));
		if (list.size() == 0)
		{
			contextTypeToValuesMap.remove(currentToken);
		}
	}

	private void collectIfNotReachedFinish(IContextToken currentToken, boolean complete, List<EObject> children,
		IContextToken newToken) throws InstanceRefConfigurationException
	{
		skipIfOptional(children, newToken, complete);

		List<GIdentifiable> filteredChidren = filterChildren(children, newToken);
		collectIfNotEmpty(filteredChidren, newToken, complete);

		boolean isMany = false;
		if (isMany(array[level]))
		{
			isMany = true;
			IContextToken token = array[level];

			filteredChidren = filterChildren(children, token);
			collectIfNotEmpty(filteredChidren, currentToken, complete);
		}

		if (!sameLevel(currentToken) || !areManyOnSameLevel(level))
		{
			level--;
		}

		if (isMany && areManyOnSameLevel(level))
		{
			same--;
			sameOnLevel[level]--;
		}

		contextStack.pop();

		List<GIdentifiable> list = contextTypeToValuesMap.get(currentToken);
		list.remove(list.size() - 1);
		if (list.size() == 0)
		{
			contextTypeToValuesMap.remove(currentToken);
		}
	}

	private void skipIfOptional(List<EObject> children, IContextToken token, boolean complete)
		throws InstanceRefConfigurationException
	{
		if (isOptional(token))
		{
			IContextToken nextToken = token.getNext();
			if (nextToken != null)
			{
				skipIfOptional(children, nextToken, complete);

				List<GIdentifiable> filteredChidren = filterChildren(children, nextToken);
				if (filteredChidren.size() > 0)
				{
					level++;
					collect(filteredChidren, nextToken, false);
					level--;
				}
			}
			else
			{
				lookupTarget(children, contextStack, complete);
			}

		}
	}

	private boolean areManyOnSameLevel(int currentLevel)
	{
		if (currentLevel < 0)
		{
			return false;
		}

		return sameOnLevel[currentLevel] > 0;
	}

	private boolean sameLevel(IContextToken token)
	{
		if (contextStack.size() == 0)
		{
			return false;
		}
		GIdentifiable peek = contextStack.peek();
		IContextToken lastToken = map.get(peek);
		return lastToken == token;
	}

	/**
	 * @param copy
	 * @param index
	 * @return
	 */
	private static String getLoopMsg(Stack<GIdentifiable> copy, int index)
	{
		StringBuffer sb = new StringBuffer();

		Collections.reverse(copy);

		GIdentifiable context = copy.get(index);

		sb.append("A loop has been identified within the configuration.\n"); //$NON-NLS-1$
		sb.append("Element: "); //$NON-NLS-1$
		sb.append(ModelUtils.getAbsoluteQualifiedName(context));
		sb.append(" already exists on the stack on position "); //$NON-NLS-1$
		sb.append(index + ".\n"); //$NON-NLS-1$
		sb.append("Context stack (from top to bottom):\n"); //$NON-NLS-1$
		for (GIdentifiable c: copy)
		{
			sb.append(ModelUtils.getAbsoluteQualifiedName(c) + "\n"); //$NON-NLS-1$
		}

		String msg = sb.substring(0, sb.lastIndexOf("\n")); //$NON-NLS-1$
		return msg;
	}

	/**
	 * @param children
	 * @param currentToken
	 * @return
	 */
	private List<GIdentifiable> filterChildren(List<EObject> children, IContextToken token)
		throws InstanceRefConfigurationException
	{
		List<GIdentifiable> filtered = new ArrayList<GIdentifiable>();

		String context = token.getContext();
		EClass currentType = getEClass(context);

		for (EObject child: children)
		{
			if (currentType.isInstance(child))
			{
				filtered.add((GIdentifiable) child);
			}
		}

		return filtered;
	}

	/**
	 * @param elements
	 *        list to be checked against target type
	 * @param currentContextStack
	 *        list containing the context elements towards possible targets
	 * @param complete
	 *        flag indicating whether the gathered contexts in the <code>currentContextStack</code> list cover all the
	 *        context types defined by the instance reference
	 */
	private void lookupTarget(List<EObject> elements, List<GIdentifiable> currentContextStack, boolean complete)
	{
		for (EObject element: elements)
		{
			if (targetEClass.isInstance(element))
			{
				// we have a valid path towards the target
				addCandidate((GIdentifiable) element, currentContextStack, complete);
			}
		}
	}

	/**
	 * @param target
	 * @param currentContextStack
	 * @param complete
	 */
	private void addCandidate(GIdentifiable target, List<GIdentifiable> currentContextStack, boolean complete)
	{
		List<GIdentifiable> list = new ArrayList<GIdentifiable>(currentContextStack);

		if (!result.containsKey(target))
		{
			result.put(target, new ArrayList<List<GIdentifiable>>());
		}
		result.get(target).add(list);

		List<IContextValue> values = new ArrayList<IContextValue>();

		Set<IContextToken> keySet = contextTypeToValuesMap.keySet();
		for (IContextToken key: keySet)
		{
			if (contextTypeToValuesMap.containsKey(key))
			{
				List<GIdentifiable> clist = contextTypeToValuesMap.get(key);
				if (!clist.isEmpty())
				{
					IContextValue cv = new ContextValue(key.getContextType(), new ArrayList<GIdentifiable>(clist));
					values.add(cv);
				}

			}
		}

		candidates.add(new InstanceReferenceCandidate(target, values, complete));

		hasCandidatesForCompleteConfig |= complete;
		hasCandidatesForIncompleteConfig |= !complete;
	}

	public List<IInstanceReferenceCandidate> getCandidates()
	{
		return candidates;
	}

	private static boolean isOptional(IContextToken token)
	{
		return token.getContext().endsWith(STAR) || token.getContext().endsWith(QUESTION);
	}

	private static boolean isMany(IContextToken token)
	{
		return token.getContext().endsWith(PLUS) || token.getContext().endsWith(STAR);
	}

	private static List<EObject> getChildrenOfPrototype(EObject element)
	{
		EObject typeOfPrototype = InstanceReferenceUtils.getTypeOfPrototype(element);

		if (typeOfPrototype == null)
		{
			return Collections.emptyList();
		}

		return typeOfPrototype.eContents();
	}

	@Requirement(
		reqID = "191184")
	private List<GIdentifiable> getAllInstancesOf(EClass eClass)
	{
		List<GIdentifiable> res = new ArrayList<GIdentifiable>();
		List<EObject> list = ModelUtils.getInstancesOfType((IProject) modelDescriptor.getRoot(), eClass, false, true);
		for (EObject eObject: list)
		{
			res.add((GIdentifiable) eObject);
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IInstanceReferenceHelper#getContextTypeWrappers()
	 */
	public List<IContextType> getContextTypes()
	{
		return contextTypeWrappers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IInstanceReferenceHelper#getTargetType()
	 */
	public EClass getTargetType()
	{
		return targetEClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IInstanceReferenceHelper#getCandidatesMap()
	 */
	public Map<GIdentifiable, List<List<GIdentifiable>>> getCandidatesMap()
	{
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IInstanceReferenceHelper#reset()
	 */
	public void reset()
	{
		// reset internal data set on init
		contextTypeWrappers.clear();

		rootToken = null;
		targetEClass = null;

		resetDataFromPreviousComputation();
	}

	private void resetDataFromPreviousComputation()
	{
		contextStack.clear();

		contextTypeToValuesMap.clear();

		result.clear();
		map.clear();
		level = -1;
		same = 0;
		sameOnLevel = new int[contextTypeWrappers.size()];

		hasCandidatesForCompleteConfig = false;
		hasCandidatesForIncompleteConfig = false;
	}

	/**
	 * @return the hasCandidatesForIncompleteConfig
	 */
	public boolean hasCandidatesForIncompleteConfig()
	{
		return hasCandidatesForIncompleteConfig;
	}

	/**
	 * @return the hasCandidatesForCompleteConfig
	 */
	public boolean hasCandidatesForCompleteConfig()
	{
		return hasCandidatesForCompleteConfig;
	}

	@SuppressWarnings("unused")
	private static void printMap(Map<GIdentifiable, List<List<GIdentifiable>>> map)
	{

		Set<GIdentifiable> keySet = map.keySet();
		int targetCount = 0;
		for (GIdentifiable target: keySet)
		{

			System.out.println("\nTarget" + ++targetCount + ":" //$NON-NLS-1$ //$NON-NLS-2$
				+ MetaModelUtils.getAbsoluteQualifiedName(target));

			List<List<GIdentifiable>> contexts = map.get(target);

			int count = 0;
			for (List<GIdentifiable> context: contexts)
			{

				System.out.println(" Context" + ++count + ":"); //$NON-NLS-1$ //$NON-NLS-2$

				for (GIdentifiable el: context)
				{
					if (el instanceof GRPortPrototype)
					{
						System.out.println("  " + MetaModelUtils.getAbsoluteQualifiedName(el) + " " + el.eContainer().eClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
		}
	}

}
