package ru.shiryaev.surfproject.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_memes.*
import kotlinx.android.synthetic.main.fragment_memes.view.*
import kotlinx.android.synthetic.main.meme_item.*
import kotlinx.android.synthetic.main.meme_item.view.*
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.adapters.ListMemeAdapter
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.services.NetworkService
import ru.shiryaev.surfproject.utils.MemeItemController
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList

class MemesFragment : Fragment(), View.OnClickListener {

    private lateinit var currentFragment: CurrentFragmentListener
    private lateinit var mContext: Context
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

        initRecyclerView(view.recyclerView)

        view.info_list_empty.isVisible = memesAdapter.itemCount == 0

        NetworkService
            .getJSONApi(NetworkService.GET_MEMES)
            ?.getMemes()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({
                if (it != null) {
                    Toast.makeText(mContext, "${it[0]}", Toast.LENGTH_LONG).show()
//                    memesAdapter.setList(it)
                    val memesList = ItemList.create().apply {
                        addAll(it, memeController)
                    }
                    memesAdapter.setItems(memesList)
                    view.info_list_empty.isVisible = memesAdapter.itemCount == 0
                }
            }, {
                Toast.makeText(mContext, "Что-то пошло не так", Toast.LENGTH_LONG).show()
            })
        return view
    }

    override fun onResume() {
        super.onResume()
        currentFragment.currentFragment(NAME_FRAGMENT)

//        btn_favorite.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_favorite -> onCLickFavorite(v)
        }
    }

    private fun onCLickFavorite(v: View) {
        Handler().postDelayed({
            //
        }, 100)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = memesAdapter
        }
    }

    companion object {
        const val NAME_FRAGMENT = "MemesFragment"
    }
}