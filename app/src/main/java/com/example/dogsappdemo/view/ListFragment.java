package com.example.dogsappdemo.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dogsappdemo.R;
import com.example.dogsappdemo.model.DogBreed;
import com.example.dogsappdemo.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Action;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment {
    private ListViewModel viewModel;
    private DogsListAdapter dogsListAdapter = new DogsListAdapter(new ArrayList<>());
    @BindView(R.id.dogsList)
    RecyclerView dogsList;

    @BindView(R.id.listError)
    TextView listError;

    @BindView(R.id.loadingView)
    ProgressBar loadingView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    public ListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

     //   ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();

        //    Navigation.findNavController(view).navigate(action);

        // create view model
        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.refresh(); // retrieve the info from view model

        dogsList.setLayoutManager(new LinearLayoutManager(getContext())); // list in linear fashion
        dogsList.setAdapter(dogsListAdapter);
        refreshLayout.setOnRefreshListener(() -> {
            dogsList.setVisibility(View.GONE);
            listError.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
            viewModel.refreshBypassCache();
            refreshLayout.setRefreshing(false);
        });
        observeViewModel();
    }
    private void observeViewModel() {

       // attach to live data

        viewModel.dogs.observe(this, dogs -> {
            if(dogs != null && dogs instanceof List){
                dogsList.setVisibility(View.VISIBLE);
                dogsListAdapter.updateDogsList(dogs);
            }

        });
        viewModel.dogsLoadError.observe(this, isError -> {
            if(isError != null && isError instanceof Boolean){
                listError.setVisibility(isError ? View.VISIBLE : View.GONE);

            }
        });
        viewModel.loading.observe(this, isLoading -> {
            if(isLoading != null && isLoading instanceof Boolean){
                loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if(isLoading)
                {
                    listError.setVisibility(View.GONE);
                    dogsList.setVisibility(View.GONE);
                }

            }
        });
    }
  /* @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionSettings: {
                if(isAdded()){
                    Navigation.findNavController(getView()).navigate(ListFragmentDirections.actionSettings());
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
*/
}
