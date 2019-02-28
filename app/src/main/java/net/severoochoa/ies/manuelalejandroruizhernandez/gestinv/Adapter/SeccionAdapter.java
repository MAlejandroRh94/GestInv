package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Seccion;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;


import java.util.List;

public class SeccionAdapter extends RecyclerView.Adapter<SeccionAdapter.SeccionViewHolder> {
    private Context context;
    private List<Seccion> seccionList;
    private DatabaseManager db;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public SeccionAdapter(Context context, List<Seccion> seccionList) {
        this.context = context;
        this.seccionList = seccionList;
        db = DatabaseManager.getInstance(context);
    }


    @NonNull
    @Override
    public SeccionAdapter.SeccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_seccion, parent, false);
        return new SeccionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SeccionAdapter.SeccionViewHolder holder, int position) {
        Seccion seccion = seccionList.get(position);
        holder.tvId.setText(String.valueOf(seccion.getId()));
        holder.tvNombre.setText(String.valueOf(seccion.getNombre()));
        db.openDB();
        holder.tvCantPas.setText(db.getPasillosFromSeccion(seccion.getId()).size()+" Pasillo/s");
        db.closeDB();
    }

    @Override
    public int getItemCount() {
        return seccionList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnItemLongClickListener(final OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public Seccion getSeccion(int position) {
        return seccionList.get(position);
    }


    public class SeccionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvId, tvNombre, tvCantPas;

        public SeccionViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCantPas = itemView.findViewById(R.id.tvCantPas);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mItemLongClickListener != null) {
                mItemLongClickListener.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }
}
