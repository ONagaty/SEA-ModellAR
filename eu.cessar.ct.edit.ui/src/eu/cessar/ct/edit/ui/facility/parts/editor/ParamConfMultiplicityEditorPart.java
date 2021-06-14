package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.math.BigInteger;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.events.IEditorListener;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleBigIntegerEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.internal.facility.editors.ParamConfMultiplicityEditor;
import eu.cessar.ct.edit.ui.utils.EditUtils;
import gautosar.gecucparameterdef.GParamConfMultiplicity;

/**
 * Editor part for editing of lower/upper multiplicity for MM 2.x/3.x
 * 
 */
public class ParamConfMultiplicityEditorPart extends AbstractEditorPart implements IEditorPart
{
	// editors
	private SingleBigIntegerEditor lowerMultiplicityEditor;
	private SingleBigIntegerEditor upperMultiplicityEditor;

	private Text lowerMultiplicityControl;
	private Text upperMultiplicityControl;

	// listeners
	private IEditorListener<BigInteger> lowerMultiplicityEditorListener;
	private IEditorListener<BigInteger> upperMultiplicityEditorListener;

	private static final String LOWER_UNSET = "0"; //$NON-NLS-1$
	private static final String UPPER_UNSET = "*"; //$NON-NLS-1$

	private enum ValidityState
	{
		VALID, INVALID, UNKNOWN;

		private boolean getBoolean()
		{
			boolean valid = false;

			if (this == VALID)
			{
				valid = true;
			}
			return valid;
		}
	}

	/**
	 * @author uidu3379
	 * 
	 */
	private final class ValidationRunnable implements Runnable
	{
		private final BigInteger newData;
		private final boolean isStarValueSet;
		private final EStructuralFeature feature;

		/**
		 * @param newData
		 * @param isStarValueSet
		 * @param feature
		 */
		private ValidationRunnable(BigInteger newData, boolean isStarValueSet, EStructuralFeature feature)
		{
			this.newData = newData;
			this.isStarValueSet = isStarValueSet;
			this.feature = feature;
		}

		public void run()
		{

			GParamConfMultiplicity inputObject = (GParamConfMultiplicity) getInputObject();
			if (newData != null)
			{
				if (feature.equals(getEditor().getLowerMultiplicityFeature()))
				{
					inputObject.gSetLowerMultiplicityAsString(newData.toString());
				}
				else
				{
					inputObject.gSetUpperMultiplicityAsString(newData.toString());
				}
			}
			else
			{
				if (isStarValueSet)
				{
					inputObject.gSetUpperMultiplicityAsString("*"); //$NON-NLS-1$

				}
				else
				{
					inputObject.eUnset(feature);

				}
			}

		}
	}

	private class SingleBigIntegerEditorForUpperM extends SingleBigIntegerEditor
	{

		private final Pattern starPattern = Pattern.compile("\\*"); //$NON-NLS-1$
		private static final String STAR_VALUE = "*"; //$NON-NLS-1$
		private boolean isStarValueSet;

