package ru.shiryaev.surfproject.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_memes.view.*
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.models.Meme
import ru.shiryaev.surfproject.utils.App
import ru.shiryaev.surfproject.utils.MemeItemController
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList

class MemesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var currentFragment: CurrentFragmentListener
    private lateinit var mContext: Context
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var viewFragment: View
    private val memesAdapter = EasyAdapter()
    private val memeController = MemeItemController()

    private lateinit var listMeme: LiveData<List<Meme>>
    private lateinit var progressBarState: LiveData<Boolean>
    private lateinit var listEmptyState: LiveData<Boolean>
    private lateinit var refreshState: LiveData<Boolean>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        progressBarState = (mContext as MainActivity).mainActivityViewModel.progressBarState
        listEmptyState = (mContext as MainActivity).mainActivityViewModel.listEmptyState
        listMeme = (mContext as MainActivity).mainActivityViewModel.allMeme
        refreshState = (mContext as MainActivity).mainActivityViewModel.refreshState
        currentFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as CurrentFragmentListener
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memes, container, false)

        progressBarState.observe((mContext as MainActivity), { view.progressBar.isVisible = it })

        (mContext as MainActivity).snackbarShow = {
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

        listMeme.observe((mContext as MainActivity), {
            if (it != null) {
                val memesList = ItemList.create().apply {
                    addAll(it, memeController)
                }
                memesAdapter.setItems(memesList)
            }
            view.info_list_empty.isVisible = memesAdapter.itemCount == 0
        })

        viewFragment = view

        mSwipeRefreshLayout = view.mainLayout

        initRecyclerView(view.recyclerView)

        (mContext as MainActivity).mainActivityViewModel.requestMeme(App.LOADING_MEME)
        return view
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(NAME_FRAGMENT)

        memeController.onClickShareBtn = { shareMeme(it) }

        memeController.onClickItemListener = { itemClick(it) }

        mSwipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        mSwipeRefreshLayout.post {
            (mContext as MainActivity).mainActivityViewModel.requestMeme(App.REFRESH_MEME)
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        with(recyclerView) {
            setHasFixedSize(false)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = memesAdapter
        }
    }

    private fun shareMeme(data: Meme) {
        val shareMeme = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data.title)
            putExtra(Intent.EXTRA_STREAM, data.photoUrl)
            type = "image/*"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }, null)
        startActivity(shareMeme)
    }

    private fun itemClick(data: Meme) {
        // Click on item
    }

    companion object {
        const val NAME_FRAGMENT = "MemesFragment"
    }
}