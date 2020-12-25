package ru.app.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_feedbacks.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.app.R
import ru.app.adapters.FieldReviewsAdapter
import ru.app.models.fields.FieldReviewsItem
import ru.app.network.ApiService
import ru.app.network.RetrofitInstance

class FeedbacksFragment : Fragment() {

    private val TAG = "FeedbacksFragment"

    private lateinit var fieldReviewsAdapter: FieldReviewsAdapter

    /** Main vars*/
    private var fieldId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_feedbacks, container, false)

        fieldId = (parentFragment as ViewPagerFragment).fieldId.toString()

        getFieldReviewsList(fieldId)

        return rootView

    }

    private fun getFieldReviewsList(fieldId: String) {

        val retroInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val call = retroInstance.getFieldReviewsList(fieldId)

        call.enqueue(object : Callback<MutableList<FieldReviewsItem>> {

            override fun onResponse(call: Call<MutableList<FieldReviewsItem>>, response: Response<MutableList<FieldReviewsItem>>) {

                if (response.isSuccessful) {

                    if (response.body()!!.isEmpty()) {

                        field_reviews_fragment_text_nothing_found.visibility = View.VISIBLE

                    } else {

                        fieldReviewsAdapter = FieldReviewsAdapter(requireContext())
                        fieldReviewsAdapter.setReviewsList(response.body() as MutableList<FieldReviewsItem>)
                        field_reviews_fragment_recycler_view.adapter = fieldReviewsAdapter

                    }

                    Log.e(TAG, "onCreateView: $fieldId ${response.body()}" )

                }

            }

            override fun onFailure(call: Call<MutableList<FieldReviewsItem>>, t: Throwable) {
                Toast.makeText(requireContext(), getString(R.string.unknown_error), Toast.LENGTH_LONG).show()
            }

        })

    }

}