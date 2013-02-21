package org.talend.camel.designer.ui.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.camel.designer.i18n.Messages;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.ui.dialog.RepositoryReviewDialog;
import org.talend.repository.ui.utils.RecombineRepositoryNodeUtil;

public class AssignJobPage extends WizardPage {
	private AssignJobReviewDialog dialog;
	private String processId;
	private String id;

	public String getId() {
		return id;
	}

	protected AssignJobPage(String pageName, String currentProcessId) {
		super(pageName);
		this.processId = currentProcessId;
	}

	public void createContents(Composite parent) {
	}

	public void createControl(Composite parent) {
		setTitle(Messages.getString("AssignJobPage_title"));//$NON-NLS-1$
		setDescription(Messages.getString("AssignJobPage_message"));//$NON-NLS-1$
		
		RepositoryManager.refresh(ERepositoryObjectType.PROCESS);
		
		dialog = new AssignJobReviewDialog(
				(AssignJobWizardDialog) getContainer(), parent.getShell(),
				ERepositoryObjectType.PROCESS, "", new ViewerFilter[] { new RouteInputContainedFilter() });
		setControl(dialog.createDialogArea(parent));		
	}

	@Override
	public IWizardPage getNextPage() {
		return null;
	}

	public boolean finish() {
		dialog.okPressed();
		if (dialog.getResult() != null) {
			IRepositoryViewObject repositoryObject = dialog.getResult()
					.getObject();
			final Item item = repositoryObject.getProperty().getItem();
			id = item.getProperty().getId();
			return true;
		}
		return false;
	}

	private class RouteInputContainedFilter extends ViewerFilter {

		private List<IRepositoryNode> routeInputContainedJobs = new ArrayList<IRepositoryNode>();

		private RouteInputContainedFilter() {
			/*
			 * find all RouteInput contained Jobs first
			 */
			IRepositoryNode jobRoot = RecombineRepositoryNodeUtil
					.getFixingTypesInputRoot(
							ProjectRepositoryNode.getInstance(),
							Arrays.asList(ERepositoryObjectType.PROCESS));
			addAllRouteInputContainedJob(routeInputContainedJobs, jobRoot);
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (!(element instanceof IRepositoryNode)) {
				return false;
			}
			IRepositoryNode node = (IRepositoryNode) element;
			ENodeType type = node.getType();
			/*
			 * if it's an element and contains a tRouteInput then selected
			 */
			if (type == ENodeType.REPOSITORY_ELEMENT) {
				for (IRepositoryNode rn : routeInputContainedJobs) {
					if (rn == node) {
						return true;
					}
				}
				return false;
			}
			/*
			 * if it's a container node, and some child of it contains a
			 * tRouteInput then selected
			 */
			else {
				for (IRepositoryNode rn : routeInputContainedJobs) {
					if (isAncestor(rn, node)) {
						return true;
					}
				}
			}
			return false;
		}

		private boolean isAncestor(IRepositoryNode jobNode,
				IRepositoryNode ancestor) {
			if (jobNode == null || ancestor == null) {
				return false;
			}
			IRepositoryNode current = jobNode;
			while (current != ancestor) {
				if (current == null) {
					return false;
				}
				current = current.getParent();
			}
			return true;
		}

		/**
		 * find all Jobs which contains a tRouteInput component
		 * 
		 * @param routeInputContainedJobs
		 * @param jobNode
		 */
		private void addAllRouteInputContainedJob(
				List<IRepositoryNode> routeInputContainedJobs,
				IRepositoryNode jobNode) {
			if (jobNode == null) {
				return;
			}
			if (jobNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
				try {
					Item item = jobNode.getObject().getProperty().getItem();
					if (!(item instanceof ProcessItem)) {
						return;
					}
					ProcessItem pi = (ProcessItem) item;
					EList<?> nodes = pi.getProcess().getNode();
					Iterator<?> iterator = nodes.iterator();
					while (iterator.hasNext()) {
						Object next = iterator.next();
						if (!(next instanceof NodeType)) {
							continue;
						}
						NodeType nt = (NodeType) next;
						String componentName = nt.getComponentName();
						if ("tRouteInput".equals(componentName)) {
							routeInputContainedJobs.add(jobNode);
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<IRepositoryNode> children = jobNode.getChildren();
			for (IRepositoryNode child : children) {
				addAllRouteInputContainedJob(routeInputContainedJobs, child);
			}
		}
	}	
	
	class AssignJobReviewDialog extends RepositoryReviewDialog {

		private AssignJobWizardDialog container;

		public AssignJobReviewDialog(AssignJobWizardDialog container,
				Shell parentShell, ERepositoryObjectType type,
				String repositoryType, ViewerFilter[] vf) {
			super(parentShell, type, repositoryType, vf);
			this.container = container;
		}

		@Override
		public Control createDialogArea(Composite parent) {
			return super.createDialogArea(parent);
		}

		@Override
		protected Button getButton(int id) {
			if (id == OK) {
				return container.getButton(IDialogConstants.FINISH_ID);
			} else if (id == CANCEL) {
				return container.getButton(IDialogConstants.CANCEL_ID);
			}
			return super.getButton(id);
		}

		@Override
		public void okPressed() {
			super.okPressed();
		}
	}
	
}