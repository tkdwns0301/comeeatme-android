
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.data.SampleImage
import com.hand.comeeatme.databinding.LayoutAlbumImageBinding

class AlbumAdapter(
    private val images: ArrayList<SampleImage>,
    private val onClickImage: (image: SampleImage) -> Unit,
    private val onCheckedImage: (position: Int) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutAlbumImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]

        holder.image.setImageBitmap(image.thumbnail)

        holder.image.setOnClickListener {
            onClickImage.invoke(image)
        }

        if(image.path != null) {
            holder.checked.visibility = View.VISIBLE
        } else {
            holder.checked.visibility = View.GONE
        }

        holder.checked.setOnClickListener {
            onCheckedImage.invoke(position)
            holder.checked.visibility = View.GONE
        }



    }

    override fun getItemCount(): Int = images.size

    class ViewHolder(binding: LayoutAlbumImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivImage
        val checked = binding.btnChecked

    }



}