package com.bb.moneyburndown.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bb.moneyburndown.R
import com.bb.moneyburndown.databinding.ActivityMainBinding
import com.bb.moneyburndown.dialog.AddChangeDialog
import com.bb.moneyburndown.dialog.SetLimitDialog
import com.bb.moneyburndown.view.About
import com.bb.moneyburndown.view.AddChange
import com.bb.moneyburndown.view.SetLimit
import com.bb.moneyburndown.viewmodel.BurndownViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: BurndownViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[BurndownViewModel::class.java]
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.eventLiveData.observe(this, Observer {
            when (it) {
                SetLimit -> SetLimitDialog().show(supportFragmentManager, SetLimitDialog::class.java.simpleName)
                AddChange -> AddChangeDialog().show(supportFragmentManager, AddChangeDialog::class.java.simpleName)
                About -> startActivity(Intent(this, AboutActivity::class.java))
            }
        })

        setContentView(binding.root)
        setSupportActionBar(toolbar)
    }

    override fun onStart() {
        super.onStart()

        burndown_view.update()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reset -> {
                viewModel.resetLimit()
                return true
            }
            R.id.action_about -> {
                viewModel.showAbout()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
