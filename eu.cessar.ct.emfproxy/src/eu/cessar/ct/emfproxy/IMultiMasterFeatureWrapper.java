/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 2:16:31 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

/**
 * @author uidl6458
 * 
 */
public interface IMultiMasterFeatureWrapper<T> extends IMasterFeatureWrapper<T>
{

	/**
	 * @param index
	 * @return
	 */
	public T get(int index);

	/**
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * @return
	 */
	public int size();

	/**
	 * @return
	 */
	public Object[] toArray();

	/**
	 * @return
	 */
	public <S> S[] toArray(S[] array);

	/**
	 * @param slaveValue
	 * @return
	 */
	public int indexOf(Object slaveValue);

	/**
	 * @param slaveValue
	 * @return
	 */
	public int lastIndexOf(Object slaveValue);

	/**
	 * 
	 */
	public void clear();

	/**
	 * @param index
	 * @param value
	 */
	public void set(int index, Object value);

	/**
	 * @param index
	 * @param value
	 */
	public void add(int index, Object value);

	/**
	 * @param index
	 */
	public void remove(int index);

	/**
	 * @param targetIndex
	 * @param sourceIndex
	 * @return
	 */
	public Object move(int targetIndex, int sourceIndex);

}
