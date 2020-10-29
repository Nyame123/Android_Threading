package com.bisapp.threadingexamples.asynctask;

import android.os.AsyncTask;

public class DocsRetrieveAsyncTask extends AsyncTask<String, String, Void> {

    private DocsRetrieveCallbacks callbacks;

    public DocsRetrieveAsyncTask(DocsRetrieveCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // this is where you prepare your stuffs before trying to execute things in the background
        callbacks.onStartRetrieving();
    }

    /**
     * This method is called to do stuff in the background
     *
     * @param strings this the first params passed in the typed params of the class
     **/
    @Override
    protected Void doInBackground(String... strings) {
        // do the heavy stuffs in here
        callbacks.doExecute();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // this is where you get the result after running in the background
        super.onPostExecute(aVoid);
        callbacks.onDone();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //this is where you update the items or ui on the main thread from the background works
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        // do what you want to do on the main thread when cancelling the async task
    }
}
