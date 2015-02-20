/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.BuyButtonText;
import com.google.android.gms.wallet.fragment.Dimension;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

import wallet.example.com.walletexample.R;

public class AddWalletActivity extends Activity {
    private static final String TAG = "AddWalletActivity";

    private static final int REQUEST_CODE_MASKED_WALLET = 1;
    private WalletFragment mWalletFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wallet);
        
        createAndAddWalletFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCompat.invalidateOptionsMenu(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult");
    }

    private void createAndAddWalletFragment() {
        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                .setBuyButtonText(BuyButtonText.BUY_NOW)
                .setBuyButtonWidth(Dimension.MATCH_PARENT);

        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                .setFragmentStyle(walletFragmentStyle)
                .setTheme(WalletConstants.THEME_LIGHT)
                .setMode(WalletFragmentMode.BUY_BUTTON)
                .build();

        mWalletFragment = WalletFragment.newInstance(walletFragmentOptions);

        // Now initialize the Wallet Fragment
        String accountName = ((BikestoreApplication) getApplication()).getAccountName();
        MaskedWalletRequest maskedWalletRequest = createMaskedWalletRequest();
        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(maskedWalletRequest)
                .setMaskedWalletRequestCode(REQUEST_CODE_MASKED_WALLET)
                .setAccountName(accountName);
        mWalletFragment.initialize(startParamsBuilder.build());

        // add Wallet fragment to the UI
        getFragmentManager().beginTransaction()
                .replace(R.id.dynamic_wallet_button_fragment, mWalletFragment)
                .commit();
    }

    private static MaskedWalletRequest createMaskedWalletRequest() {

        return MaskedWalletRequest.newBuilder()
                .setMerchantName(Constants.MERCHANT_NAME)
                .setPhoneNumberRequired(true)
                .setShippingAddressRequired(true)
                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
                .setIsBillingAgreement(true)
                .build();
    }
}
