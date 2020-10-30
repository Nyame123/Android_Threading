package com.bisapp.threadingexamples.loaders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import com.bisapp.threadingexamples.R

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoaderFragment : ListFragment(), LoaderManager.LoaderCallbacks<AppListLoader.AppsDataPair> {

    val ID_LOADER_APP_LIST = 0
    lateinit var adapter: AppsAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lv = listView
        LoaderManager.getInstance(this).initLoader(ID_LOADER_APP_LIST, null, this)
        adapter = AppsAdapter(requireContext(),R.layout.rowlayout)
        lv.adapter = this.adapter;
    }
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<AppListLoader.AppsDataPair> {

        return AppListLoader(requireContext())
    }

    override fun onLoadFinished(
        loader: Loader<AppListLoader.AppsDataPair>,
        data: AppListLoader.AppsDataPair?
    ) {
        // set new data to adapter
        adapter.setData(data!!.first)

        if (isResumed) {
            setListShown(true)
        } else {
            setListShownNoAnimation(true)
        }

    }

    override fun onLoaderReset(loader: Loader<AppListLoader.AppsDataPair>) {
        adapter.setData(null)

    }
}