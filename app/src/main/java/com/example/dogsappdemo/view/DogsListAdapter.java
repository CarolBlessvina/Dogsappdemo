package com.example.dogsappdemo.view;

import android.provider.ContactsContract;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsappdemo.R;

import com.example.dogsappdemo.databinding.ItemDogBinding;

import com.example.dogsappdemo.model.DogBreed;
import com.example.dogsappdemo.util.Util;

import java.util.ArrayList;
import java.util.List;


public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> implements DogClickListener {

    private ArrayList<DogBreed> dogsList;
    public DogsListAdapter(ArrayList<DogBreed> dogsList){
        this.dogsList=dogsList;
    }
    public void updateDogsList(List<DogBreed> newDogsList){
      dogsList.clear();
      dogsList.addAll(newDogsList);
      notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDogBinding view = DataBindingUtil.inflate(inflater,R.layout.item_dog, parent,false);

        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {

        holder.itemView.setDog(dogsList.get(position));
        holder.itemView.setListener(this);
     /*   ImageView image = holder.itemView.findViewById(R.id.imageView);
        TextView name = holder.itemView.findViewById(R.id.name);
        TextView lifespan = holder.itemView.findViewById(R.id.lifeSpan);
        LinearLayout layout = holder.itemView.findViewById(R.id.dogLayout);
>>>>>>> dev

        name.setText(dogsList.get(position).dogBreed);
        lifespan.setText(dogsList.get(position).lifeSpan);
        Util.loadImage(image, dogsList.get(position).imageUrl,Util.getProgressDrawable(image.getContext()));
<<<<<<< HEAD
=======

        layout.setOnClickListener(view -> {
        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        action.setDoguuid(dogsList.get(position).uuid);
            Navigation.findNavController(layout).navigate(action);

        });*/
    }

    @Override
    public void onDogClicked(View v) {
        String uuidString = ((TextView)v.findViewById(R.id.dogId)).getText().toString();
        int uuid = Integer.valueOf(uuidString);
        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        action.setDoguuid(uuid);
        Navigation.findNavController(v).navigate(action);

    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder {

        public ItemDogBinding itemView;

        public DogViewHolder(@NonNull ItemDogBinding itemView) {
            super(itemView.getRoot());

            this.itemView =itemView;
        }
    }

}
