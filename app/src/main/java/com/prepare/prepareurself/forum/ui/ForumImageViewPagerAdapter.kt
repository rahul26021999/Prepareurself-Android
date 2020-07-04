package com.prepare.prepareurself.forum.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.OpenForumAttachment
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.fullscreen_image_dialog.view.*

class ForumImageViewPagerAdapter(var attachments:List<String>,
                                 var context:Context,
                                 var type:Int): PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.fullscreen_image_dialog,container,false)
        val attachment = attachments[position]

        view.tv_name_fullscreen_image.text = attachment

        if (attachment.isNotEmpty()){
            var imagUrl = ""
            if (type==1){
                imagUrl = "${Constants.QUERYATTACHMENTBASEURL}${attachment}"
            }else if (type == 2){
                imagUrl = "${Constants.REPLYATTACHMENTBASEURL}${attachment}"
            }
            if (imagUrl.endsWith(".svg")){
                Utility.loadSVGImage(context, imagUrl, view.fullscreen_image_forum)
            }else{
                Glide.with(context)
                        .load(imagUrl)
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(view.fullscreen_image_forum)
            }
        }

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun getCount(): Int {
        return attachments.size
    }
}