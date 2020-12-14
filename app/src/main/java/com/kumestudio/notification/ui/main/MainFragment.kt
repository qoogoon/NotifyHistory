package com.kumestudio.notification.ui.main

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.room.Room
import com.kumestudio.notification.Data
import com.kumestudio.notification.R
import com.kumestudio.notification.adapter.main.NotifyListAdapter
import com.kumestudio.notification.constant.Constant
import com.kumestudio.notification.db.AppDatabase
import com.kumestudio.notification.db.NotificationData
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * 데이터 바인딩하여 데이터를 RecyclerListView에 가시화하는 Fragment
 */

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        var viewModel: MainViewModel? = null
    }

    private  lateinit var  viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    lateinit var db : AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        db = Room.databaseBuilder(requireContext(),AppDatabase::class.java, "database").build()
        initAlarmList()
        initNotifyData()
    }

    /**
     * ViewModel 초기설정
     */
    private fun initViewModel(){
        viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(Application())
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel!!.listData.observe(viewLifecycleOwner, getNotifyListObserver())
    }

    /**
     * 알림 리스트 초기설정
     */
    private fun initAlarmList(){
        notificationList.adapter =
                NotifyListAdapter(arrayListOf(),requireContext())

        val decoration = DividerItemDecoration(requireContext(),VERTICAL)
        notificationList.addItemDecoration(decoration)
    }

    /**
     * 알림 데이터 초기 삽입
     */
    private fun initNotifyData(){
        CoroutineScope(Dispatchers.IO).launch {
            val notificationAll = db.notificationDao().getAll()
            CoroutineScope(Dispatchers.Main).launch {
                viewModel!!.listData.value = notificationAll.toMutableList()
            }
        }
    }

    /**
     * 알림 리스트 옵저버
     * @return Observer
     */
    private fun getNotifyListObserver() : Observer<List<NotificationData>>{
        return Observer { arrData ->
            val arrDataByDateRange = arrData.filter { data -> data.`when` > (Date().time - Constant.dayMs * Constant.dateToList)}
            val adapter = (notificationList.adapter as NotifyListAdapter)
            val groupList : MutableList<Data.NotificationGroup> = mutableListOf()
            var tmpData : NotificationData? = null
            var divHour : Int? = null
            var divDay : Int? = null
            groupList.add(Data.NotificationGroup(Data.HEADER))
            for(data in arrDataByDateRange){
                val isChildData = tmpData != null && data.packageName == tmpData.packageName &&
                        data.title == tmpData.title
                if(isChildData){
                    groupList.last().childNotifications!!.add(data)
                }else{
                    if(divDay != Constant.getDay(data.`when`)){
                        groupList.add(Data.NotificationGroup(Data.DATE, data.`when`))
                        divDay = Constant.getDay(data.`when`)
                    }

                    if(divHour != Constant.getHour(data.`when`)){
                        groupList.add(Data.NotificationGroup(Data.TIME, data.`when`))
                        divHour = Constant.getHour(data.`when`)
                    }

                    val insertGroup = Data.NotificationGroup(Data.NOTIFICATION, data.`when`, false, mutableListOf(data))
                    groupList.add(insertGroup)
                    tmpData = data
                }
            }

            adapter.data = groupList
            adapter.notifyDataSetChanged()
        }
    }
}

