package com.s3.toolkit.cs.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.s3.toolkit.builder.FiltecNature;

public class CSWizard extends Wizard implements INewWizard {

	private CSWizardPage page;

	public CSWizard() {
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}
	
	@Override
	public void addPages() {
		page = new CSWizardPage();
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(page.getProjectName());
		
		try {
			project.create(progressMonitor);
			project.open(progressMonitor);
			IProjectDescription description = project.getDescription();
			description.setNatureIds(new String[]{FiltecNature.NATURE_ID});
			project.setDescription(description, progressMonitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
