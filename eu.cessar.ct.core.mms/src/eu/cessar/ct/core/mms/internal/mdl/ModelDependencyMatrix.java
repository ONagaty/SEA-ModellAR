/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 21.05.2013 15:08:03
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.mdl;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import eu.cessar.ct.sdk.collections.BitMatrix;

/**
 * Generates graphs representing the containment and association relations in an EMF model to improve search
 * capabilities with broad dependency information.
 * 
 * @author uidl6458
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Wed Jul 16 15:36:59 2014 %
 * 
 *         %version: 6 %
 */
public class ModelDependencyMatrix implements IModelDependencyMatrix
{
	/**
	 * The metamodel's containment adjacency matrix.
	 */
	private BitMatrix containmentAdjacencyMatrix;
	/**
	 * The metamodel's containment transitive closure (route) matrix.
	 */
	private BitMatrix containmentClosureMatrix;

	/**
	 * Only needed for debug.
	 */
	// private EClass[] containmentClasses;

	/**
	 * References considered for containment.
	 */
	private EReference[] containmentReferences;

	/**
	 * The metamodel's non-containment adjacency matrix.
	 */
	private BitMatrix nonContainmentAdjacencyMatrix;
	/**
	 * The metamodel's non-containment transitive closure (route) matrix.
	 */
	private BitMatrix nonContainmentClosureMatrix;

	/**
	 * References considered for non-containment.
	 */
	private EReference[] nonContainmentReferences;

	/**
	 * Only needed for debug.
	 */
	// private EClass[] nonContainmentClasses;

	/**
	 * Current eligibility setting used while constructing the containment/non-containment matrices.
	 */
	private MDLEligibility eligibility;

	/**
	 * Maps {@code EClass}es to their indices inside the containment matrices.
	 */
	private Map<EClass, Integer> containmentClassMap;

	/**
	 * Maps {@code EClass}es to their indices inside the non-containment matrices.
	 */
	private Map<EClass, Integer> nonContainmentClassMap;

	/**
	 * Construct a MDM from a list of {@code EClass}es. Will consider the involved {@code EReference}s as well.
	 * 
	 * @param classes
	 *        the list of {@code EClass}es.
	 */
	public ModelDependencyMatrix(List<EClass> classes)
	{
		generateModelGraph(classes);
	}

