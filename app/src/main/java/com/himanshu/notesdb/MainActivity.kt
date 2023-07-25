package com.himanshu.notesdb

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.himanshu.notesdb.databinding.ActivityMainBinding
import com.himanshu.notesdb.databinding.CustomLayoutBinding

class MainActivity : AppCompatActivity(),ListClickInterface {
    var arrayList = arrayListOf<NotesEntity>()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var binding: ActivityMainBinding
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var notesRoomDB : NotesRoomDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerViewAdapter = RecyclerViewAdapter(arrayList,this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        gridLayoutManager = GridLayoutManager(this,2)
        binding.recyclerView.layoutManager= layoutManager
        binding.recyclerView.adapter = recyclerViewAdapter


        notesRoomDB = NotesRoomDB.getNotesDatabase(this)

        //on create me data show krwane ke liye
        getNotes()

        binding.btnFab.setOnClickListener {
            var dialog= Dialog(this)
            var dialogBinding = CustomLayoutBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.setCancelable(false)
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etTitle.text.toString().isNullOrEmpty()) {
                    dialogBinding.etTitle.error="Please Enter your name"
                }
                else if (dialogBinding.etDescription.text.toString().isNullOrEmpty()) {
                    dialogBinding.etDescription.error="Please Enter the roll no"
                }
                else {
                    //arrayList.add(NotesEntity(dialogBinding.etTitle.text.toString(),dialogBinding.etDescription.text.toString()))
                    recyclerViewAdapter.notifyDataSetChanged()
                    Toast.makeText(this, "Added successful", Toast.LENGTH_SHORT).show()
                    class Insert : AsyncTask<Void, Void, Void>(){

                        //for background heavy tasks
                        override fun doInBackground(vararg p0: Void?): Void? {
                            notesRoomDB.notesDao()
                                .insertNotes(NotesEntity(title = "${dialogBinding.etTitle.text}",description = "${dialogBinding.etDescription.text}"))
                            return null
                        }
                        override fun onPreExecute() {
                            super.onPreExecute()
                        }
                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            getNotes()
                        }

                        override fun onProgressUpdate(vararg values: Void?) {
                            super.onProgressUpdate(*values)
                        }
                    }

                    Insert().execute()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    fun getNotes(){
        arrayList.clear()
        class Retrieve : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
                arrayList.addAll(notesRoomDB.notesDao().getNotes())
                notesRoomDB.notesDao()
                    .getNotes()
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
        Retrieve().execute()
    }


    override fun onClickUpdate(position: Int) {

        class Update: AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
                notesRoomDB.notesDao().updateNotes(arrayList[position])
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                recyclerViewAdapter.notifyDataSetChanged()
                getNotes()
            }
        }
    }
//        var dialog=Dialog(this)
//        var dialogBinding = CustomLayoutBinding.inflate(layoutInflater)
//        dialog.setCancelable(false)
//        dialog.setContentView(dialogBinding.root)
//        dialogBinding.btnAdd.setText("Update")
//        dialogBinding.tvTitle.setText("Update name: ${arrayList[position].title}")
//        dialogBinding.etTitle.setText("${arrayList[position].title}")
//        dialogBinding.tvDescription.setText("Update roll no: ${arrayList[position].description}")
//        dialogBinding.etDescription.setText("${arrayList[position].description}")
//        dialogBinding.btnAdd.setOnClickListener {
//            if (dialogBinding.etTitle.text.toString().isNullOrEmpty()) {
//                dialogBinding.etTitle.error="Enter your name"
//            } else if (dialogBinding.etDescription.text.toString().isNullOrEmpty()) {
//                dialogBinding.etDescription.error="Enter your roll no"
//            } else {
//                //arrayList.set(position,StudentEntity(dialogBinding.etName.text.toString(),dialogBinding.etRollNo.text.toString().toInt()))
//                recyclerViewAdapter.notifyDataSetChanged()
//                Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
//                dialog.dismiss()
//            }
//        }
//        dialog.show()
//        recyclerViewAdapter.notifyDataSetChanged()

    override fun onClickDelete(position: Int) {

        class Delete : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                notesRoomDB.notesDao().deleteNotes(arrayList[position])
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                recyclerViewAdapter.notifyDataSetChanged()
                getNotes()
            }
        }
        Delete().execute()
    }
}