package contre.facon.bruitdansleciel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progur.droidmelody.SongFinder
import contre.facon.bruitdansleciel.`interface`.SongClickListener
import contre.facon.bruitdansleciel.R

class SongsAdapter(val mDataset: List<SongFinder.Song>, val mListener: SongClickListener) :
    RecyclerView.Adapter<SongsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contextView = LayoutInflater.from(parent.context).inflate(
            R.layout.song_layout,
            parent,
            false
        )
        contextView.setOnClickListener(View.OnClickListener {
            val t = mDataset[viewType]
            mListener.onSongClick(t)
        })
        return ViewHolder(contextView, mDataset, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.songTitle.text = mDataset[position].title
        holder.songIndex = position
    }

    override fun getItemCount() = mDataset.size


    class ViewHolder(
        v: View,
        val mDataset: List<SongFinder.Song>,
        val mListener: SongClickListener
    ) : RecyclerView.ViewHolder(v),
        View.OnClickListener {
        val songTitle: TextView = v.findViewById<TextView>(R.id.songTitle)
        var songIndex: Int = 0;

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mListener.onSongClick(mDataset[songIndex])
        }
    }
}