package eu.cessar.ct.core.mms.ecuc.commands.parameters;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.commands.IEcucCessarCommandParameter;
import eu.cessar.ct.core.mms.ecuc.commands.NewEcucContainerCommand;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * A generic command parameter implementation that must be passed to the command that create a new ECUC container.
 */
public class NewEcucContainerCommandParameter extends CommandParameter implements IEcucCessarCommandParameter
{
	/** the definition of the container about to be created */
	private GContainerDef containerDefinition;

	/** the Short Name of the container about to be created */
	private String shortName = null;

	/**
	 * 
	 */
	private boolean deriveNameFromSuggestion;

	/**
	 * if the container that is about to be created is one of the choices of a Choice Container, this member hold the
	 * choice container definition
	 */
	private GChoiceContainerDef choiceOwnerDef;

	/**
	 * Constructor for container command parameter
	 * 
	 * @param owner
	 *        the owner object for the container about to be created
	 * @param definition
	 *        the definition of the container about to be created
	 * @param newShortName
	 *        the Short Name of the container about to be created, or only the suggestion if the
	 *        <code>deriveNameFromSuggestion</code> is <code>true</code>
	 * @param deriveNameFromSuggestion
	 *        whether to use the suggested <code>newShortName</code> as it is, or to compute it as uniquely derived from
	 *        the suggestion, but with an index
	 */
	public NewEcucContainerCommandParameter(GIdentifiable owner, GContainerDef definition, String newShortName,
		boolean deriveNameFromSuggestion)
	{
		super(owner);
		Assert.isNotNull(owner);
		Assert.isNotNull(definition);

		containerDefinition = definition;
		shortName = newShortName;
		this.deriveNameFromSuggestion = deriveNameFromSuggestion;
	}

	/**
	 * Constructor for container command parameter
	 * 
	 * @param owner
	 *        the owner object for the container about to be created
	 * @param definition
	 *        the definition of the container about to be created
	 * @param newShortName
	 *        the Short Name of the container about to be created
	 */
	public NewEcucContainerCommandParameter(GIdentifiable owner, GContainerDef definition, String newShortName)
	{
		super(owner);
		Assert.isNotNull(owner);
		Assert.isNotNull(definition);

		containerDefinition = definition;
		shortName = newShortName;
	}

	/**
	 * Another constructor for the container command parameter, where the Short Name is computed using
	 * {@link EcucMetaModelUtils#computeUniqueChildShortName(EObject, String)}
	 * 
	 * @param owner
	 *        the owner object for the container about to be created
	 * @param definition
	 *        the definition of the container about to be created
	 */
	public NewEcucContainerCommandParameter(GIdentifiable owner, GContainerDef definition)
	{
		this(owner, definition, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucCessarCommandParameter#getDefinition()
	 */
	public GContainerDef getDefinition()
	{
		return containerDefinition;
	}

	/**
	 * Return the shortName stored inside the command parameters. If there is no such shortname stored a new one will be
	 * generated
	 * 
	 * @return
	 */
	public String getShortName()
	{
		if (shortName == null)
		{
			return MetaModelUtils.computeUniqueChildShortName((EObject) owner, containerDefinition.gGetShortName());
		}
		else if (shortName != null && deriveNameFromSuggestion)
		{
			return MetaModelUtils.computeUniqueChildShortName((EObject) owner, shortName);
		}

		else
		{
			return shortName;
		}
	}

	/**
	 * The method that gets an ECU container creation command, based on this command parameter.
	 * 
	 * @return An EMF {@link Command} instance that, when executed, create a new ECUC container instance.
	 */
	public Command createCommand(AdapterFactory adapterFactory)
	{
		Object image = null;
		if (adapterFactory != null)
		{
			Adapter adapter = adapterFactory.adapt(containerDefinition, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider)
			{
				image = ((IItemLabelProvider) adapter).getImage(containerDefinition);
			}
		}
		return new NewEcucContainerCommand(this, image);
	}

	// methods that deal with choice container definition, if is the case

	/**
	 * @param choiceDef
	 */
	public void setChoiceContainerDefinition(GChoiceContainerDef choiceDef)
	{
		choiceOwnerDef = choiceDef;
	}

	/**
	 * @return
	 */
	public GChoiceContainerDef getChoiceContainerDefinition()
	{
		return choiceOwnerDef;
	}

	/**
	 * @return
	 */
	public boolean isChoice()
	{
		return choiceOwnerDef != null;
	}
}
