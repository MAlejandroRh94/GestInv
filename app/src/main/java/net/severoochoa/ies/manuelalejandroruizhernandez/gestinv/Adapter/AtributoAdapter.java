package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Atributo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class AtributoAdapter extends RecyclerView.Adapter<AtributoAdapter.AtributoViewHolder> {
    private List<Atributo> atributoList;

    private CategoriaAdapter.OnItemClickListener mItemClickListener;
    private CategoriaAdapter.OnItemLongClickListener mItemLongClickListener;

    public AtributoAdapter(List<Atributo> atributoList) {
        this.atributoList = atributoList;
    }

    @Override
    public AtributoAdapter.AtributoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_atributo, parent, false);
        return new AtributoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AtributoViewHolder holder, int position) {
        Atributo atributo = atributoList.get(position);
        holder.tvValor.setText(atributo.getValor());
    }

    @Override
    public int getItemCount() {
        return atributoList.size();
    }

    public Atributo getAtributo(int position) {
        return atributoList.get(position);
    }


    public class AtributoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvValor;

        public AtributoViewHolder(View itemView) {
            super(itemView);
            tvValor = itemView.findViewById(R.id.tvValor);
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


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void SetOnItemClickListener(final CategoriaAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnItemLongClickListener(final CategoriaAdapter.OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }
}
