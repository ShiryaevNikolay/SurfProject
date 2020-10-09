package ru.shiryaev.surfproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_memes.view.*
import ru.shiryaev.surfproject.MainActivity
import ru.shiryaev.surfproject.R
import ru.shiryaev.surfproject.adapters.ListMemeAdapter
import ru.shiryaev.surfproject.interfaces.CurrentFragmentListener
import ru.shiryaev.surfproject.services.NetworkService

class MemesFragment : Fragment() {

    private lateinit var currentFragment: CurrentFragmentListener
    private lateinit var memesAdapter: ListMemeAdapter
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        currentFragment = (context as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.get(0) as CurrentFragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memes, container, false)
        memesAdapter = ListMemeAdapter()
        view.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = memesAdapter
        }

        view.info_list_empty.isVisible = memesAdapter.itemCount == 0

        NetworkService
            .getJSONApi()
            .getMemes()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it != null) {
                    Toast.makeText(mContext, "${it[0]}", Toast.LENGTH_LONG).show()
                    memesAdapter.setList(it)
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
    }

    companion object {
        const val NAME_FRAGMENT = "MemesFragment"
    }
}