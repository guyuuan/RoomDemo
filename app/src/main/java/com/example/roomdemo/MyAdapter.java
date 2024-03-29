package com.example.roomdemo;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Word> allWords = new ArrayList<>();
    private boolean UseCardView;
    private WordViewModel viewModel;

    public MyAdapter(boolean useCardView, WordViewModel viewModel) {
        this.UseCardView = useCardView;
        this.viewModel = viewModel;
    }

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (UseCardView) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card2, parent, false));
        } else {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.normal2, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Word word = allWords.get(position);
        holder.tv_num.setText(String.valueOf(position + 1));
        holder.tv_eng.setText(word.getWord());
        holder.tv_ch.setText(word.getChineseMeaning());
        holder.aSwitchChInvisible.setOnCheckedChangeListener(null);
        if (word.isChinese_invisible()) {
            holder.tv_ch.setVisibility(View.GONE);
            holder.aSwitchChInvisible.setChecked(true);
        } else {
            holder.tv_ch.setVisibility(View.VISIBLE);
            holder.aSwitchChInvisible.setChecked(false);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q=" + holder.tv_eng.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.aSwitchChInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    holder.tv_ch.setVisibility(View.GONE);
                    word.setChinese_invisible(true);
                    viewModel.updateWord(word);
                }else {
                    holder.tv_ch.setVisibility(View.VISIBLE);
                    word.setChinese_invisible(false);
                    viewModel.updateWord(word);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allWords.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_num, tv_eng, tv_ch;
        Switch aSwitchChInvisible;
        ConstraintLayout constraintLayout;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_num = itemView.findViewById(R.id.tvnum);
            tv_eng = itemView.findViewById(R.id.tveng);
            tv_ch = itemView.findViewById(R.id.tvch);
            constraintLayout=itemView.findViewById(R.id.constraintLayout);
            aSwitchChInvisible = itemView.findViewById(R.id.chineseinvisible);
        }
    }
}
