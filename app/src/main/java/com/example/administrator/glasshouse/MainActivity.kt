package com.example.administrator.glasshouse

import android.content.Context
import android.content.Intent
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
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.administrator.glasshouse.Adapter.ViewPagerAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_screen.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
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
        val mSharedPreferences = getSharedPreferences(Config.SharedCode, Context.MODE_PRIVATE)
        val idFarm = mSharedPreferences.getInt(Config.FarmId,0)
        val name = areaIntent.getStringExtra(Config.FarmName)
        txtArea.text = name
        supportActionBar!!.title = name
        if (idFarm == 0) {
            txtArea.text = "Farm 1"
            supportActionBar!!.title = "Farm 1"
        }


        // Kiểm tra thử kết nối apollo với GraphQL
        // Kết nối thành công
        // Tìm hiểu thêm các chức năng khác của GraphQL
        //getAllUsers()
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
                val intent = Intent(this@MainActivity,SignInActivity::class.java)
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
}
