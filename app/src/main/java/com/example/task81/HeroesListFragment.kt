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
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.*

class HeroesListFragment : Fragment() {

    private val fileName = "json.txt"
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_heroes_list, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefresh.setOnRefreshListener {
            requestToApi()
            Toast.makeText(context, "fromApi", Toast.LENGTH_LONG).show()
            //todo add diffutil
        }

        val about = view.findViewById<Button>(R.id.buttonAbout)

        about.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val fragmentAbout = FragmentAbout()
            fragmentTransaction.replace(R.id.container, fragmentAbout)
            fragmentTransaction.commit()
        }

        val path = requireContext().filesDir.absolutePath + "/" + fileName
        val file = File(path)

        if (file.exists()){

            val fileInputStream: FileInputStream = requireContext().openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            recyclerView.adapter = Adapter(jsonToHero(stringBuilder.toString()))
            Toast.makeText(requireContext(), "fromFile", Toast.LENGTH_LONG).show()

        } else {

            requestToApi()
            Toast.makeText(requireContext(), "fromApi", Toast.LENGTH_LONG).show()
        }

        return view
    }

    private fun requestToApi(){

        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url("https://api.opendota.com/api/herostats").build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val stringResponse = response.body!!.string()
                requireActivity().runOnUiThread{

                    val filename = "json.txt"
                    val outputStream: FileOutputStream = requireActivity().openFileOutput(filename, Context.MODE_PRIVATE)
                    outputStream.write(stringResponse.toByteArray())
                    outputStream.close()

                    recyclerView.adapter = Adapter(jsonToHero(stringResponse))

                    swipeRefresh.isRefreshing = false
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