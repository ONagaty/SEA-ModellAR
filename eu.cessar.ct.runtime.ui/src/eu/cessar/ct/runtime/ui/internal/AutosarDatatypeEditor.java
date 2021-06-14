package eu.cessar.ct.runtime.ui.internal;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import eu.cessar.ct.cid.IArtifactParameter;
import eu.cessar.ct.cid.extension.IDatatype;
import eu.cessar.ct.cid.ui.IDatatypeEditor;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.platform.ui.util.SelectionUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.runtime.internal.AutosarDatatype;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Editor that creates a tree viewer control listing only elements of a given EClass
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Fri Jun 19 18:17:25 2015 %
 *
 *         %version: 3 %
 */
@Requirement(
	reqID = "54756")
public class AutosarDatatypeEditor implements IDatatypeEditor<GIdentifiable>
{
	private IArtifactParameter<GIdentifiable> plugetParameter;
	private IDatatype<GIdentifiable> type;
	private TreeViewer treeViewer;
	// The listener to notify of each selection made in this control
	private Listener listener;

	@Override
	public Control createControlForParameter(Composite parent, IArtifactParameter<?> parameter)
	{
		plugetParameter = (IArtifactParameter<GIdentifiable>) parameter;

		// get the active project
		IProject activeProject = SelectionUtils.getActiveProject(false);
		// before setting the tree input, ask for model to be loaded
		PlatformUtils.waitForModelLoading(activeProject, new NullProgressMonitor());

		// create the table viewer by displaying only the elements of type eclass, belonging to the active project

		Composite treeViewerComposite = new Composite(parent, SWT.NONE);
		treeViewerComposite.setLayout(new GridLayout(2, false));
		treeViewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		// create a treeViewer
		treeViewer = new TreeViewer(treeViewerComposite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		// Create and format the Tree
		Tree tree = treeViewer.getTree();
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 200;
		tree.setLayoutData(gridData);
		tree.setHeaderVisible(true);

		// Add the columns to the table
		TreeColumn treeColumn = new TreeColumn(tree, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeColumn.setText(plugetParameter.getLabel());
		treeColumn.setWidth(500);

		// get the EClass
		String eclassContent = ((AutosarDatatype) type).getEclassFromCid();
		EClass eclass = MMSRegistry.INSTANCE.getMMService(activeProject).findEClass(eclassContent);
		QualifiedNameTreeProvider treeContentProvider = new QualifiedNameTreeProvider(eclass, activeProject);
		treeContentProvider.setDatatype(type);
		treeViewer.setContentProvider(treeContentProvider);
		List<EObject> instancesOfType = ModelUtils.getInstancesOfType(activeProject, eclass, true, true);

		treeViewer.setInput(instancesOfType);

		// Create sided label for description, if description is available
		String description = plugetParameter.getDescription();
		if (description != null && description != AutomationConstants.EMPTY_STRING)
		{
			Label descriptionLabel = new Label(treeViewerComposite, SWT.WRAP);
			// make it multi line
			GridData labelGridData = new GridData(SWT.LEFT, SWT.FILL, true, true);
			labelGridData.widthHint = 400;
			descriptionLabel.setLayoutData(labelGridData);
			descriptionLabel.setText(description);
		}

		// add a listener for element selection to check if the Finish button can be enabled
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				// notify the editor listener that a selection was made
				if (listener != null)
				{
					Event changeEvent = new Event();
					changeEvent.type = SWT.Selection;
					listener.handleEvent(changeEvent);
				}
			}
		});
		return null;
	}

	@Override
	public void initDatatype(IDatatype<GIdentifiable> datatype)
	{
		type = datatype;
	}

	@Override
	public void setValue(String value)
	{
		// not implemented
	}

	@Override
	public String getValue()
	{
		// will return the element selected in the tree viewer or null if no element is selected
		String elementQualifiedName = null;
		TreeSelection selection = (TreeSelection) treeViewer.getSelection();

		if (!selection.isEmpty())
		{
			elementQualifiedName = selection.getFirstElement().toString();
		}
		return elementQualifiedName;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.cid.ui.IDatatypeEditor#getParameterForEditor()
	 */
	@Override
	public IArtifactParameter<GIdentifiable> getParameterForEditor()
	{
		return plugetParameter;
	}

	public void add(Listener listener1)
	{
		listener = listener1;
	}
}
