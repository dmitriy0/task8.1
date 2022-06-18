package com.example.task81

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import coil.load

class HeroInfoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hero, container, false)

        val image = view.findViewById<ImageView>(R.id.image)
        val name = view.findViewById<TextView>(R.id.name)
        val health = view.findViewById<TextView>(R.id.text_health)
        val mana = view.findViewById<TextView>(R.id.text_mana)
        val attackType = view.findViewById<TextView>(R.id.attack_type)
        val textRoles = view.findViewById<TextView>(R.id.roles)

        image.load(requireArguments().getString("imageUrl"))
        name.text = requireArguments().getString("name")
        health.text = requireArguments().getString("health")
        mana.text = requireArguments().getString("mana")
        attackType.text = "Attack type: ${requireArguments().getString("attackType")}"
        val roles = requireArguments().getStringArray("roles")
        var stringRoles = ""
        for (i in roles!!.indices){
            stringRoles += "${roles[i]} "
        }
        textRoles.text = "Roles: $stringRoles"

        return view
    }
}