		/**
		 * @param acceptNull
		 */
		public SingleBigIntegerEditorForUpperM(boolean acceptNull)
		{
			super(acceptNull);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see eu.cessar.ct.core.platform.ui.widgets.SingleBigIntegerEditor# isValidPattern(java.lang.String)
		 */
		@Override
		protected boolean isValidPattern(String value)
		{
			return super.isValidPattern(value) || starPattern.matcher(value).matches();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeeu.cessar.ct.core.platform.ui.widgets.SingleBigIntegerEditor# convertFromString(java.lang.String)
		 */
		@Override
		protected BigInteger convertFromString(String value)
		{

			if (value.equals(STAR_VALUE))
			{
				isStarValueSet = true;
				return null;
			}
			return super.convertFromString(value);
		}

		public boolean isStarValueSet()
		{
			return isStarValueSet;
		}

	}

	/**
	 * @param editor
	 */
	public ParamConfMultiplicityEditorPart(IModelFragmentEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent)
	{
		// add controls
		Composite composite = getFormToolkit().createComposite(parent);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		lowerMultiplicityEditor = new SingleBigIntegerEditor(true);
		// updateReadOnlyState(lowerMultiplicityEditor);
		lowerMultiplicityControl = (Text) lowerMultiplicityEditor.createEditor(composite);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.widthHint = 30;
		lowerMultiplicityControl.setLayoutData(gridData);
		lowerMultiplicityEditor.setEventListener(getEditor().getEventListener());
		lowerMultiplicityControl.addVerifyListener(EditUtils.createMultiplicityVerifyListener());

		CLabel label = getFormToolkit().createCLabel(composite, "-"); //$NON-NLS-1$
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		upperMultiplicityEditor = new SingleBigIntegerEditorForUpperM(true);
		// updateReadOnlyState(upperMultiplicityEditor);
		upperMultiplicityControl = (Text) upperMultiplicityEditor.createEditor(composite);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.widthHint = 30;
		upperMultiplicityControl.setLayoutData(gridData);
		upperMultiplicityEditor.setEventListener(getEditor().getEventListener());
		lowerMultiplicityControl.addVerifyListener(EditUtils.createMultiplicityVerifyListener());

		// add listeners
		lowerMultiplicityEditorListener = new IEditorListener<BigInteger>()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see eu.cessar.ct.core.platform.ui.widgets.IEditorListener #acceptData(java.lang.Object,
			 * java.lang.Object)
			 */
			public boolean acceptData(final BigInteger oldData, final BigInteger newData)
			{
				return doAcceptData(oldData, newData, getEditor().getLowerMultiplicityFeature(), false);
			}
		};
		lowerMultiplicityEditor.addEditorListener(lowerMultiplicityEditorListener);

