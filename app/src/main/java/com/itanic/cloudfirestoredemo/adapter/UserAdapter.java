package com.itanic.cloudfirestoredemo.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.itanic.cloudfirestoredemo.R;
import com.itanic.cloudfirestoredemo.databinding.RowListItemBinding;
import com.itanic.cloudfirestoredemo.model.User;

/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class UserAdapter extends FirestoreAdapter<UserAdapter.ViewHolder> {

    public UserAdapter(Query query) {
        super(query);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RowListItemBinding mBinding;

        ViewHolder(RowListItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        void bind(final DocumentSnapshot snapshot) {

            User user = snapshot.toObject(User.class);

            mBinding.rowTextName.setText(user.getName());
            mBinding.rowTextLanguage.setText(user.getLanguage());
        }
    }
}