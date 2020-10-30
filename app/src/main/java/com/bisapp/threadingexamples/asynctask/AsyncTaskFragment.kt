package com.bisapp.threadingexamples.asynctask

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bisapp.customrecyclerview.CustomRecyclerView
import com.bisapp.threadingexamples.R
import com.bisapp.threadingexamples.utils.FetchDocumentsOnStorage
import com.bisapp.threadingexamples.utils.FileData
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_first.*
import java.lang.ref.WeakReference
import java.util.concurrent.Executors


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AsyncTaskFragment : Fragment(), CustomRecyclerView.BindViewsListener,
    DocsRetrieveCallbacks,
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var fetchDocs: FetchDocumentsOnStorage
    lateinit var docRetrieveAsynctask: DocsRetrieveAsyncTask
    lateinit var request: RequestBuilder<Drawable>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //fetch the docs on the device internal storage
        asyncTaskCall(false)
        swipe_to_refresh.setOnRefreshListener(this)

        category_recyclerview.setDivider(true)
            .setBindViewsListener(this)
        request = Glide.with(this).asDrawable().fitCenter()
    }

    private fun asyncTaskCall(parallelExecution: Boolean) {
        fetchDocs = FetchDocumentsOnStorage(WeakReference(context))
        docRetrieveAsynctask = DocsRetrieveAsyncTask(this)
        if (parallelExecution)
            docRetrieveAsynctask.executeOnExecutor(Executors.newFixedThreadPool(3))
        else
            docRetrieveAsynctask.execute()
    }

    override fun bindViews(view: View?, objects: MutableList<*>?, position: Int) {

        val fileData = objects?.get(position) as FileData
        val path = view?.findViewById<TextView>(R.id.title)
        val size = view?.findViewById<TextView>(R.id.size)
        val icon = view?.findViewById<CircleImageView>(R.id.apk_icon)
        path?.text = fileData.name.name
        size?.text = fileData.size
        request.load(fileData.name)
            .into(icon!!)

    }

    override fun onDone() {
        swipe_to_refresh.isRefreshing = false
        progress_bar.visibility = View.GONE
    }

    override fun onStartRetrieving() {
        swipe_to_refresh.isRefreshing = true
        progress_bar.visibility = View.VISIBLE
    }

    override fun doExecute() {
        val files = fetchDocs.listFiles()
        if (!files.isEmpty()) {
            Handler(Looper.getMainLooper()).post {
                category_recyclerview.addModels(files)
            }

        }
    }

    override fun onRefresh() {
        if (!docRetrieveAsynctask.isCancelled) {
            docRetrieveAsynctask.cancel(true)
        }

        docRetrieveAsynctask = DocsRetrieveAsyncTask(this)
        docRetrieveAsynctask.execute()
    }
}