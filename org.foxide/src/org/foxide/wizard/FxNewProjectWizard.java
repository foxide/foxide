package org.foxide.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
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
    private IImportStructureProvider provider = FileSystemStructureProvider.INSTANCE;

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
        try {
            final String projectName = page.getProjectName();
            
            WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
                protected void execute(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        IWorkspace workspace = ResourcesPlugin.getWorkspace();

                        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
                        URL projectFileURL = bundle.getEntry("templates/projects/default/.project");
                        URL importRootURI = bundle.getEntry("templates/projects/default/");

                        File file = new File(FileLocator.resolve(projectFileURL).toURI());
                        File importRootFolder = new File(FileLocator.resolve(importRootURI).toURI());
                        IProjectDescription description = workspace.loadProjectDescription(new FileInputStream(file));

                        IProjectDescription desc = workspace.newProjectDescription(projectName);
                        desc.setBuildSpec(description.getBuildSpec());
                        desc.setComment(description.getComment());
                        desc.setDynamicReferences(description.getDynamicReferences());
                        desc.setNatureIds(description.getNatureIds());
                        desc.setReferencedProjects(description.getReferencedProjects());

                        IProject project = workspace.getRoot().getProject(projectName);
                        project.create(desc, null);
                        project.open(IResource.BACKGROUND_REFRESH, null);

                        ImportOperation operation = new ImportOperation(project.getFullPath(), importRootFolder, provider, query);
                        operation.setContext(getShell());
                        operation.setOverwriteResources(true);
                        operation.setCreateContainerStructure(false);
                        operation.run(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            getContainer().run(true, true, op);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
