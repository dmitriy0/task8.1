package com.example.task81

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load


class Adapter(private val data: ArrayList<Hero>): RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val icon = view.findViewById<ImageView>(R.id.icon)!!
        val name = view.findViewById<TextView>(R.id.name)!!
        val health = view.findViewById<TextView>(R.id.text_health)!!
        val mana = view.findViewById<TextView>(R.id.text_mana)!!
        val card = view.findViewById<CardView>(R.id.card)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hero = data[position]
        holder.name.text = hero.name
        holder.health.text = hero.health.toString()
        holder.mana.text = hero.mana.toString()
        holder.icon.load("https://api.opendota.com${hero.iconUrl}")
        holder.card.setOnClickListener{
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

    override fun getItemCount(): Int = data.size
}