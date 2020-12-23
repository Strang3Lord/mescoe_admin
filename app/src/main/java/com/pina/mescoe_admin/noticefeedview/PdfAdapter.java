package com.pina.mescoe_admin.noticefeedview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pina.mescoe_admin.R;
import com.pina.mescoe_admin.uploadPDF;

import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private Context context;
    private List<uploadPDF> list;

    public PdfAdapter(Context context, List<uploadPDF> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdfitemlayout,parent,false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, final int position) {

        holder.pdfname.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.putExtra("pdfUrl",list.get(position).getUri());
            context.startActivity(intent);
        });

        holder.pdfimage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(list.get(position).getUri()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PdfViewHolder extends RecyclerView.ViewHolder {

        private TextView pdfname;
        private ImageView pdfimage;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfimage = itemView.findViewById(R.id.pdfimage);
            pdfname = itemView.findViewById(R.id.pdfname);
        }
    }
}
