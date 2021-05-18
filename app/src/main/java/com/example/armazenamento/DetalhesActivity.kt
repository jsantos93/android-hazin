package com.example.armazenamento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detalhes.*
import kotlinx.android.synthetic.main.new_toolbar.*

class DetalhesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)

        val position = intent.getIntExtra(MainActivity.MAIN_ACTIVITY_ARM_DETAIL_POSITION,0)
        val arm = intent.getParcelableExtra<Armazenamento?>(MainActivity.MAIN_ACTIVITY_ARM_DETAIL_PARCELABLE)

        textViewTitle.text = arm?.arm_title
        textViewDetails.text = arm?.arm_content


        new_toolbar.title = arm?.arm_title
        setSupportActionBar(findViewById(R.id.new_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}