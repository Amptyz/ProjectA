package com.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Util.File.MyFile;
import com.example.uidesign.R;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    private MainActivity mainActivity;
    private List<MyFile> mFileList;
    private Context context;
    public void printt(){

    }
    public static class AudioViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public AudioViewHolder(@NonNull View itemView) {

            super(itemView);
            textView=itemView.findViewById(R.id.tv_item);
        }
    }

    public AudioAdapter(Context cont,List<MyFile>list){
        context=cont;
        mFileList=list;
    }
    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_recycle_item, parent, false);
        return new AudioAdapter.AudioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        MyFile myFile = mFileList.get(position);
        holder.textView.setText(myFile.getFileName());
        int posi=position;
        if(onItemClickListener!=null){
            // 点击
            holder.textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(holder.textView, posi);
                }
            });
            // 长按
            holder.textView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onItemLongClick(holder.textView, posi);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View veiw, int position);
        void onItemLongClick(View view, int position);
    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


}
