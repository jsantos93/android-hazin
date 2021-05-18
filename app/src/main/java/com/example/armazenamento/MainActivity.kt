package com.example.armazenamento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity(), ArmazenamentoAdapter.OnItemClickListener {

    companion object{

        const val MAIN_ACTIVITY_ARM_DETAIL_CODE = 1
        const val MAIN_ACTIVITY_ARM_DETAIL_PARCELABLE = "MAIN_ACTIVITY_ARM_DETAIL_PARCELABLE"
        const val MAIN_ACTIVITY_ARM_DETAIL_POSITION = "MAIN_ACTIVITY_ARM_DETAIL_POSITION"

    }
    private var internalStorage = false
    private var externalStorage = false
    private var armList = ArrayList<Armazenamento>()
    private var adapter = ArmazenamentoAdapter(armList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arm_recycler_view.adapter = adapter
        arm_recycler_view.layoutManager = LinearLayoutManager(this)
        arm_recycler_view.setHasFixedSize(true)

        createButton.setOnClickListener(){
            var title = armTitle.text.toString()
            if(title.isNotBlank()){
                Log.i("MEU", title)
//                val title = armTitle.text.toString()
                val content = armContent.text.toString()

                if(internalStorage){
                    var fon = radio_internal.text.toString()
                    Log.i("MEU", fon)
                    val newArm = Armazenamento(title, content, radio_internal.text.toString())
                    writeInternalStorage(null, newArm)
                }
                else if(externalStorage){
                    val newArm = Armazenamento(armTitle.text.toString(), armContent.text.toString(), radio_external.text.toString())
                    writeExternalStorage(null, newArm)
                }
            }
        }

    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_internal ->
                    if (checked) {
                        internalStorage = true
                        externalStorage = false
                    }
                R.id.radio_external ->
                    if (checked) {
                        internalStorage = false
                        externalStorage = true
                    }
            }
        }
    }

    private fun insertItem(view: View?, armazenamento: Armazenamento){
        val index = 0
        armList.add(index, armazenamento)
        adapter.notifyItemInserted(index)
        arm_recycler_view.scrollToPosition(0)
        Log.i("MEU", "achou")
    }

    override fun onItemClick(position: Int) {
        var armTarget = armList[position]
        val detailIntent = Intent(this, DetalhesActivity::class.java)
        detailIntent.putExtra(MAIN_ACTIVITY_ARM_DETAIL_POSITION, position)
        detailIntent.putExtra(MAIN_ACTIVITY_ARM_DETAIL_PARCELABLE, armTarget)
        startActivityForResult(detailIntent, MAIN_ACTIVITY_ARM_DETAIL_CODE)
    }


    private fun writeInternalStorage(view: View?, armazenamento: Armazenamento) {
        try {
            val file = File(filesDir, armazenamento.arm_title)
            Log.i("MEU", "InternalStorage")
            file.createNewFile()
            openFileOutput(armazenamento.arm_title, MODE_PRIVATE).use {
                it.write(armazenamento.arm_content.toByteArray())
            }

            insertItem(null, armazenamento)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun writeExternalStorage(view: View?, armazenamento: Armazenamento) {
        try {
            if(!isExternalStorageWritable()){
                return
            }
            val fileDir = File(getExternalFilesDir(null), armazenamento.arm_title)
            fileDir.createNewFile()
            openFileOutput(armazenamento.arm_title, MODE_PRIVATE).use {
                it.write(armazenamento.arm_content.toByteArray())
            }
            insertItem(null, armazenamento)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Checks if a volume containing external storage is available
    // for read and write.
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // Checks if a volume containing external storage is available to at least read.
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }
}