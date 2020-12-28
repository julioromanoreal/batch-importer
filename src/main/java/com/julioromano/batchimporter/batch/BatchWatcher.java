package com.julioromano.batchimporter.batch;

import com.julioromano.batchimporter.exceptions.ProcessingException;

public interface BatchWatcher {

    void watchFiles(String path) throws ProcessingException;

}
