package com.kumestudio.notify.ui.main

//import androidx.lifecycle.ViewModelProviders

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.room.Room
import com.kumestudio.notify.R
import com.kumestudio.notify.adapter.main.NotifyListAdapter
import com.kumestudio.notify.db.AppDatabase
import com.kumestudio.notify.db.NotificationData
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private  lateinit var  viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    lateinit var db : AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //view model
        viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(Application())
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.listData.observe(viewLifecycleOwner, getNotifyListObserver())

        //DB
        db = Room.databaseBuilder(requireContext(),AppDatabase::class.java, "database")
            .build()

        //알림 리스트 어뎁터
        notificationList.adapter =
            NotifyListAdapter(MutableLiveData<List<NotificationData>>(),requireContext())

        //알림 리스트 구분선
        val decoration = DividerItemDecoration(requireContext(),VERTICAL)
        notificationList.addItemDecoration(decoration)

        //알림 리스트에 초기값 넣기
        CoroutineScope(Dispatchers.IO).launch {
            val notificationAll = db.notificationDao().getAll()
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.listData.value = notificationAll.toMutableList()
            }
        }
    }

    /**
     * 알림 리스트 옵저버
     */
    private fun getNotifyListObserver() : Observer<List<NotificationData>>{
        return Observer { arrData->
            val listData = MutableLiveData<List<NotificationData>>()
            val adapter = (notificationList.adapter as NotifyListAdapter)

            listData.value = arrData.reversed()
            adapter.data = listData
            adapter.notifyDataSetChanged()
        }
    }
}

