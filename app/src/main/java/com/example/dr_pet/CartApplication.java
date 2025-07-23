package com.example.dr_pet;

import android.app.Application;
import com.example.dr_pet.manager.CartManager;

public class CartApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CartManager.getInstance().initialize(this);
    }
}