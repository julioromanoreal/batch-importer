package com.julioromano.batchimporter.batch;

import com.julioromano.batchimporter.batch.files.FileBatchWatcher;

public abstract class BatchWatcherFactory {

    public static BatchWatcher getFileBatchWatcher() {
        return new FileBatchWatcher();
    }

}
