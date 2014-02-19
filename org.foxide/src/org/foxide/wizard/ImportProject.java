package org.foxide.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;

public class ImportProject {

    private URL projectUrl;
    private URL projectFileUrl;
    private URLConnection projectFileConnection;
    private URLConnection projectConnection;

    public ImportProject(URL projectUrl) throws IOException, URISyntaxException {
        this.projectUrl = projectUrl;
        String projectFilePath = projectUrl.toString();
        if (!projectFilePath.endsWith("/")) {
            projectFilePath = projectFilePath + "/";
        }
        projectFilePath = projectFilePath + ".project";
        this.projectFileUrl = new URL(projectFilePath);
        this.projectConnection = projectUrl.openConnection();
        this.projectFileConnection = projectFileUrl.openConnection();
    }

    public InputStream getProjectFile() throws IOException, URISyntaxException {
        if (isJarFile()) {
            JarURLConnection jarconnection = (JarURLConnection) projectFileConnection;
            JarFile jarFile = jarconnection.getJarFile();
            String entryName = jarconnection.getEntryName();
            ZipEntry zentry = jarFile.getEntry(entryName);
            return jarFile.getInputStream(zentry);
        } else {
            return new FileInputStream(new File(projectFileUrl.toURI()));
        }
    }

    public Object getProjectRootFolder() throws URISyntaxException, IOException {
        if (isJarFile()) {
            JarURLConnection jarconnection = (JarURLConnection) projectConnection;
            JarFile jarFile = jarconnection.getJarFile();
            String entryName = jarconnection.getEntryName();
            ZipEntry zentry = jarFile.getEntry(entryName);
            return zentry;
        } else {
            return new File(projectUrl.toURI());
        }
    }

    private boolean isJarFile() {
        return projectConnection instanceof JarURLConnection;
    }

    public IImportStructureProvider getImportStructureProvider() throws IOException {
        if (isJarFile()) {
            JarURLConnection jarconnection = (JarURLConnection) projectConnection;
            return new JarImportStructureProvider(jarconnection.getJarFile());
        } else {
            return FileSystemStructureProvider.INSTANCE;
        }
    }

    public URLConnection getProjectConnection() {
        return projectConnection;
    }
}