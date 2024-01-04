package com.example;

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

    private List<MyFile> mFileList;

    public static class AudioViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public AudioViewHolder(@NonNull View itemView) {

            super(itemView);
            textView=itemView.findViewById(R.id.tv_item);
        }
    }

    public AudioAdapter(List<MyFile>list){
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
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }


}
