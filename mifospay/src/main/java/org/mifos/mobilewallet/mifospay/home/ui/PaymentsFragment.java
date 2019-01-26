package org.mifos.mobilewallet.mifospay.home.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseFragment;
import org.mifos.mobilewallet.mifospay.history.ui.HistoryFragment;
import org.mifos.mobilewallet.mifospay.home.adapter.TabLayoutAdapter;
import org.mifos.mobilewallet.mifospay.payments.ui.InvoicesFragment;
import org.mifos.mobilewallet.mifospay.payments.ui.RequestFragment;
import org.mifos.mobilewallet.mifospay.payments.ui.SendFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Payments Fragment
 * @author anonymous
 * @since 17/6/17
 */
public class PaymentsFragment extends BaseFragment {

    @BindView(R.id.vp_tab_layout)
    ViewPager vpTabLayout;

    @BindView(R.id.tl_tab_layout)
    TabLayout tilTabLayout;

    /**
     * Returns a new instance of the fragment.
     */
    public static PaymentsFragment newInstance() {
        Bundle args = new Bundle();
        PaymentsFragment fragment = new PaymentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finance, container, false);
        ButterKnife.bind(this, rootView);

        setupUi();
        setupViewPager();
        tilTabLayout.setupWithViewPager(vpTabLayout);

        return rootView;
    }

    /**
     * Sets the swipe refresh layout to false.
     * Updates the ActionBar title.
     */
    private void setupUi() {
        setSwipeEnabled(false);
        setToolbarTitle(getString(R.string.payments));
    }

    /**
     * Initializes the @tabLayoutAdapter and adds the required fragments.
     */
    private void setupViewPager() {
        TabLayoutAdapter tabLayoutAdapter
                = new TabLayoutAdapter(getChildFragmentManager());
        tabLayoutAdapter.addFragment(new SendFragment(), getString(R.string.send));
        tabLayoutAdapter.addFragment(new RequestFragment(), getString(R.string.request));
        tabLayoutAdapter.addFragment(new HistoryFragment(), getString(R.string.history));
        tabLayoutAdapter.addFragment(new InvoicesFragment(), getString(R.string.invoices));
        vpTabLayout.setAdapter(tabLayoutAdapter);
    }
}
