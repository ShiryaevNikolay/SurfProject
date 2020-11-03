package ru.shiryaev.surfproject.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main_screen.*
import kotlinx.android.synthetic.main.fragment_main_screen.view.*
import kotlinx.android.synthetic.main.fragment_memes.view.*
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.interfaces.ShowMemeListener
import ru.shiryaev.surfproject.models.DbMeme
import ru.shiryaev.surfproject.models.controllers.DbMemeItemController
import ru.shiryaev.surfproject.screens.main.MainScreenFragment
import ru.shiryaev.surfproject.utils.App
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList

class MemesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, MenuItem.OnMenuItemClickListener {

    private lateinit var currentFragment: CurrentFragmentListener
    private lateinit var showMeme: ShowMemeListener
    private lateinit var mContext: Context
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private val memesAdapter = EasyAdapter()
    private val memeDbController = DbMemeItemController()

    private lateinit var infoEmptyListTv: TextView
    private lateinit var progressBarState: LiveData<Boolean>
    private lateinit var listEmptyState: LiveData<Boolean>
    private lateinit var refreshState: LiveData<Boolean>
    private lateinit var mMainScreenFragment: MainScreenFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        progressBarState = (mContext as MainActivity).mainActivityViewModel.progressBarMemeState
        listEmptyState = (mContext as MainActivity).mainActivityViewModel.listEmptyState
        refreshState = (mContext as MainActivity).mainActivityViewModel.refreshState
        mMainScreenFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as MainScreenFragment
        with(mMainScreenFragment) {
            currentFragment = this
            showMeme = this
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memes, container, false)

        infoEmptyListTv = view.info_list_empty

        progressBarState.observe((mContext as MainActivity), { view.progressBar.isVisible = it })

        (mContext as MainActivity).snackbarMemeShow = {
            val snack = Snackbar.make(view.mainLayout, "Произошла ошибка", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
            if (context != null) {
                snack.view.setBackgroundResource(R.drawable.warning_layout)
                snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
                snack.show()
            }
            view.info_list_empty.isVisible = memesAdapter.itemCount == 0
        }

        (mContext as MainActivity).refreshState = {
            view.mainLayout.isRefreshing = false
            view.info_list_empty.isVisible = memesAdapter.itemCount == 0
        }

        mSwipeRefreshLayout = view.mainLayout
        mSwipeRefreshLayout.apply {
            setColorSchemeColors(resources.getColor(R.color.secondaryColor))
            setProgressBackgroundColorSchemeColor(resources.getColor(R.color.colorAccent))
        }

        initRecyclerView(view.recyclerView)

        (mContext as MainActivity).mainActivityViewModel.requestMeme(App.LOADING_MEME)
        return view
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(MEMES_FRAGMENT)

        memeDbController.onClickShareBtn = { shareMeme(it) }

        memeDbController.onClickItemListener = { itemClick(it) }

        mSwipeRefreshLayout.setOnRefreshListener(this)

        mMainScreenFragment.toolbar.menu.findItem(R.id.close).setOnMenuItemClickListener(this)

        mMainScreenFragment.onNavigationClickToolbar = {
            (mContext as MainActivity).mainActivityViewModel.getAll().observe((mContext as MainActivity), {listMemeOfFilter ->
                setListToAdapter(listMemeOfFilter)
            })
        }

        mMainScreenFragment.toolbar.search_et.addTextChangedListener {
            (mContext as MainActivity).mainActivityViewModel.getMemeOfFilter(it.toString()).observe((mContext as MainActivity), { listMemeOfFilter ->
                setListToAdapter(listMemeOfFilter)
                infoEmptyListTv.isVisible = memesAdapter.itemCount == 0
            })
        }

        (mContext as MainActivity).mainActivityViewModel.getAll().observe((mContext as MainActivity), {
            setListToAdapter(it)
            infoEmptyListTv.isVisible = memesAdapter.itemCount == 0
        })
    }

    override fun onRefresh() {
        mSwipeRefreshLayout.post {
            (mContext as MainActivity).mainActivityViewModel.requestMeme(App.REFRESH_MEME)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.close) {
            mMainScreenFragment.toolbar.search_et.text = null
        }
        return true
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        with(recyclerView) {
            setHasFixedSize(false)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = memesAdapter
        }
    }

    private fun shareMeme(data: DbMeme) {
        val shareMeme = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data.title)
            putExtra(Intent.EXTRA_STREAM, data.photoUrl)
            type = "image/*"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }, null)
        startActivity(shareMeme)
    }

    private fun itemClick(data: DbMeme) { showMeme.showMeme(data, MEMES_FRAGMENT) }

    private fun setListToAdapter(listMeme: List<DbMeme>?) {
        if (listMeme != null) {
            val memesList = ItemList.create().apply {
                addAll(listMeme, memeDbController)
            }
            memesAdapter.setItems(memesList)
        }
    }

    companion object {
        const val MEMES_FRAGMENT = "MemesFragment"
    }
}