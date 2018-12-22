package com.yizhipin.teacher.schedule.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.yizhipin.R
import com.yizhipin.base.common.AppManager
import com.yizhipin.base.event.HomeIntentEvent
import com.yizhipin.base.ui.activity.BaseActivity
import com.yizhipin.generalizecenter.ui.fragment.GeneralizeFragment
import com.yizhipin.teacher.schedule.ui.fragment.MeFragment
import com.yizhipin.teacher.schedule.ui.fragment.ScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*

class MainActivity : BaseActivity() {

    private val mStack = Stack<Fragment>()
//    private val mGrabFragment by lazy { GrabFragment() }
    private val mScheduleFragment by lazy { ScheduleFragment() }
    private val mGeneralizeFragment by lazy { GeneralizeFragment() }
    private val mMeFragment by lazy { MeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragment()
        initBottomNav()
        changeFragment(0)
        initObserve()
    }

    private fun initFragment() {
        val manager = supportFragmentManager.beginTransaction()
//        manager.add(R.id.mContaier, mGrabFragment)
        manager.add(R.id.mContaier, mScheduleFragment)
        manager.add(R.id.mContaier, mGeneralizeFragment)
        manager.add(R.id.mContaier, mMeFragment)
        manager.commit()

//        mStack.add(mGrabFragment)
        mStack.add(mScheduleFragment)
        mStack.add(mGeneralizeFragment)
        mStack.add(mMeFragment)
    }

    private fun initBottomNav() {
        mBottomNavBar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {
            }

            override fun onTabUnselected(position: Int) {
            }

            override fun onTabSelected(position: Int) {
                changeFragment(position)
            }

        })
    }

    private fun changeFragment(position: Int) {
        val manager = supportFragmentManager.beginTransaction()
        for (fragment in mStack) {
            manager.hide(fragment)
        }
        manager.show(mStack[position])
        manager.commit()
    }

    private fun initObserve() {
        Bus.observe<HomeIntentEvent>()
                .subscribe { t: HomeIntentEvent ->
                    run {

                        if (t.position == 3) {
                            changeFragment(2)
                            mBottomNavBar.selectTab(2)
                        } else {
                            changeFragment(1)
                            mBottomNavBar.selectTab(1)
                        }
                    }
                }.registerInBus(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Bus.unregister(this)
    }

    private var mPressTime: Long = 0
    override fun onBackPressed() {
        val time = System.currentTimeMillis()
        if (time - mPressTime > 2000) {
            toast("再按一次退出App")
            mPressTime = time
        } else {
            AppManager.instance.exitApp(this)
        }
    }
}


