package com.duckduckgo


import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duckdcukgo.instant.R
import kotlinx.android.synthetic.main.about_.*

/**
 * Created by vadivel on 25/9/17.
 */
class About : Fragment() {
    val styledText = "Powered by <font color='black'>Duck Duck </font><font color='#26c107'>Go</font>"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.about_, container, false)


        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    title_about.text =  Html.fromHtml(styledText, Html.FROM_HTML_MODE_LEGACY)
                else title_about.text = Html.fromHtml(styledText)
    }
}