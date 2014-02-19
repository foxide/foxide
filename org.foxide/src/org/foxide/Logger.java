package org.foxide;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class Logger {
    private static ILog LOG = Activator.getDefault().getLog();

    public static void info(String message) {
        LOG.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, message));
    }
}
