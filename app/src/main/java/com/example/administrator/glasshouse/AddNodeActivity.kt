package com.example.administrator.glasshouse

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.administrator.glasshouse.Adapter.ViewPagerAddNodeAdapter
import kotlinx.android.synthetic.main.activity_add_node.*

class AddNodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_node)

        setSupportActionBar(tbAddNode)
        supportActionBar!!.title = "Add Node"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewpagerAddNode.adapter = ViewPagerAddNodeAdapter(supportFragmentManager)
        tabLayOutAddNode.setupWithViewPager(viewpagerAddNode)
    }
}