		upperMultiplicityEditorListener = new IEditorListener<BigInteger>()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see eu.cessar.ct.core.platform.ui.widgets.IEditorListener #acceptData(java.lang.Object,
			 * java.lang.Object)
			 */
			public boolean acceptData(final BigInteger oldData, final BigInteger newData)
			{
				return doAcceptData(oldData, newData, getEditor().getUpperMultiplicityFeature(),
					((SingleBigIntegerEditorForUpperM) upperMultiplicityEditor).isStarValueSet);
			}
		};
		upperMultiplicityEditor.addEditorListener(upperMultiplicityEditorListener);

		return composite;
	}

	/**
	 * 
	 * @param oldData
	 * @param newData
	 * @param feature
	 * @param isStarValueSet
	 * @return
	 */
	protected boolean doAcceptData(BigInteger oldData, final BigInteger newData, final EStructuralFeature feature,
		final boolean isStarValueSet)
	{
		return isValidData(oldData, newData, feature)
			&& performChangeWithChecks(new ValidationRunnable(newData, isStarValueSet, feature), "Updating data.."); //$NON-NLS-1$
	}

	/**
	 * @param oldData
	 * @param newData
	 * @param feature
	 * @return
	 */
	private boolean isValidData(BigInteger oldData, BigInteger newData, EStructuralFeature feature)
	{
		if (newData == null)
		{
			return true;
		}

		ValidityState valid = ValidityState.UNKNOWN;

		if (feature.equals(getEditor().getLowerMultiplicityFeature()))
		{
			// lowerMultiplicity must be <= upperMultiplicity
			valid = isDataValidInEditor(upperMultiplicityEditor, newData, oldData, false);
		}

		if (valid != ValidityState.UNKNOWN)
		{
			return valid.getBoolean();
		}

		// upperMultiplicity must be <= upperMultiplicity
		valid = isDataValidInEditor(lowerMultiplicityEditor, newData, oldData, true);

		return valid.getBoolean();
	}

	private ValidityState isDataValidInEditor(SingleBigIntegerEditor editor, BigInteger newData, BigInteger oldData,
		boolean isLowerMultiplicityEditor)
	{
		ValidityState valid = ValidityState.UNKNOWN;

		if (editor != null)
		{
			BigInteger bound = editor.getInputData();
			// the lowerMultiplicity may be unset
			if (bound == null)
			{
				valid = ValidityState.VALID;
			}

			else if (isInBounds(newData, bound, isLowerMultiplicityEditor))
			{
				valid = ValidityState.VALID;
			}
			else
			{
				if (isLowerMultiplicityEditor && upperMultiplicityEditor != null)
				{
					upperMultiplicityEditor.setInputData(oldData);
				}
				else if (!isLowerMultiplicityEditor && lowerMultiplicityEditor != null)
				{
					lowerMultiplicityEditor.setInputData(oldData);
				}
				valid = ValidityState.INVALID;
			}
		}

		return valid;
	}

	private boolean isInBounds(BigInteger data, BigInteger bound, boolean isLowerBound)
	{
		boolean isInBounds = false;

		if (isLowerBound)
		{
			isInBounds = data.compareTo(bound) >= 0;
		}
		else if (!isLowerBound)
		{
			isInBounds = data.compareTo(bound) <= 0;
		}

		return isInBounds;
	}

	/**
	 * 
	 * @param editor
	 * @param listener
	 */
	private void removeEditorListener(SingleBigIntegerEditor editor, IEditorListener<BigInteger> listener)
	{
		if (editor != null && listener != null)
		{
			editor.removeEditorListener(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	public void dispose()
	{
		removeEditorListener(lowerMultiplicityEditor, lowerMultiplicityEditorListener);
		removeEditorListener(upperMultiplicityEditor, upperMultiplicityEditorListener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#refresh()
	 */
	public void refresh()
	{
		updateReadOnlyState(lowerMultiplicityEditor);
		updateReadOnlyState(upperMultiplicityEditor);
		setInputDataToMultiplicityEditor(lowerMultiplicityEditor, getEditor().getLowerMultiplicityFeature());
		setInputDataToMultiplicityEditor(upperMultiplicityEditor, getEditor().getUpperMultiplicityFeature());
	}

	/**
	 * 
	 * @param editor
	 */
	private void updateReadOnlyState(IDatatypeEditor<?> editor)
	{
		if (editor != null)
		{
			editor.setReadOnly(!getEditor().getEditableStatus().isOK());
		}
	}

	/**
	 * 
	 * @param editor
	 * @param feature
	 */
	private void setInputDataToMultiplicityEditor(SingleBigIntegerEditor editor, EStructuralFeature feature)
	{
		if (editor != null)
		{
			GParamConfMultiplicity inputObject = (GParamConfMultiplicity) getInputObject();
			if (inputObject.eIsSet(feature))
			{
				if (inputObject.gGetLowerMultiplicityAsString().length() == 0)
				{
					editor.setInputData(null);
					return;
				}
				else
				{
					setValidInputData(editor, feature, inputObject);
				}
			}
			else
			{
				editor.setInputData(null);
			}
		}
	}

	private void setValidInputData(SingleBigIntegerEditor editor, EStructuralFeature feature,
		GParamConfMultiplicity inputObject)
	{
		if (feature.equals(getEditor().getLowerMultiplicityFeature()))
		{
			String lower = inputObject.gGetLowerMultiplicityAsString();
			BigInteger uiData = new BigInteger(lower);
			editor.setInputData(uiData);
		}
		else
		{
			String upper = inputObject.gGetUpperMultiplicityAsString();
			if ("*".equals(upper)) //$NON-NLS-1$
			{
				if (upperMultiplicityControl != null && !upperMultiplicityControl.isDisposed())
				{
					upperMultiplicityControl.setText("*"); //$NON-NLS-1$
				}
			}
			else
			{
				BigInteger uiData = new BigInteger(upper);
				editor.setInputData(uiData);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart#getEditor()
	 */
	@Override
	public ParamConfMultiplicityEditor getEditor()
	{
		return (ParamConfMultiplicityEditor) super.getEditor();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_MULTIPLICITY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		String text = ""; //$NON-NLS-1$
		String lower, upper;
		GParamConfMultiplicity inputObject = (GParamConfMultiplicity) getInputObject();

		if (inputObject == null)
		{
			return ""; //$NON-NLS-1$
		}

		lower = inputObject.gGetLowerMultiplicityAsString();
		upper = inputObject.gGetUpperMultiplicityAsString();
		if (lower == null || "".equals(lower)) //$NON-NLS-1$
		{
			lower = LOWER_UNSET;
		}

		if (upper == null || "".equals(upper)) //$NON-NLS-1$
		{
			upper = UPPER_UNSET;
		}

		text += lower + ".." + upper; //$NON-NLS-1$
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		if (lowerMultiplicityEditor != null)
		{
			lowerMultiplicityEditor.setEnabled(enabled);
		}

		if (upperMultiplicityEditor != null)
		{
			upperMultiplicityEditor.setEnabled(enabled);
		}

	}
}
