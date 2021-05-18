package com.example.armazenamento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.armazenamento_card.*
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
                val content = armContent.text.toString()
                val isJetpackOn = checkBoxJetPack.isChecked
                if(internalStorage){
                    val newArm = Armazenamento(title, content, radio_internal.text.toString())
                    writeInternalStorage(null, newArm, isJetpackOn)
                }
                else if(externalStorage){
                    val newArm = Armazenamento(armTitle.text.toString(), armContent.text.toString(), radio_external.text.toString())
                    writeExternalStorage(null, newArm, isJetpackOn)
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
    }

    override fun onItemClick(position: Int) {
        var armTarget = armList[position]
        val detailIntent = Intent(this, DetalhesActivity::class.java)
        detailIntent.putExtra(MAIN_ACTIVITY_ARM_DETAIL_PARCELABLE, armTarget)
        startActivityForResult(detailIntent, MAIN_ACTIVITY_ARM_DETAIL_CODE)
    }


    private fun writeInternalStorage(view: View?, armazenamento: Armazenamento, isJetpackOn: Boolean) {
        try {
            val file = File(filesDir, armazenamento.arm_title)
            file.createNewFile()
            if(isJetpackOn){
                insertItem(null, armazenamento)
                val encryptedFile = encryptFile(file)
                encryptedFile?.openFileOutput().use { writer ->
                    writer?.write(armazenamento.arm_content.toByteArray())
                }
            }else{
                insertItem(null, armazenamento)
                openFileOutput(armazenamento.arm_title, MODE_PRIVATE).use {
                    it.write(armazenamento.arm_content.toByteArray())
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun writeExternalStorage(view: View?, armazenamento: Armazenamento, isJetpackOn: Boolean) {
        try {
            if(!isExternalStorageWritable()){
                return
            }
            val fileDir = File(getExternalFilesDir(null), armazenamento.arm_title)
            if(isJetpackOn){
                insertItem(null, armazenamento)
                val encryptedFile = encryptFile(fileDir)
                encryptedFile?.openFileOutput().use { writer ->
                    writer?.write(armazenamento.arm_content.toByteArray())
                }
            }else{
                insertItem(null, armazenamento)
                fileDir.createNewFile()
                openFileOutput(armazenamento.arm_title, MODE_PRIVATE).use {
                    it.write(armazenamento.arm_content.toByteArray())
                }
            }

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
//    fun isExternalStorageReadable(): Boolean {
//        return Environment.getExternalStorageState() in
//                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
//    }

    private fun deleteItem(view: View?, position: Int){
        armList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    override fun onDeleteItemClick(position: Int) {
        val armTarget = armList[position]
        deleteItem(null, position)
        if(armTarget.storage_type == radio_internal.text.toString()){
            this.deleteFile(armTarget.arm_title)
        }else{
            val externalFile = File(this.getExternalFilesDir(null), armTarget.arm_title)
            externalFile.delete()
        }
    }

    private fun encryptFile(file: File): EncryptedFile? {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        return EncryptedFile.Builder(
            file,
            this,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }
}

