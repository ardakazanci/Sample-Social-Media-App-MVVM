package com.ardakazanci.samplesocialmediaapp.ui.main.ui.home.util

import android.util.Log
import androidx.paging.PagedList
import com.ardakazanci.samplesocialmediaapp.data.local.ContentDb
import com.ardakazanci.samplesocialmediaapp.data.model.Content
import com.ardakazanci.samplesocialmediaapp.data.model.Doc
import com.ardakazanci.samplesocialmediaapp.data.network.ApiService
import com.ardakazanci.samplesocialmediaapp.data.network.IApiInterface
import com.ardakazanci.samplesocialmediaapp.utils.PagingRequestHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class ContentBoundaryCallback(private val db: ContentDb) :
    PagedList.BoundaryCallback<Doc>() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val api = ApiService.getRetrofit().create(IApiInterface::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    private val helper = PagingRequestHelper(executor)


    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->

            scope.launch {
                try {
                    val response = api.fetchContents()
                    when {
                        response.isSuccessful -> {

                            val contents = response.body()?.content!!.docs
                            executor.execute {
                                db.contentDao().insert(contents)
                                helperCallback.recordSuccess()
                            }


                        }
                    }

                } catch (exception: Exception) {
                    Log.e("RedditBoundaryCallback", "Failed to load data!")
                    helperCallback.recordFailure(exception)
                }


            }


        }


    }

    override fun onItemAtEndLoaded(itemAtEnd: Doc) {
        super.onItemAtEndLoaded(itemAtEnd)

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->


            scope.launch {


                try {

                    val response =
                        api.fetchContents(after = itemAtEnd.sharingDate)

                    when {
                        response.isSuccessful -> {
                            val contents = response.body()?.content!!.docs
                            executor.execute {
                                db.contentDao().insert(contents)
                                helperCallback.recordSuccess()

                            }

                        }

                    }
                } catch (exception: Exception) {
                    Log.e("RedditBoundaryCallback", "Failed to load data!")
                    helperCallback.recordFailure(exception)
                }


            }


        }
    }

}