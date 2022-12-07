package com.hand.comeeatme.util.widget.adapter.user
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.comeeatme.R
import com.hand.comeeatme.data.response.post.Content
import com.hand.comeeatme.databinding.LayoutAlbumImageBinding
import com.hand.comeeatme.view.main.home.post.DetailPostFragment

class UserGridAdapter(
    private val items: List<Content>,
    private val context: Context,
) : RecyclerView.Adapter<UserGridAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutAlbumImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(context, item)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(binding: LayoutAlbumImageBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.ivImage

        fun bind(context: Context, item: Content) {
            Glide.with(context)
                .load(item.imageUrls[0])
                .into(image)

            itemView.setOnClickListener {
                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val ft: FragmentTransaction = manager.beginTransaction()

                ft.add(R.id.fg_MainContainer, DetailPostFragment.newInstance(item.id), DetailPostFragment.TAG)
                ft.addToBackStack(DetailPostFragment.TAG)
                ft.commitAllowingStateLoss()
            }
        }

    }


}