package com.si_ware.neospectra.Activities.MainFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.si_ware.neospectra.C1284R;

public class AboutFragment extends Fragment {
    View myFragment;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.myFragment = inflater.inflate(C1284R.layout.fragment_about_us, container, false);
        return this.myFragment;
    }
}
