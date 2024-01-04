package com.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Util.File.MyFile;
import com.example.uidesign.R;
import com.example.uidesign.data.model.ClassRecord;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    private MainActivity mainActivity;
    private List<ClassRecord> mRecordList;
    private Context context;

    public static class AudioViewHolder extends RecyclerView.ViewHolder {

        public TextView textClassName;
        public TextView textSummary;
        public TextView textDate;
        public ImageButton btnDelete;

        public AudioViewHolder(@NonNull View itemView) {

            super(itemView);
            textClassName = itemView.findViewById(R.id.className);
            textSummary = itemView.findViewById(R.id.summary);
            textDate = itemView.findViewById(R.id.date);
            btnDelete = itemView.findViewById(R.id.delete);
        }
    }

    public AudioAdapter(Context cont,List<ClassRecord>list){
        context=cont;
        mRecordList=list;
    }
    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_recycle_item, parent, false);
        return new AudioAdapter.AudioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        ClassRecord myRecord = mRecordList.get(position);
        holder.textClassName.setText(myRecord.getClassName());
        holder.textSummary.setText(myRecord.getSummary());
        holder.textDate.setText(myRecord.getDate());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        int posi=position;
        if(onItemClickListener!=null){
            // 点击
            holder.textClassName.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(holder.textClassName, posi);
                }
            });
            // 长按
            holder.textClassName.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onItemLongClick(holder.textClassName, posi);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


}
