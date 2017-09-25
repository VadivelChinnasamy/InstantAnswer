package intent.com.duckduckgo.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.duckdcukgo.instant.R
import com.duckduckgo.ShowPics
import com.duckduckgo.Utils.SectionOrRow
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.section_header.view.*
import kotlinx.android.synthetic.main.see_all_matches.view.*


/**
 * Created by vadivel on 13/9/17.
 */

@Suppress("DEPRECATION")
internal class Summaries(activity: FragmentActivity, mResultArr: ArrayList<SectionOrRow>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var mContext: Context = activity
    val dataBody: ArrayList<SectionOrRow> = mResultArr


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.see_all_matches, viewGroup, false)
            return ViewHolder(view)

        } else {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.section_header, viewGroup, false)
            return HeaderViewHolder(view)

        }

        //val view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.see_all_matches, viewGroup, false)

    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        val item = dataBody[position]

        if (!item.isRow) {
            return 0
        } else {
            return 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is HeaderViewHolder) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)

                holder.itemView.header_txt.text = Html.fromHtml(dataBody[position].result, Html.FROM_HTML_MODE_LEGACY)
            else
                holder.itemView.header_txt.text = Html.fromHtml(dataBody[position].result)


        } else if (holder is ViewHolder) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)

                holder.itemView.result.text = Html.fromHtml(dataBody[position].result, Html.FROM_HTML_MODE_LEGACY)
            else
                holder.itemView.result.text = Html.fromHtml(dataBody[position].result)


            holder.itemView.result.movementMethod = LinkMovementMethod.getInstance();

            val charSequence = holder.itemView.result.text
            val sp = SpannableStringBuilder(charSequence)

            val spans = sp.getSpans(0, charSequence.length, URLSpan::class.java)

            for (urlSpan in spans) {
                val mySpan = MySpan(urlSpan.url)
                sp.setSpan(mySpan, sp.getSpanStart(urlSpan),
                        sp.getSpanEnd(urlSpan), Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            }

            holder.itemView.result.text = sp
            Log.e("----->", "IMAGE==========>" + dataBody[position].images)

            try {
                if (dataBody[position].images.isNotEmpty()) {
                    Picasso.with(mContext)
                            .load(dataBody[position].images)

                            .placeholder(R.mipmap.ic_logo)
                            .transform(RoundedTransformation(280, 10))
                            .error(R.mipmap.ic_logo)
                            .into(holder.itemView.mImageView, object : Callback {
                                override fun onError() {
                                    holder.itemView.mProgressbar.visibility = View.GONE
                                }

                                override fun onSuccess() {
                                    holder.itemView.mProgressbar.visibility = View.GONE


                                }

                            })
                } else {
                    Picasso.with(mContext)
                            .load(R.mipmap.ic_logo)

                            .into(holder.itemView.mImageView);
                }
            } catch (e: Exception) {
                e.printStackTrace();
            }

            holder.itemView.mImageView.setOnClickListener {

                mContext.startActivity(Intent(mContext, ShowPics::class.java)
                        .putExtra("image", dataBody[position].images))
            }
        }
    }


    override fun getItemCount(): Int {

        return dataBody.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            ButterKnife.bind(this, itemView)
        }
    }

    internal inner class HeaderViewHolder(var View: View) : RecyclerView.ViewHolder(View) {

        init {
            ButterKnife.bind(this, View)


        }
    }

    private var isClickingLink = false

    private inner class MySpan(private val mUrl: String) : ClickableSpan() {

        override fun onClick(widget: View) {

            isClickingLink = true

            Log.w("log", "=========== clicking link")
            // 1. do url click
        }

    }

    class RoundedTransformation(i: Int, i1: Int) : Transformation {

        private var radius: Int = i
        private var margin: Int = i1 // dp


        override fun transform(source: Bitmap): Bitmap {

            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = BitmapShader(source, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP)

            val output = Bitmap.createBitmap(source.width,
                    source.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (source.width - margin).toFloat(), (source.height - margin).toFloat()), radius.toFloat(), radius.toFloat(), paint)

            if (source != output) {
                source.recycle()
            }

            val paint1 = Paint()
            paint1.color = Color.TRANSPARENT
            paint1.style = Paint.Style.STROKE
            paint1.isAntiAlias = true
            paint1.strokeWidth = 2f
            canvas.drawCircle(((source.width - margin) / 2).toFloat(),
                    ((source.height - margin) / 2).toFloat(), (radius - 2).toFloat(), paint1)

            return output
        }

        override fun key(): String {
            return "rounded"
        }


    }
}
