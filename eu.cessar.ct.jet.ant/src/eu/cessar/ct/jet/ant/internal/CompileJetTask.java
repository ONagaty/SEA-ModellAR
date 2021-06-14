/**
 * <copyright>
 * 
 * Copyright (c) Continental AG and others.<br/>
 * http://www.continental-corporation.com All rights reserved.
 * 
 * File created by uidu8153<br/>
 * May 21, 2015 11:41:26 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.jet.ant.internal;


/**
 * Ant task for compiling a jet.
 * 
 * @author uidu8153
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Wed Jun  3 12:41:43 2015 %
 * 
 *         %version: 2 %
 */
public class CompileJetTask extends AbstractCompileJetTask
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#getTaskCompatType()
	 */
	@Override
	protected ETaskType getTaskCompatType()
	{
		// TODO Auto-generated method stub
		return ETaskType.UNIVERSAL_TASK;
	}

}
