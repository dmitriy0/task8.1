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
import com.example.task81.databinding.FragmentHeroBinding

class HeroInfoFragment : Fragment() {

    private lateinit var binding: FragmentHeroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roles = requireArguments().getStringArray("roles")
        var stringRoles = ""
        for (i in roles!!.indices) {
            stringRoles += "${roles[i]} "
        }

        with(binding) {
            image.load(requireArguments().getString("imageUrl"))
            name.text = requireArguments().getString("name")
            textHealth.text = requireArguments().getString("health")
            textMana.text = requireArguments().getString("mana")
            attackType.text = "Attack type: ${requireArguments().getString("attackType")}"
            textRoles.text = "Roles: $stringRoles"
        }
    }

}