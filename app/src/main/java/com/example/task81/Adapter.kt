/*package com.example.task81

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import coil.load


class Adapter(private val data: ArrayList<Hero>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.icon)!!
        val name = view.findViewById<TextView>(R.id.name)!!
        val health = view.findViewById<TextView>(R.id.textHealth)!!
        val mana = view.findViewById<TextView>(R.id.textMana)!!
        val card = view.findViewById<CardView>(R.id.card)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hero = data[position]
        holder.name.text = hero.name
        holder.health.text = hero.health.toString()
        holder.mana.text = hero.mana.toString()
        holder.icon.load("https://api.opendota.com${hero.iconUrl}")
        holder.card.setOnClickListener {
            val activity = it.context as AppCompatActivity?

            val heroFragment = HeroInfoFragment()
            val bundle = Bundle()
            bundle.putString("name", hero.name)
            bundle.putString("imageUrl", "https://api.opendota.com${hero.imageUrl}")
            bundle.putString("mana", hero.mana.toString())
            bundle.putString("health", hero.health.toString())
            bundle.putStringArray("roles", hero.roles)
            bundle.putString("attackType", hero.attackType)
            heroFragment.arguments = bundle

            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, heroFragment).addToBackStack("")
                .commit()
        }
    }

    override fun getItemCount(): Int = data.size
} */

package com.example.task81

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.task81.databinding.RecyclerViewItemBinding


class Adapter(private val data: ArrayList<Hero>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerViewItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hero = data[position]
        with(holder.binding) {
            name.text = hero.name
            textHealth.text = hero.health.toString()
            textMana.text = hero.mana.toString()
            icon.load("https://api.opendota.com${hero.iconUrl}")
            card.setOnClickListener {
                val activity = it.context as AppCompatActivity?
                val heroInfoFragment = HeroInfoFragment()
                val bundle = Bundle()
                bundle.putString("name", hero.name)
                bundle.putString("imageUrl", "https://api.opendota.com${hero.imageUrl}")
                bundle.putString("mana", hero.mana.toString())
                bundle.putString("health", hero.health.toString())
                bundle.putStringArray("roles", hero.roles)
                bundle.putString("attackType", hero.attackType)
                heroInfoFragment.arguments = bundle

                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, heroInfoFragment).addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun getItemCount(): Int = data.size
}