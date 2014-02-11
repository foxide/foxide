package org.foxide.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class FxPerspective implements IPerspectiveFactory {

    public static final String PACKAGE_EXPLORER = "org.eclipse.jdt.ui.PackageExplorer";
    public static final String MARKERS = "org.eclipse.ui.views.AllMarkersView";
    public static final String PROJECT_EXPLORER = IPageLayout.ID_PROJECT_EXPLORER;
    public static final String OUTLINE = IPageLayout.ID_OUTLINE;
    public static final String PROPERTIES = IPageLayout.ID_PROP_SHEET;

    @Override
    public void createInitialLayout(IPageLayout factory) {
        IFolderLayout topLeft = factory.createFolder("topLeft", //$NON-NLS-1$
                IPageLayout.LEFT, 0.25f, factory.getEditorArea());

        IFolderLayout bottom = factory.createFolder("bottomRight", //$NON-NLS-1$
                IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());

        IFolderLayout topRight = factory.createFolder("topRight", //$NON-NLS-1$
                IPageLayout.RIGHT, 0.75f, factory.getEditorArea());

        topLeft.addView(PROJECT_EXPLORER);
        topRight.addView(OUTLINE);
        bottom.addView(MARKERS);
        bottom.addView(PROPERTIES);

    }
}