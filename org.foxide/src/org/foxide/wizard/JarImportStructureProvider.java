package org.foxide.wizard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;

class JarImportStructureProvider implements IImportStructureProvider {

    private JarFile jarFile;

    public JarImportStructureProvider(JarFile jarFile) throws IOException {
        this.jarFile = jarFile;
    }

    @Override
    public List getChildren(Object element) {
        ZipEntry entry = (ZipEntry) element;
        IPath parentPath = new org.eclipse.core.runtime.Path(entry.getName()).addTrailingSeparator();
        String parent = parentPath.toString();
        int parentCount = parentPath.segmentCount();

        List result = new ArrayList<>();
        Enumeration<JarEntry> jarenum = jarFile.entries();
        while (jarenum.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) jarenum.nextElement();
            IPath path = new org.eclipse.core.runtime.Path(jarEntry.getName()).addTrailingSeparator();
            int count = path.segmentCount();
            if (count - 1 == parentCount && path.toString().startsWith(parent)) {
                result.add(jarEntry);
            }
        }

        return result;
    }

    @Override
    public InputStream getContents(Object element) {
        try {
            return jarFile.getInputStream((ZipEntry) element);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String getFullPath(Object element) {
        return ((ZipEntry) element).getName();
    }

    @Override
    public String getLabel(Object element) {
        ZipEntry entry = (ZipEntry) element;
        org.eclipse.core.runtime.Path path = new org.eclipse.core.runtime.Path(((ZipEntry) element).getName());
        String last = path.lastSegment();
        if (last == null) {
            return entry.getName();
        }
        return last;
    }

    @Override
    public boolean isFolder(Object element) {
        return ((ZipEntry) element).isDirectory();
    }

}