package com.example.administrator.glasshouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.Adapter.RelayAdapter
import com.example.administrator.glasshouse.Adapter.ViewPagerFarmAdapter
import com.example.administrator.glasshouse.SupportClass.MyApolloClient
import com.example.administrator.glasshouse.Utils.Config
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_screen.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, RelayAdapter.AdapterCallback {

    //Hàm kế thừa interface để xử lý logic trong adapter
    override fun onMethodCallback() {
        val relayName = mSharedPreferences.getString(Config.RelayName, "AaA")
        val relayType = mSharedPreferences.getInt(Config.RELAY_TYPE, 0)
        val relayTag = mSharedPreferences.getInt(Config.RELAY_TAG, 0)
        val relayState = mSharedPreferences.getString(Config.RELAY_STATE, "")
        //Toast.makeText(this@MainActivity, "$relayTag $relayName $relayType $relayState",Toast.LENGTH_SHORT).show()
        // Mutation SetState'
        MyApolloClient.getApolloClient().mutate(
                SetStateRelayMutation.builder().nodeRelayTag(relayTag.toLong())
                        .serviceTag(serviceTag)
                        .typeRelay(relayType.toLong())
                        .state(relayState)
                        .build()
        ).enqueue(object : ApolloCall.Callback<SetStateRelayMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.d("!setState", e.message)
            }

            override fun onResponse(response: Response<SetStateRelayMutation.Data>) {
                Log.d("!setState", "onCompleted")
                this@MainActivity.runOnUiThread {
                    val check = response.data()!!.setStateRelay()!!
                    if (check) {
                        if (relayState == "O") Toast.makeText(this@MainActivity, "Relay ON", Toast.LENGTH_SHORT).show()
                        if (relayState == "F") Toast.makeText(this@MainActivity, "Relay OFF", Toast.LENGTH_SHORT).show()
                    } else Toast.makeText(this@MainActivity, "Something is wrong", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var serviceTag: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup toolbar
        setSupportActionBar(tbMain)

        // thiết lập toggle cho navigation
        val toggle = ActionBarDrawerToggle(this@MainActivity, main_drawer, tbMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        main_drawer.addDrawerListener(toggle)
        toggle.syncState()

        // tạo hàm onClick cho bottom navigation
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //tạo hàm onClick cho navigation
        nav_view.setNavigationItemSelectedListener(this)

        // tạo adapter cho viewpager
        val viewPagerAdapter = ViewPagerFarmAdapter(supportFragmentManager)
        view_pager.adapter = viewPagerAdapter
        viewPagerAdapter.notifyDataSetChanged()
        view_pager.setPagingEnabled(false)

        // Switch to Select Area
        val headerView = nav_view.getHeaderView(0)
        val imgAva = headerView.findViewById<View>(R.id.imgAvaFarm) as CircleImageView
        val txtArea = headerView.findViewById<View>(R.id.txtFarmName) as TextView
        imgAva.setOnClickListener {
            sendToArea()
        }

        // Có thể xóa đi khi đã có Server
        // Truyền dữ liệu từ Activity Area Change sang MainAcitivy rồi từ đó sang Fragment
        // Nhận Id từ Activity Area Change
        val areaIntent = intent
        mSharedPreferences = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        serviceTag = mSharedPreferences.getString(Config.GateId, "")!!
        //val idGate = mSharedPreferences.getString(Config.GateId, "")
        //val name = areaIntent.getStringExtra(Config.FarmName)
        txtArea.text = serviceTag
        supportActionBar!!.title = serviceTag
        // thực hiện các tính năng của floating action button menu
        fabAddRelay.setOnClickListener { sendToAddNodeAcitivity() }
        fabAddSensor.setOnClickListener { sendToAddNodeAcitivity() }


        // Kiểm tra thử kết nối apollo với GraphQL
        // Kết nối thành công
        // Tìm hiểu thêm các chức năng khác của GraphQL
        //getAllUsers()
    }

    private fun sendToAddNodeAcitivity() {
        val intent = Intent(this@MainActivity, AddNodeActivity::class.java)
        startActivity(intent)
    }


    private fun sendToArea() {
        val intent = Intent(this@MainActivity, FarmChangeActivity::class.java)
        startActivity(intent)
    }

    // thiết lập nút quay lại
    override fun onBackPressed() {
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //Tạo menu và thiết lập chức năng
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.itSetting -> {
                sendToSetting()
            }
            R.id.itShareFarm -> {
                sendToShareGate()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itProfile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.itLogout -> {
                val intent = Intent(this@MainActivity, SignInActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navi_overview -> {
                view_pager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navi_relay -> {
                view_pager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navi_chart -> {
                view_pager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navi_noti -> {
                view_pager.currentItem = 3
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun sendToShareGate() {
        val intent = Intent(this@MainActivity, ShareGateActivity::class.java)
        startActivity(intent)
    }

    private fun sendToSetting() {
        val intent = Intent(this@MainActivity, SettingActivity::class.java)
        startActivity(intent)
    }

}
