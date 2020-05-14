package luyao.wanandroid.ui.navigation

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_navigation.*
import luyao.util.ktx.ext.dp2px
import luyao.wanandroid.R
import luyao.wanandroid.adapter.NavigationAdapter
import luyao.wanandroid.adapter.VerticalTabAdapter
import luyao.wanandroid.model.bean.Navigation
import luyao.wanandroid.view.SpaceItemDecoration
import org.koin.androidx.viewmodel.ext.android.getViewModel
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

/**
 * 导航页面
 * Created by Lu
 * on 2018/3/28 21:26
 */
class NavigationFragment : luyao.mvvm.core.base.BaseVMFragment<NavigationViewModel>(useDataBinding = false) {

    override fun initVM(): NavigationViewModel = getViewModel()

    private val navigationList = mutableListOf<Navigation>()
    private val tabAdapter by lazy { VerticalTabAdapter(navigationList.map { it.name }) }
    private val navigationAdapter by lazy { NavigationAdapter() }
    private val mLayoutManager by lazy { LinearLayoutManager(activity) }

    override fun getLayoutResId() = R.layout.fragment_navigation

    override fun initView() {
        navigationRecycleView.run {
            layoutManager = mLayoutManager
            addItemDecoration(SpaceItemDecoration(this.dp2px(10)))
            adapter = navigationAdapter
        }

        initTabLayout()
    }

    private fun initTabLayout() {
        tabLayout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabView?, position: Int) {
            }

            override fun onTabSelected(tab: TabView?, position: Int) {
                scrollToPosition(position)
            }
        })
    }

    private fun scrollToPosition(position: Int) {
        val firstPotion = mLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = mLayoutManager.findLastVisibleItemPosition()
        when {
            position <= firstPotion || position >= lastPosition -> navigationRecycleView.smoothScrollToPosition(position)
            else -> navigationRecycleView.run {
                smoothScrollBy(0, this.getChildAt(position - firstPotion).top - this.dp2px(8))
            }
        }
    }

    override fun initData() {
        mViewModel.getNavigation()
    }

    private fun getNavigation(navigationList: List<Navigation>) {
        this.navigationList.clear()
        this.navigationList.addAll(navigationList)
        tabLayout.setTabAdapter(tabAdapter)

        navigationAdapter.setNewData(navigationList)
    }

    override fun startObserve() {
        mViewModel.run {
            uiState.observe(viewLifecycleOwner, Observer {
                it?.let { getNavigation(it) }
            })
        }
    }
}