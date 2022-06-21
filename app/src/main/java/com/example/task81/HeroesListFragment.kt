package com.example.task81

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.task81.databinding.FragmentHeroesListBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.*

class HeroesListFragment : Fragment() {

    private lateinit var binding: FragmentHeroesListBinding
    private val fileName = "json.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.swipeRefresh.setOnRefreshListener {
            requestToApi()
            Toast.makeText(context, "fromApi", Toast.LENGTH_LONG).show()
            //todo add diffutil
        }

        binding.buttonAbout.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val fragmentAbout = FragmentAbout()
            fragmentTransaction.replace(R.id.container, fragmentAbout)
            fragmentTransaction.commit()
        }

        val path = requireContext().filesDir.absolutePath + "/" + fileName
        val file = File(path)

        if (file.exists()) {

            val fileInputStream: FileInputStream = requireContext().openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            binding.recyclerView.adapter = Adapter(jsonToHero(stringBuilder.toString()))
            Toast.makeText(requireContext(), "fromFile", Toast.LENGTH_LONG).show()

        } else {

            requestToApi()
            Toast.makeText(requireContext(), "fromApi", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestToApi() {

        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url("https://api.opendota.com/api/herostats").build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "something went wrong check your internet connection",
                        Toast.LENGTH_LONG
                    ).show()
                    
                    binding.swipeRefresh.isRefreshing = false
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val stringResponse = response.body!!.string()
                requireActivity().runOnUiThread {

                    val filename = "json.txt"
                    val outputStream: FileOutputStream =
                        requireActivity().openFileOutput(filename, Context.MODE_PRIVATE)
                    outputStream.write(stringResponse.toByteArray())
                    outputStream.close()

                    binding.recyclerView.adapter = Adapter(jsonToHero(stringResponse))

                    binding.swipeRefresh.isRefreshing = false
                }
            }

        })
    }

    private fun jsonToHero(json: String): ArrayList<Hero> {
        val type = Types.newParameterizedType(List::class.java, Hero::class.java)
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter<ArrayList<Hero>>(type)
        return adapter.fromJson(json)!!
    }

}