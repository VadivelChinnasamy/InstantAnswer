package com.duckduckgo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import butterknife.ButterKnife
import com.duckdcukgo.instant.R
import com.duckduckgo.Utils.Constants
import com.duckduckgo.Utils.SectionOrRow
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import intent.com.duckduckgo.Adapter.Summaries
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.include_searchbar.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


@Suppress("DEPRECATION")
class HomeFrag : Fragment() {

    private var dataBody: String? = null
    private var mResultArr: ArrayList<SectionOrRow> = ArrayList()
    private var readMore: Boolean = false
    private var mAbstractMsg: String = ""
    val styledText = "Powered by <font color='black'>Duck Duck </font><font color='#26c107'>Go</font>"
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        val view = inflater!!.inflate(R.layout.fragment_home, container, false)
        ButterKnife.bind(this, view)

        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMethod("today" as String)


        search_Img.setOnClickListener {view->

            hideKeyboard()
            poweredtxt.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                poweredtxt.text =  Html.fromHtml(styledText, Html.FROM_HTML_MODE_LEGACY)
            else poweredtxt.text = Html.fromHtml(styledText)
                val txt=search_edt.text.toString().trim()

            if (!search_edt.text.isEmpty()) getMethod(""+txt); else Toast.makeText(activity, "Search box empty", Toast.LENGTH_LONG).show()
        }

        read_more_txt.setOnClickListener {

            if (!readMore) {
                abstract_txt.maxLines = 100
                readMore = true
                read_more_txt.text = "Minimize..."
            } else {
                readMore = false
                abstract_txt.maxLines = 1
                read_more_txt.text = "ReadMore..."
            }

            abstract_txt.text = mAbstractMsg
        }
        hideKeyboard()

    }

    private fun hideKeyboard(){
        val view = activity.getCurrentFocus()
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }
    }

    private fun getMethod(searchTxt: String) {
        val okHttpClient = OkHttpClient()
        mResultArr = ArrayList()
        val builder = Request.Builder()

        val request = builder
                .url(Constants.BASEURL + searchTxt + "&format=json&pretty=1")
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request, e: IOException) {
                dataBody = "Error\n" + e.message

                updateView()
            }

            override fun onResponse(response: Response) {
                Log.e("**********", "__dataBody_" + response.body())
                if (response.isSuccessful) {
                    try {

                        dataBody = response.body().string()
                        val mObj: JSONObject = JSONObject(dataBody)
                        val mArr: JSONArray = mObj.getJSONArray("RelatedTopics")
                        mAbstractMsg = mObj.getString("Abstract")


                        if (mArr.length() != 0) {

                            activity.runOnUiThread {
                                no_result.visibility = View.GONE
                                recycleView.visibility = View.VISIBLE
                            }
                            for (i in 0..mArr.length() - 1) {

                                if (mArr.getJSONObject(i).has("Result")) {

                                    mResultArr.add(SectionOrRow.createSection(mArr.getJSONObject(i).getString("Result"),
                                            mArr.getJSONObject(i).getString("FirstURL"),
                                            mArr.getJSONObject(i).getString("Text"),
                                            mArr.getJSONObject(i).getJSONObject("Icon").getString("URL")))


                                } else {
                                    mResultArr.add(SectionOrRow.createRow(mArr.getJSONObject(i).getString("Name")))
                                    val mTopicsArr: JSONArray = mArr.getJSONObject(i).getJSONArray("Topics")
                                    if (mArr.length() != 0) {
                                        for (j in 0..mTopicsArr.length() - 1) {
                                            mResultArr.add(SectionOrRow.createSection(mTopicsArr.getJSONObject(j).getString("Result"),
                                                    mTopicsArr.getJSONObject(j).getString("FirstURL"),
                                                    mTopicsArr.getJSONObject(j).getString("Text"),
                                                    mTopicsArr.getJSONObject(j).getJSONObject("Icon").getString("URL")))

                                        }



                                    }

                                }

                            }

                        }else{
                            activity.runOnUiThread {
                                no_result.visibility = View.VISIBLE
                                recycleView.visibility = View.GONE
                            }
                        }


                    } catch (e: IOException) {
                        activity.runOnUiThread {
                            no_result.visibility = View.VISIBLE
                            recycleView.visibility = View.GONE
                        }
                        e.printStackTrace()
                    }


                } else {
                    dataBody = "Not Success\ncode : " + response.code()
                }

                updateView()
            }

            fun updateView() {
                activity.runOnUiThread { showView() }
            }
        })
    }

    private fun showView() {
        abstract_txt.text = mAbstractMsg
        if (mAbstractMsg.isNotEmpty()) readmore_layout.visibility = View.VISIBLE else readmore_layout.visibility = View.GONE

        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = Summaries(activity, mResultArr)
    }
    /*private void postMethodNoBody() {


        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        Request request = builder
                .url(urlEdt.getText().toString().trim())
                .post(body)
                .build();
        Log.e("**********", "!!__________________API CALL________________________");
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Log.e("**********", "!!__________________API CALL____FAILURE____________________");
            }

            @Override
            public void onResponse(Response response) {
                Log.e("**********", "!!__________________API onResponse");
                if (response.isSuccessful()) {

                    try {
                        dataBody = response.body().string();
                        Log.e("**********", "!!!!!!!!!" + dataBody);
                        JSONObject mObj = new JSONObject(dataBody);


                    } catch (Exception e) {
                        e.printStackTrace();
                        dataBody = "Error !\n\n" + e.getMessage();
                    }


                } else {
                    dataBody = "Not Success\ncode : " + response.code();
                }

                updateView();

            }

            public void updateView() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        showView();
                    }
                });
            }

        });


    }*/
    fun formatString(text: String): String {
        val json = StringBuilder()
        var indentString = ""
        for (i in 0..text.length - 1) {
            val letter = text[i]
            when (letter) {
                '{', '[' -> {
                    json.append("\n" + indentString + letter + "\n")
                    indentString = indentString + "\t"
                    json.append(indentString)
                }
                '}', ']' -> {
                    indentString = indentString.replaceFirst("\t".toRegex(), "")
                    json.append("\n" + indentString + letter)
                }
                ',' -> json.append(letter + "\n" + indentString)
                else -> json.append(letter)
            }
        }
        return json.toString()
    }

}
