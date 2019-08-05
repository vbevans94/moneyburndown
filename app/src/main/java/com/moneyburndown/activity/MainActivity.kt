package com.moneyburndown.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.moneyburndown.R
import com.moneyburndown.databinding.ActivityMainBinding
import com.moneyburndown.dialog.AddChangeDialog
import com.moneyburndown.dialog.SetLimitDialog
import com.moneyburndown.view.About
import com.moneyburndown.view.AddChange
import com.moneyburndown.view.SetLimit
import com.moneyburndown.viewmodel.BurndownViewModel
import com.moneyburndown.viewmodel.BurndownViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModelFactory: BurndownViewModelFactory by inject()
    private lateinit var viewModel: BurndownViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[BurndownViewModel::class.java]
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
