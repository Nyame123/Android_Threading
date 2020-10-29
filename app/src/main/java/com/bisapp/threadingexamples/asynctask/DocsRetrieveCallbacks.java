package com.bisapp.threadingexamples.asynctask;

public interface DocsRetrieveCallbacks {

    void onStartRetrieving();
    void doExecute();
    void onDone();
}
