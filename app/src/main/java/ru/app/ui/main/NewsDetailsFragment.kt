package ru.app.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.fragment_news_details.view.*
import ru.app.R


class NewsDetailsFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_news_details, container, false)

        val bundleReceiver = this.arguments
        var newsTitle = ""
        if (bundleReceiver != null) {
            newsTitle = bundleReceiver.getString("NEWS_TITLE_DATA", "-1")
            rootView.news_details_fragment_news_title.text = newsTitle
        }

        var newsImage = ""
        if (bundleReceiver != null) {
            newsImage = bundleReceiver.getString("NEWS_IMAGE_DATA", "-1")
            Glide.with(this)
                    .load(newsImage)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .transition(DrawableTransitionOptions.withCrossFade(400))
                    .into(rootView.news_details_fragment_news_image);

        }

        var newsDate = ""
        if (bundleReceiver != null) {
            newsDate = bundleReceiver.getString("NEWS_DATE_DATA", "-1")
            rootView.news_details_fragment_news_date.text = newsDate
        }

        var newsDescription = ""
        if (bundleReceiver != null) {
            newsDescription = bundleReceiver.getString("NEWS_DESCRIPTION_DATA", "-1")
            rootView.news_details_fragment_news_description.text = newsDescription
        }


        // Back button
        rootView.news_details_fragment_back_button.setOnClickListener{
            navController.navigate(R.id.action_newsDetailsFragment_to_newsFragment)
        }




        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


}