	/**
	 * Indicates if the {@code source} may refer, directly or indirectly, the {@code dest}, considering the route matrix
	 * {@code closureMatrix}.
	 * 
	 * @param source
	 *        the source {@code EClass}
	 * @param dest
	 *        the destination {@code EClass}
	 * @param classMap
	 *        the index of each involved {@code EClass} inside the square {@code closureMatrix}
	 * @param closureMatrix
	 *        the route matrix of the {@code EClass}es/{@code EReference}s graph
	 * @return true if {@code source} may refer {@code dest}, false otherwise
	 */
	private static boolean reachable(EClass source, EClass dest, Map<EClass, Integer> classMap, BitMatrix closureMatrix)
	{
		Integer parentIndex = classMap.get(source);
		Integer childIndex = classMap.get(dest);

		if ((null == parentIndex) || (null == childIndex))
		{
			return false;
		}

		return closureMatrix.get(childIndex, parentIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.internal.mdl.IModelDependencyMatrix#mayRefer(org.eclipse.emf.ecore.EClass,
	 * org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public boolean mayRefer(EClass source, EClass dest)
	{
		return reachable(source, dest, nonContainmentClassMap, nonContainmentClosureMatrix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.internal.mdl.IModelDependencyMatrix#mayContain(org.eclipse.emf.ecore.EClass,
	 * org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public boolean mayContain(EClass parent, EClass child)
	{
		return reachable(parent, child, containmentClassMap, containmentClosureMatrix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.internal.mdl.IModelDependencyMatrix#getReferences(org.eclipse.emf.ecore.EClass,
	 * org.eclipse.emf.ecore.EClass, eu.cessar.ct.core.mms.internal.mdl.MDLEligibility)
	 */
	@Override
	public List<EReference> getReferences(EClass fromClass, EClass toClass, MDLEligibility eligib)
	{
		switch (eligib)
		{
			case CONTAINMENT:
				return containmentReferences(fromClass, toClass);
			case NON_CONTAINMENT:
				return nonContainmentReferences(fromClass, toClass);
			default:
				// Unreachable
				return null;
		}
	}

	/**
	 * Get the containment references of {@code fromClass} to follow in a reachability search from {@code fromClass} to
	 * {@code toClass}.
	 * 
	 * @param fromClass
	 *        the starting {@code EClass}
	 * @param toClass
	 *        the destination {@code EClass}
	 * @return the non-null, possibly empty, list of references
	 */
	private List<EReference> containmentReferences(EClass fromClass, EClass toClass)
	{
		return references(fromClass, toClass, containmentClassMap, containmentReferences, containmentClosureMatrix,
			containmentAdjacencyMatrix);
	}

	/**
	 * Get the non-containment references of {@code fromClass} to follow in a reachability search from {@code fromClass}
	 * to {@code toClass}.
	 * 
	 * @param fromClass
	 *        the starting {@code EClass}
	 * @param toClass
	 *        the destination {@code EClass}
	 * @return the non-null, possibly empty, list of references
	 */
	private List<EReference> nonContainmentReferences(EClass fromClass, EClass toClass)
	{
		return references(fromClass, toClass, nonContainmentClassMap, nonContainmentReferences,
			nonContainmentClosureMatrix, nonContainmentAdjacencyMatrix);
	}

	/**
	 * Get the references of {@code fromClass} to follow in a reachability search from {@code fromClass} to
	 * {@code toClass}, based on the associated adjacency and route matrices.
	 * 
	 * @param fromClass
	 *        the starting {@code EClass}
	 * @param toClass
	 *        the destination {@code EClass}
	 * @param classMap
	 *        the index of each {@code EClass} inside the square matrices {@code closureMatrix} and
	 *        {@code adjacencyMatrix}
	 * @param refs
	 *        the references array
	 * @param closureMatrix
	 *        the closure matrix (route matrix) for the EClass/EReference graph
	 * @param adjacencyMatrix
	 *        the adjacency matrix for the EClass/EReference graph
	 * @return the non-null, possibly empty, list of references
	 */
	private static List<EReference> references(EClass fromClass, EClass toClass, Map<EClass, Integer> classMap,
		EReference[] refs, BitMatrix closureMatrix, BitMatrix adjacencyMatrix)
	{
		int fromClassIndex = classMap.get(fromClass);
		int toClassIndex = classMap.get(toClass);
		List<EReference> referenceList = new ArrayList<EReference>();
		int classCount = classMap.size();

		if (!closureMatrix.get(toClassIndex, fromClassIndex))
		{
			return referenceList;
		}
		else
		{
			BitSet adjacencyRow = adjacencyMatrix.getRow(fromClassIndex);

			// Exploit the BitSet.nextSetBit(int) API for faster access
			for (int iRef = adjacencyRow.nextSetBit(classCount); iRef >= 0; iRef = adjacencyRow.nextSetBit(iRef + 1))
			{
				if ((closureMatrix.get(iRef, fromClassIndex)) && (closureMatrix.get(toClassIndex, iRef)))
				{
					referenceList.add(refs[iRef - classCount]);
				}
			}
		}
		return referenceList;
	}

	/**
	 * Get all subclasses of the {@code refType} {@code EClass} members of {@code eClassSet}.
	 * 
	 * @param refType
	 *        the {@code EClass}
	 * @param eClassSet
	 *        the EClass set to consider candidates from
	 * @return non-null, possibly empty, collection of subclasses
	 */
	protected static Collection<EClass> getEAllSubClasses(EClass refType, Set<EClass> eClassSet)
	{
		Set<EClass> result = new HashSet<EClass>();
		for (EClass eClass: eClassSet)
		{
			if (refType.isSuperTypeOf(eClass))
			{
				result.add(eClass);
			}
		}

		return result;

	}

	/**
	 * Get all EClasses
	 * 
	 * @return a collection containing all {@link EClass}
	 */
	public Collection<EClass> getEAllClasses(MDLEligibility eligib)
	{
		Set<EClass> eClassSet = null;
		if (eligib == MDLEligibility.CONTAINMENT)
		{
			eClassSet = containmentClassMap.keySet();
		}
		else
		{

			eClassSet = nonContainmentClassMap.keySet();
		}
		return eClassSet;
	}

	// Only for debug.
	// /**
	// * Get the "name" of an {@code EObject}.
	// *
	// * @param eParent
	// * the {@code EObject}
	// * @return the possibly null name of {@code EObject}
	// */
	// private static String getNodeName(EObject eParent)
	// {
	//		EStructuralFeature fname = eParent.eClass().getEStructuralFeature("name"); //$NON-NLS-1$
	//
	// if ((null != fname) && (eParent.eIsSet(fname)))
	// {
	// String name = eParent.eGet(fname).toString();
	// char first = name.charAt(0);
	// if (Character.isLetter(first) && !Character.isLowerCase(first))
	// {
	// return name;
	// }
	// }
	// return null;
	// }

	/**
	 * Indicates whether {@code ref} is a containment reference.
	 * 
	 * @param ref
	 *        the reference
	 * @return true if {@code ref} is a containment reference, false otherwise
	 */
	private static boolean eligibleForContainment(EReference ref)
	{
		return ref.isContainment();
	}

	/**
	 * Indicates whether {@code ref} is considered for non-containment reachability.
	 * 
	 * @param ref
	 *        the reference
	 * @return true if {@code ref} is considered for non-containment reachability, false otherwise
	 */
	private static boolean eligibleForNonContainment(EReference ref)
	{
		return !ref.isContainment() && !ref.isContainer();
	}

	/**
	 * Indicates whether {@code ref} should be considered for the current matrix.
	 * 
	 * @param ref
	 *        the reference
	 * @return true if {@code ref} should be considered for the current matrix, false otherwise
	 */
	private boolean eligible(EReference ref)
	{
		switch (eligibility)
		{
			case CONTAINMENT:
				return eligibleForContainment(ref);
			case NON_CONTAINMENT:
				return eligibleForNonContainment(ref);
			default:
				return true;
		}
	}

	/**
	 * The references of {@code clz} to consider for search.
	 * 
	 * @param clz
	 *        the {@code EClass}
	 * @return the non-null, possibly empty, list of {@code EReference}s
	 */
	private static List<EReference> classifierReferences(EClass clz)
	{
		return clz.getEReferences();
	}

	/**
	 * The super-classes of {@code refType} members of {@code eClassSet}
	 * 
	 * @param refType
	 *        the {@code EClass}
	 * @param eClassSet
	 *        the {@code EClass} set to consider
	 * @return the non-null, possibly empty, collection of {@code EClass}es
	 */
	private static Collection<EClass> getAllSuperClasses(EClass refType, Set<EClass> eClassSet)
	{
		return Collections2.filter(refType.getEAllSuperTypes(), Predicates.in(eClassSet));
	}

	/**
	 * The Warshall algorithm for in-place transitive closure.
	 * 
	 * @param m
	 *        the adjacency matrix, that is altered to a route matrix by the method
	 */
	private static void warshall(BitMatrix m)
	{
		int n = m.getRowCount(); // assumed square matrix

		for (int k = 0; k < n; k++)
		{
			for (int i = 0; i < n; i++)
			{
				if (m.get(k, i))
				{
					for (int j = 0; j < n; j++)
					{
						if (!m.get(j, i))
						{
							m.set(j, i, m.get(j, k));
						}
					}
				}
			}
		}
	}

	/**
	 * Construct the sequence of vertices of the {@code EClass}es/{@code EReference}s graph starting from
	 * {@code current}.
	 * 
	 * @param current
	 *        the current {@code EClass} to consider
	 * @param classMap
	 *        the index of each {@code EClass} in the {@code EClass}es sequence
	 * @param referenceMap
	 *        the index of each {@code EReference} in the {@code EReference}s sequence
	 * @param eClassSet
	 *        the set of {@code EClass}es to consider
	 */
	private void computeClassVertices(EClass current, Map<EClass, Integer> classMap,
		Map<EReference, Integer> referenceMap, Set<EClass> eClassSet)
	{
		if (classMap.containsKey(current))
		{
			return;
		}

		int classSize = classMap.size();
		classMap.put(current, classSize);

		Iterator<EReference> it = classifierReferences(current).iterator();

		while (it.hasNext())
		{
			EReference ref = it.next();

			if (!eligible(ref))
			{
				continue;
			}

			if (!referenceMap.containsKey(ref))
			{
				referenceMap.put(ref, referenceMap.size());
			}

			EClass refType = ref.getEReferenceType();

			for (EClass subClass: getEAllSubClasses(refType, eClassSet))
			{
				computeClassVertices(subClass, classMap, referenceMap, eClassSet);
			}

			for (EClass superClass: getAllSuperClasses(refType, eClassSet))
			{
				computeClassVertices(superClass, classMap, referenceMap, eClassSet);
			}
		}
	}

	// Only for debug
	// private static void exportAdjacencyMatrix(BitMatrix m, EClass[] classes, EReference[] references, String
	// fileName)
	// {
	// int classCount = classes.length;
	// int referenceCount = references.length;
	// int n = classCount + referenceCount;
	//
	//
	// File graphFile = new File(fileName);
	//
	// FileOutputStream graphout = null;
	// try
	// {
	// graphout = new FileOutputStream(graphFile, false);
	// PrintStream pout = new PrintStream(graphout);
	//
	// int hdr;
	// for (hdr = 0; hdr < classCount; hdr++)
	// {
	// pout.print("," + getNodeName(classes[hdr]));
	// }
	// for (; hdr < n; hdr++)
	// {
	// pout.print("," + references[hdr - classCount].getName());
	// }
	//
	// for (int i = 0; i < n; i++)
	// {
	// pout.println();
	// if (i < classCount)
	// {
	// pout.print(getNodeName(classes[i]) + ",");
	// }
	// else
	// {
	// pout.print(references[i - classCount].getName() + ",");
	// }
	//
	// for (int j = 0; j < n; j++)
	// {
	// pout.print((m.get(j, i) ? 1 : 0) + ",");
	// }
	// }
	// pout.close();
	// }
	// catch (FileNotFoundException e)
	// {
	// e.printStackTrace();
	// }
	// finally
	// {
	// if (null != graphout)
	// {
	// try
	// {
	// graphout.close();
	// }
	// catch (IOException e)
	// {
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	/**
	 * Compute the adjacency matrix of the {@code EClass}es/{@code EReference}s graph
	 * 
	 * @param classes
	 *        the {@code EClass}es to consider
	 * @param refs
	 *        the {@code EReference}s to consider
	 * @param classMap
	 *        the index of each {@code EClass} inside the square matrix
	 * @param eClassSet
	 *        the set of {@code EClass}es to consider
	 * @return the adjacency (bit) matrix of the {@code EClass}es/{@code EReference}s graph
	 */
	private static BitMatrix adjacencyMatrix(EClass[] classes, EReference[] refs, Map<EClass, Integer> classMap,
		Set<EClass> eClassSet)
	{
		int classCount = classes.length;
		int referenceCount = refs.length;
		int n = classCount + referenceCount;

		BitMatrix resultAdjacencyMatrix = new BitMatrix(n, n);

		for (int i = 0; i < classCount; i++)
		{
			for (int j = 0; j < referenceCount; j++)
			{
				EClass clz = classes[i];
				EReference ref = refs[j];
				EClass refType = ref.getEReferenceType();

				if (clz.getEAllReferences().contains(ref))
				{
					resultAdjacencyMatrix.set(classCount + j, i);

					for (EClass subClass: getEAllSubClasses(refType, eClassSet))
					{
						int subClassIndex = classMap.get(subClass);
						resultAdjacencyMatrix.set(subClassIndex, classCount + j);
					}

					for (EClass superClass: getAllSuperClasses(refType, eClassSet))
					{
						int superClassIndex = classMap.get(superClass);
						resultAdjacencyMatrix.set(superClassIndex, classCount + j);
					}
				}

				if (refType.isSuperTypeOf(clz))
				{
					resultAdjacencyMatrix.set(i, classCount + j);
				}
			}
		}

		return resultAdjacencyMatrix;
	}

	/**
	 * Get an array of T based on a map from T elements to their indices in the array.
	 * 
	 * @param map
	 *        the map from T elements to their indices
	 * @return the constructed array
	 */
	private static <T> T[] arrayFromIndexedMap(Class<T> clz, Map<T, Integer> map)
	{
		int n = map.size();
		T[] result = (T[]) Array.newInstance(clz, n);

		Iterator<Entry<T, Integer>> it = map.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<T, Integer> pairs = it.next();
			result[pairs.getValue()] = pairs.getKey();
		}

		return result;
	}

	/**
	 * Generate the containment matrices for the {@code EClass}es/{@code EReference}es graph (adjacency and route).
	 * 
	 * @param eClazzez
	 *        the {@code EClass} list
	 * @param eClassSet
	 *        the {@code EClass} set to consider
	 */
	private void genContainment(List<EClass> eClazzez, Set<EClass> eClassSet)
	{
		eligibility = MDLEligibility.CONTAINMENT;
		genMatrix(eClazzez, eClassSet);

		// // Only for debug
		// exportAdjacencyMatrix(containmentAdjacencyMatrix, containmentClasses, containmentReferences,
		// "d:/MDLDEBUG/containmentAdjacencyMatrix.csv");
		// exportAdjacencyMatrix(containmentClosureMatrix, containmentClasses, containmentReferences,
		// "d:/MDLDEBUG/containmentClosureMatrix.csv");
	}

	/**
	 * Generate the non-containment matrices for the {@code EClass}es/{@code EReference}es graph (adjacency and
	 * closure).
	 * 
	 * @param eClazzez
	 *        the {@code EClass} list
	 * @param eClassSet
	 *        the {@code EClass} set to consider
	 */
	private void genNonContainment(List<EClass> eClazzez, Set<EClass> eClassSet)
	{
		eligibility = MDLEligibility.NON_CONTAINMENT;
		genMatrix(eClazzez, eClassSet);

		// // Only for debug
		// exportAdjacencyMatrix(nonContainmentAdjacencyMatrix, nonContainmentClasses, nonContainmentReferences,
		// "d:/MDLDEBUG/nonContainmentAdjacencyMatrix.csv");
		// exportAdjacencyMatrix(nonContainmentClosureMatrix, nonContainmentClasses, nonContainmentReferences,
		// "d:/MDLDEBUG/nonContainmentClosureMatrix.csv");
	}

	/**
	 * Generate matrices for the {@code EClass}es/{@code EReference}es graph (adjacency and closure) for a type of
	 * EReferences controlled by {@code eligibility}.
	 * 
	 * @param eClazzez
	 *        the {@code EClass} list
	 * @param eClassSet
	 *        the {@code EClass} set to consider
	 */
	private void genMatrix(List<EClass> eClazzez, Set<EClass> eClassSet)
	{
		Map<EClass, Integer> classMap = new HashMap<EClass, Integer>();
		Map<EReference, Integer> referenceMap = new HashMap<EReference, Integer>();

		for (EClass clz: eClazzez)
		{
			computeClassVertices(clz, classMap, referenceMap, eClassSet);
		}

		EClass[] classes = arrayFromIndexedMap(EClass.class, classMap);
		EReference[] references = arrayFromIndexedMap(EReference.class, referenceMap);

		BitMatrix adjacencyMatrix = adjacencyMatrix(classes, references, classMap, eClassSet);

		BitMatrix closureMatrix = adjacencyMatrix.clone();

		// Compute the transitive closure.
		warshall(closureMatrix);

		switch (eligibility)
		{
			case CONTAINMENT:
				containmentAdjacencyMatrix = adjacencyMatrix;
				containmentClosureMatrix = closureMatrix;
				containmentClassMap = classMap;
				// containmentClasses = classes;

				containmentReferences = references;
				break;
			case NON_CONTAINMENT:
				nonContainmentAdjacencyMatrix = adjacencyMatrix;
				nonContainmentClosureMatrix = closureMatrix;
				nonContainmentClassMap = classMap;
				// nonContainmentClasses = classes;

				nonContainmentReferences = references;
				break;
			default:
				// Unreachable
				break;
		}
	}

	/**
	 * Generate the model dependency graph, represented by adjacency and closure matrices for both containment and
	 * non-containment {@code EReference}s.
	 * 
	 * @param classes
	 *        the {@code EClass} list
	 */
	private void generateModelGraph(List<EClass> classes)
	{
		Set<EClass> eClassSet = new HashSet<EClass>(classes);

		genContainment(classes, eClassSet);
		genNonContainment(classes, eClassSet);

		// CessarPluginActivator activator = CessarPluginActivator.getDefault();
		//
		// int contN = containmentAdjacencyMatrix.getRowCount();
		// int nonContN = nonContainmentAdjacencyMatrix.getRowCount();

		//	activator.logInfo("Matrix info: " + contN + "x" + contN + ", " + nonContN + "x" + nonContN); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
