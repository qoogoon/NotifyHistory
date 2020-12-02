package com.kumestudio.notify.ui.main

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.room.Room
import com.kumestudio.notify.Data
import com.kumestudio.notify.R
import com.kumestudio.notify.adapter.main.NotifyListAdapter
import com.kumestudio.notify.db.AppDatabase
import com.kumestudio.notify.db.NotificationData
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
        viewModel.listData.observe(viewLifecycleOwner, getNotifyListObserver())
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
                viewModel.listData.value = notificationAll.toMutableList()
            }
        }
    }

    /**
     * 알림 리스트 옵저버
     * @return Observer
     */
    private fun getNotifyListObserver() : Observer<List<NotificationData>>{
        return Observer { arrData->
            val adapter = (notificationList.adapter as NotifyListAdapter)

            val groupList : MutableList<Data.NotificationGroup> = mutableListOf()
            var tmpData : NotificationData? = null
            var divHour : Int? = null
            var divDay : Int? = null
            for(data in arrData){
                val isChildData = tmpData != null && data.packageName == tmpData.packageName &&
                        data.title == tmpData.title
                if(isChildData){
                    groupList.last().childNotifications!!.add(data)
                }else{
                    if(divDay != getDay(data.`when`)){
                        groupList.add(Data.NotificationGroup(Data.DATE, data.`when`))
                        divDay = getDay(data.`when`)
                    }

                    if(divHour != getHour(data.`when`)){
                        groupList.add(Data.NotificationGroup(Data.TIME, data.`when`))
                        divHour = getHour(data.`when`)
                    }

                    val insertGroup = Data.NotificationGroup(Data.NOTIFICATION, data.`when`, mutableListOf(data))
                    groupList.add(insertGroup)
                    tmpData = data
                }
            }

            adapter.data = groupList
            adapter.notifyDataSetChanged()
        }
    }

    private fun getHour(dateTime : Long): Int{
        val firstDataTime = Calendar.getInstance()
        firstDataTime.time = Date(dateTime)
        return firstDataTime.get(Calendar.HOUR_OF_DAY)
    }

    private fun getDay(dateTime : Long): Int{
        val firstDataTime = Calendar.getInstance()
        firstDataTime.time = Date(dateTime)
        return firstDataTime.get(Calendar.DAY_OF_MONTH)
    }
}

