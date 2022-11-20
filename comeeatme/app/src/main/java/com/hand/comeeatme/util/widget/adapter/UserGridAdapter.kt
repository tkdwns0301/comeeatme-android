
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.hand.comeeatme.R
import com.hand.comeeatme.databinding.LayoutAlbumImageBinding
import com.hand.comeeatme.view.main.home.post.DetailPostFragment

class UserGridAdapter(
    private val images: ArrayList<ArrayList<Int>>,
    private val context: Context,
) : RecyclerView.Adapter<UserGridAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutAlbumImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position][0]

        holder.image.setImageResource(image)

        holder.image.setOnClickListener {
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = manager.beginTransaction()

            ft.add(R.id.fg_MainContainer, DetailPostFragment(), "fm_Post")
            ft.commitAllowingStateLoss()
        }
    }

    override fun getItemCount(): Int = images.size

    class ViewHolder(binding: LayoutAlbumImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivImage

    }


}