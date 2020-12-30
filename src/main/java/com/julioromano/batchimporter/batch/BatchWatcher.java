package com.julioromano.batchimporter.batch;

import com.julioromano.batchimporter.exceptions.FileWatchingException;

/**
 * {@code BatchWatcher} interface represents the contract in which a valid
 * Watcher should comply in order to keep the application working.
 *
 * {@code BatchWatcher} interface represents only the entrypoint in which
 * the whole process start.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public interface BatchWatcher {

    void watchFiles(String path) throws FileWatchingException;

}
