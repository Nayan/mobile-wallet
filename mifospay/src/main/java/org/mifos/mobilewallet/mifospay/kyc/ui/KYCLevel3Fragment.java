package org.mifos.mobilewallet.mifospay.kyc.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.base.BaseFragment;
import org.mifos.mobilewallet.mifospay.kyc.KYCContract;
import org.mifos.mobilewallet.mifospay.kyc.presenter.KYCLevel3Presenter;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by ankur on 17/May/2018
 */

public class KYCLevel3Fragment extends BaseFragment implements KYCContract.KYCLevel3View {

    @Inject
    KYCLevel3Presenter mPresenter;

    KYCContract.KYCLevel3Presenter mKYCLevel3Presenter;

    public static KYCLevel3Fragment newInstance() {

        Bundle args = new Bundle();

        KYCLevel3Fragment fragment = new KYCLevel3Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Setter function for setting the Presenter for KYCLevel3Presenter

    @Override
    public void setPresenter(KYCContract.KYCLevel3Presenter presenter) {
        mKYCLevel3Presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        //Inflating the kyc level 3 fragment
        View rootView = inflater.inflate(R.layout.fragment_kyc_lvl3, container, false);
        //Using the butter knife library function bind to bind the views
        ButterKnife.bind(this, rootView);
        mPresenter.attachView(this);
        //setting the toolbar Title of KYC REGISTERATION
        setToolbarTitle(Constants.KYC_REGISTRATION_LEVEL_3);
        return rootView;
    }

}
