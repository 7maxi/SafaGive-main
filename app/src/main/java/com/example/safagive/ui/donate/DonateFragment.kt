package com.example.safagive.ui.donate

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.safagive.Login
import com.example.safagive.VistaMainFragment
import com.example.safagive.databinding.FragmentDonateBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DonateFragment : Fragment() {

    private var _binding: FragmentDonateBinding? = null

    private val binding get() = _binding!!

    private lateinit var addImageButton1Launcher: ActivityResultLauncher<Intent>
    private lateinit var addImageButton2Launcher: ActivityResultLauncher<Intent>
    private lateinit var addImageButton3Launcher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DonateViewModel::class.java)

        _binding = FragmentDonateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.buttonDonate.setOnClickListener{
            val newDonation = mapOf(
                "categoria" to binding.ddlCategory.editText?.text.toString(),
                "descripcion" to binding.textInputDescription.editText?.text.toString(),
                "fotos" to listOf("foto1", "foto2"),
                "titulo" to binding.textTitleProduct.editText?.text.toString(),
                "usuario" to Login.email
            )
            donate(newDonation)
        }
        setupImageButtons()
        setupCategoriesDdl()

        return root
    }

    private fun setupImageButtons() {
        addImageButton1Launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleImageResult(result, binding.addImageButton1)
        }
        addImageButton2Launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleImageResult(result, binding.addImageButton2)
        }
        addImageButton3Launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleImageResult(result, binding.addImageButton3)
        }

        binding.addImageButton1.setOnClickListener {
            openGallery(addImageButton1Launcher)
        }

        binding.addImageButton2.setOnClickListener {
            openGallery(addImageButton2Launcher)
        }

        binding.addImageButton3.setOnClickListener {
            openGallery(addImageButton3Launcher)
        }
    }

    private fun openGallery(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }

    private fun handleImageResult(result: androidx.activity.result.ActivityResult, button: FloatingActionButton) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            if (uri != null) {
                val imageView = ImageView(requireContext())
                imageView.layoutParams = button.layoutParams
                imageView.setImageURI(uri)
                val parent = button.parent as ViewGroup
                val index = parent.indexOfChild(button)
                parent.removeView(button)
                parent.addView(imageView, index)
            }
        }
    }
    private fun setupCategoriesDdl() {
        val categories = listOf("Ropa y accesorios", "Libros", "Material escolar", "Tecnología")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, categories)
        val autoCompleteTextView = binding.ddlCategory.editText as? AutoCompleteTextView
        autoCompleteTextView?.setAdapter(adapter)
    }
    private fun donate(producto: Map<String, Any?>){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://safagive-44998-default-rtdb.europe-west1.firebasedatabase.app/")
        val productosRef: DatabaseReference = database.getReference("productos")
        val newProductRef: DatabaseReference = productosRef.push()
        newProductRef.setValue(producto)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "¡Gracias por tu donación", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(context, "No se pudo donar...inténtalo de nuevo", Toast.LENGTH_LONG)
                        .show()
                }
            }
        val intentG = Intent(context, VistaMainFragment::class.java)
        startActivity(intentG)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}