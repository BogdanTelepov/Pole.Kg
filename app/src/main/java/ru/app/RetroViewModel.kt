package ru.app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.app.models.news.NewsListItem
import ru.app.network.ApiService
import ru.app.network.RetrofitInstance


class RetroViewModel : ViewModel() {

    var recyclerListData: MutableLiveData<MutableList<NewsListItem>> = MutableLiveData()

    fun getRecyclerListDataObserver(): MutableLiveData<MutableList<NewsListItem>> {
        return recyclerListData
    }

    fun makeApiCallForNewsList() {
        val retroInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val call = retroInstance.getNewsList()

        call.enqueue(object : Callback<MutableList<NewsListItem>>{
            override fun onResponse(call: Call<MutableList<NewsListItem>>, response: Response<MutableList<NewsListItem>>) {
                if(response.isSuccessful) {
                    //recyclerViewAdapter.setListData(response.body()?.items!!)
                    //recyclerViewAdapter.notifyDataSetChanged()
                    recyclerListData.postValue(response.body())
                } else {
                    recyclerListData.postValue(null)
                }
            }

            override fun onFailure(call: Call<MutableList<NewsListItem>>, t: Throwable) {
                // Toast.makeText(this@RecyclerViewActivity, "Error in getting data from api.", Toast.LENGTH_LONG).show()

                recyclerListData.postValue(null)
            }
        })
    }
}