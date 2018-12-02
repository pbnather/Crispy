package dk.au.itsmap.group4.crispy.ui;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


// separating of OnClickListeners to Activity inspired by:
// https://github.com/guenodz/livedata-recyclerview-sample/blob/master/app/src/main/java/me/guendouz/livedata_recyclerview/PostsAdapter.java
public abstract class GenericRecyclerViewAdapter<T> extends RecyclerView.Adapter<GenericRecyclerViewAdapter.AbstractViewHolder> {

    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClicked(T meal);
    }

    private List<T> mValues;


    public GenericRecyclerViewAdapter() {
        mValues = new ArrayList<>();
    }

    public void setData(List<T> newData) {
        this.mValues = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onBindViewHolder(final GenericRecyclerViewAdapter.AbstractViewHolder holder, int position) {
        holder.bind(mValues.get(position));
    }

    protected abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
        protected AbstractViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        protected abstract void bind(final T item);
    }

}