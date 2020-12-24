package com.julioromano.batchimporter.batch;

import java.io.IOException;

public interface BatchWatcher {

    public void watchFiles(String path) throws IOException;

}
