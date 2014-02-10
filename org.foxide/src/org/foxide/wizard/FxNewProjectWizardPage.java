package org.foxide.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class FxNewProjectWizardPage extends WizardPage {

    private FxNewProjectWizardPageComposite rootComposite;

    protected FxNewProjectWizardPage() {
        super("newporject");
        setTitle("Create a Firefox OS App Project");
        setDescription("Enter a project name.");
    }

    @Override
    public void createControl(Composite parent) {
        rootComposite = new FxNewProjectWizardPageComposite(parent, SWT.NONE);
        setControl(rootComposite);
    }

    public String getProjectName() {
        return rootComposite.getText().getText();
    }

}
