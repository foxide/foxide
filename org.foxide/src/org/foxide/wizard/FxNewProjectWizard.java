package org.foxide.wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.foxide.Activator;
import org.osgi.framework.Bundle;

public class FxNewProjectWizard extends Wizard implements INewWizard {

    private IWorkbench workbench;
    protected IStructuredSelection selection;
    private FxNewProjectWizardPage page = new FxNewProjectWizardPage();

    private IOverwriteQuery query = new IOverwriteQuery() {
        @Override
        public String queryOverwrite(String pathString) {
            return ALL;
        }
    };

    @Override
    public void addPages() {
        addPage(page);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        TrayDialog.setDialogHelpAvailable(false);
        this.workbench = workbench;
        this.selection = currentSelection;
        ImageDescriptor desc = IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/new_wiz.png");//$NON-NLS-1$
        setDefaultPageImageDescriptor(desc);
    }

    @Override
    public boolean performFinish() {
        final String projectName = page.getProjectName();
        try {
            createProject(projectName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void createProject(final String projectName) throws InvocationTargetException, InterruptedException {
        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            protected void execute(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                try {
                    importProject(projectName);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            }
        };
        getContainer().run(true, true, op);
    }

    protected void importProject(String projectName) throws URISyntaxException, IOException, CoreException, InvocationTargetException, InterruptedException {
        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
        URL bundleFileURL = bundle.getEntry("templates/projects/default/");
        URL projectFileURL = FileLocator.resolve(bundleFileURL);

        ImportProject importProject = new ImportProject(projectFileURL);

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProjectDescription description = workspace.loadProjectDescription(importProject.getProjectFile());
        IProjectDescription desc = workspace.newProjectDescription(projectName);
        desc.setBuildSpec(description.getBuildSpec());
        desc.setComment(description.getComment());
        desc.setDynamicReferences(description.getDynamicReferences());
        desc.setNatureIds(description.getNatureIds());
        desc.setReferencedProjects(description.getReferencedProjects());

        IProject project = workspace.getRoot().getProject(projectName);
        project.create(desc, null);
        project.open(IResource.BACKGROUND_REFRESH, null);

        ImportOperation operation = new ImportOperation(project.getFullPath(), importProject.getProjectRootFolder(), importProject.getImportStructureProvider(), query);
        operation.setContext(getShell());
        operation.setOverwriteResources(true);
        operation.setCreateContainerStructure(false);
        operation.run(null);
    }
}
