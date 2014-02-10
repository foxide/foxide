package org.foxide.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FxNewProjectWizardPageComposite extends Composite {
    private Text text;

    public Text getText() {
        return text;
    }

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public FxNewProjectWizardPageComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(3, false));

        Label lblNewLabel = new Label(this, SWT.NONE);
        lblNewLabel.setText("Project name:");
        new Label(this, SWT.NONE);

        text = new Text(this, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
