package luyao.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import luyao.wanandroid.R
import luyao.wanandroid.model.bean.Article
import luyao.wanandroid.model.bean.Navigation
import luyao.wanandroid.ui.BrowserActivity


/**
 * Created by Lu
 * on 2018/3/29 22:36
 */
class NavigationAdapter(layoutResId: Int = R.layout.item_navigation) : BaseQuickAdapter<Navigation, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: Navigation) {

        helper.setText(R.id.navigationName, item.name)
        helper.getView<TagFlowLayout>(R.id.navigationTagLayout).run {
            adapter = object : TagAdapter<Article>(item.articles) {
                override fun getCount(): Int {
                    return item.articles.size
                }

                override fun getView(parent: FlowLayout, position: Int, t: Article): View {
                    val tv = LayoutInflater.from(parent.context).inflate(R.layout.tag,
                            parent, false) as TextView
                    tv.text = t.title
                    return tv
                }
            }

            setOnTagClickListener { view, position, _ ->
                androidx.navigation.Navigation.findNavController(view).navigate(R.id.action_tab_to_browser, bundleOf(BrowserActivity.URL to item.articles[position].link))
                true
            }
        }
    }

}
