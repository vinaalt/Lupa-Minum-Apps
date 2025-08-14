package edu.unikom.lupaminum.adapter

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import edu.unikom.lupaminum.R
import edu.unikom.lupaminum.model.DataItem

class UserAdapter(private val users:MutableList<DataItem>)
    : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent,  false)

        return ListViewHolder(view)
    }

//    fun addUser(newUsers: DataItem){
//        users.add(newUsers)
//        notifyItemInserted(users.lastIndex)
//    }

    fun updateData(newUsers: List<DataItem>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var user = users[position]

        Log.d("DATA", "${user.firstName}")

        holder.tvUsername.text = "${user.firstName} ${user.lastName}"
        holder.tvEmail.text = "${user.email}"

        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(RequestOptions().override(88,88).placeholder(R.drawable.ic_avatar))
            .transform(CircleCrop())
            .into(holder.ivAvatar)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUsername: TextView = itemView.findViewById(R.id.itemName)
        var tvEmail: TextView = itemView.findViewById(R.id.itemEmail)
        var ivAvatar: ImageView = itemView.findViewById(R.id.itemAvatar)
    }

//    override fun getItemCount(): Int = users.size

    override fun getItemCount(): Int {
        Log.d("DATA", "Ukuran list: ${users.size}")
        return users.size
    }
}