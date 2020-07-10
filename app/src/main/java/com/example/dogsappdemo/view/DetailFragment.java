package com.example.dogsappdemo.view;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;
import android.provider.Telephony;
import android.telephony.SmsManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.dogsappdemo.R;
import com.example.dogsappdemo.databinding.FragmentDetailBinding;
import com.example.dogsappdemo.databinding.FragmentDetailBindingImpl;
import com.example.dogsappdemo.databinding.SendSmsDialogBinding;
import com.example.dogsappdemo.model.DogBreed;
import com.example.dogsappdemo.model.DogPalette;
import com.example.dogsappdemo.model.SmsInfo;
import com.example.dogsappdemo.viewmodel.DetailViewModel;


public class DetailFragment extends Fragment {
    private int doguuid;
    private DetailViewModel viewModel;
    private FragmentDetailBinding binding;
    private Boolean sendSmsStarted = false;
    private DogBreed currentDog;

   /* @BindView(R.id.dogImage)
    ImageView dogImage;

    @BindView(R.id.dogName)
    TextView dogName;
    @BindView(R.id.dogPurpose)
    TextView dogPurpose;
    @BindView(R.id.dogTemperament)
    TextView dogTemperament;

    @BindView(R.id.dogLifespan)
    TextView lifeSpan;*/

    public DetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

        this.binding = binding;
        setHasOptionsMenu(true);
        //  ButterKnife.bind(this,view);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            doguuid = DetailFragmentArgs.fromBundle(getArguments()).getDoguuid();
        }
        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch(doguuid);
        observeViewModel();

    }

    private void observeViewModel() {
        viewModel.dogLiveData.observe(this, dogBreed -> {
            if (dogBreed != null && dogBreed instanceof DogBreed && getContext() != null) {
                currentDog = dogBreed;
                binding.setDog(dogBreed);
                if (dogBreed.imageUrl != null) {
                    setupBackgroundColor(dogBreed.imageUrl);
                }
            }

        });
    }

    private void setupBackgroundColor(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    int intColor = palette.getLightMutedSwatch().getRgb();
                                    DogPalette myPalette = new DogPalette(intColor);
                                    binding.setPalette(myPalette);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_sms: {
                if (!sendSmsStarted) {
                    sendSmsStarted = true;
                    ((MainActivity) getActivity()).checkSmsPermission();
                }
                break;
            }
            case R.id.action_share: {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "check out this dog breed ");
                intent.putExtra(Intent.EXTRA_TEXT, currentDog.dogBreed + " bred for" + currentDog.bredFor);
                intent.putExtra(Intent.EXTRA_STREAM, currentDog.imageUrl);
                startActivity(Intent.createChooser(intent, "Share With "));
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void onPermissionResult(Boolean permissionGranted) {
        // sendSmsStarted = false;
        if (isAdded() && sendSmsStarted && permissionGranted) {
            SmsInfo smsInfo = new SmsInfo("", currentDog.dogBreed + " bred for" + currentDog.bredFor, currentDog.imageUrl);
            SendSmsDialogBinding dialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()),
                    R.layout.send_sms_dialog,
                    null,
                    false
            );
            new AlertDialog.Builder(getContext())
                    .setView(dialogBinding.getRoot())
                    .setPositiveButton("Send SMS", ((dialog, which) -> {
                        if (!dialogBinding.smsDestination.getText().toString().isEmpty()) {
                            smsInfo.to = dialogBinding.smsDestination.getText().toString();
                            sendSms(smsInfo);
                        }

                    }))
                    .setNegativeButton("cancel", ((dialogInterface, i) -> {
                    }))
                    .show();
            sendSmsStarted = false;
            dialogBinding.setSmsInfo(smsInfo);
        }
    }

    private void sendSms(SmsInfo smsInfo) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null);
    }
}
