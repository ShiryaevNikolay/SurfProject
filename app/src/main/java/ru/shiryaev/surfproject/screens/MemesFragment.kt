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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_memes.view.*
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.models.Meme
import ru.shiryaev.surfproject.services.NetworkService
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        currentFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as CurrentFragmentListener
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memes, container, false)

        viewFragment = view

        mSwipeRefreshLayout = view.mainLayout

        initRecyclerView(view.recyclerView)

        requestMemes(view)
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
            requestMemes(viewFragment)
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        with(recyclerView) {
            setHasFixedSize(false)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = memesAdapter
        }
    }

    private fun requestMemes(view: View) {
        NetworkService
            .getJSONApi(NetworkService.GET_MEMES)
            ?.getMemes()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doFinally {
                view.progressBar.isVisible = false
                view.mainLayout.isRefreshing = false
                view.info_list_empty.isVisible = memesAdapter.itemCount == 0
            }
            ?.subscribe({
                if (it != null) {
                    val memesList = ItemList.create().apply {
                        addAll(it, memeController)
                    }
                    memesAdapter.setItems(memesList)
                }
            }, {
                val snack = Snackbar.make(view.mainLayout, "Произошла ошибка", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                if (context != null) {
                    snack.view.setBackgroundResource(R.drawable.warning_layout)
                    snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                        .setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
                    snack.show()
                }
            })
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