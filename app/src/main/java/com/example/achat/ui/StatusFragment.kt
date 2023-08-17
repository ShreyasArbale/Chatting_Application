package com.example.achat.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.achat.R
import com.example.achat.adapter.AppProductImageAdapter
import com.example.achat.databinding.FragmentStatusBinding
import com.example.achat.model.AddProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class StatusFragment : Fragment() {

    lateinit var binding: FragmentStatusBinding
    private lateinit var list: ArrayList<Uri>
    private lateinit var adapter: AppProductImageAdapter
    private lateinit var dialog: Dialog
    private lateinit var listImages: ArrayList<String>

    private var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode  == Activity.RESULT_OK){
            val imageUrl = it.data!!.data
            list.add(imageUrl!!)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentStatusBinding.inflate(layoutInflater)
        list = ArrayList()
        listImages = ArrayList()

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchProductActivity.launch(intent)
        }

        adapter = AppProductImageAdapter(list)
        binding.productImgRecyclerView.adapter = adapter

        binding.submitProductBtn.setOnClickListener{
            validateData()
        }

        return binding.root
    }

    private fun validateData() {
        if (list.size <1){
            Toast.makeText(requireContext(), "Please select product images", Toast.LENGTH_SHORT).show()
        }else{
            uploadProductImage()
        }
    }

    private var i = 0

    private fun uploadProductImage() {
        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    listImages.add(image!!.toString())
                    dialog.dismiss()
                    if (list.size == listImages.size){
                        storeData()
                    }else{
                        i += 1
                        uploadProductImage()
                    }
                }
                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong with storage",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData() {
        val db = Firebase.firestore.collection("products")
        val key = db.document().id

        val data = AddProductModel(
            key,
            listImages
        )

        db.document(key).set(data).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(requireContext(),"Product Added", Toast.LENGTH_SHORT).show()

        }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"Something went wrong", Toast.LENGTH_SHORT).show()

            }
    }

}