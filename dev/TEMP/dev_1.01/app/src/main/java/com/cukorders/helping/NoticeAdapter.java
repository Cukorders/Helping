package com.cukorders.helping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    String data1[], data2[];
    Context context;
    int images[];

    public NoticeAdapter(Context ct, String s1[], String s2[], int img[]){
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
    }


    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_row, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        holder.title.setText(data1[position]);
        holder.description.setText(data2[position]);
        holder.noticeImage.setImageResource(images[position]);


    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        ImageView noticeImage;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_title_view);
            description = itemView.findViewById(R.id.notice_description_view);
            noticeImage = itemView.findViewById(R.id.notice_image_view);
        }
    }
}
