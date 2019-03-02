package org.mifos.mobilewallet.mifospay.finance.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.mifos.mobilewallet.core.domain.model.BankAccountDetails;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.bank.BankContract;
import org.mifos.mobilewallet.mifospay.bank.adapters.BankAccountsAdapter;
import org.mifos.mobilewallet.mifospay.bank.presenter.BankAccountsPresenter;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.base.BaseFragment;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AccountsFragment extends BaseFragment implements BankContract.BankAccountsView {

    @BindView(R.id.inc_state_view)
    View vStateView;

    @Inject
    BankAccountsPresenter mPresenter;
    BankContract.BankAccountsPresenter mBankAccountsPresenter;

    @BindView(R.id.rv_accounts)
    RecyclerView mRvLinkedBankAccounts;


    @BindView(R.id.iv_empty_no_transaction_history)
    ImageView ivTransactionsStateIcon;

    @BindView(R.id.tv_empty_no_transaction_history_title)
    TextView tvTransactionsStateTitle;

    @Inject
    BankAccountsAdapter mBankAccountsAdapter;


    @BindView(R.id.linked_bank_account_text)
    TextView linkedAccountsText;

    @BindView(R.id.tv_empty_no_transaction_history_subtitle)
    TextView tvTransactionsStateSubtitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accounts, container, false);
        ButterKnife.bind(this, rootView);
        showProgressDialog(Constants.PLEASE_WAIT);
        setupRecycletView();
        mPresenter.attachView(this);
        mBankAccountsPresenter.fetchLinkedBankAccounts();
        showProgressDialog(Constants.PLEASE_WAIT);
        return rootView;
    }
    private void setupRecycletView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvLinkedBankAccounts.setLayoutManager(layoutManager);
        mRvLinkedBankAccounts.setHasFixedSize(true);
        mRvLinkedBankAccounts.setAdapter(mBankAccountsAdapter);
        mRvLinkedBankAccounts.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

//        mRvLinkedBankAccounts.addOnItemTouchListener(new RecyclerItemClickListener(this,
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View childView, int position) {
//                        Intent intent = new Intent(BankAccountsActivity.this,
//                                BankAccountDetailActivity.class);
//                        intent.putExtra(Constants.BANK_ACCOUNT_DETAILS,
//                                mBankAccountsAdapter.getBankDetails(position));
//                        intent.putExtra(Constants.INDEX, position);
//                        startActivityForResult(intent, Bank_Account_Details_Request_Code);
//                    }
//
//                    @Override
//                    public void onItemLongPress(View childView, int position) {
//
//                    }
//                }));
    }

    @Override
    public void showLinkedBankAccounts(List<BankAccountDetails> bankAccountList) {
        if (bankAccountList == null || bankAccountList.size() == 0) {
            mRvLinkedBankAccounts.setVisibility(View.GONE);
            linkedAccountsText.setVisibility(View.GONE);
            setupUi();
        } else {
            sortListInAplhabeticalOrder(bankAccountList);
            hideEmptyStateView();
            mRvLinkedBankAccounts.setVisibility(View.VISIBLE);
            linkedAccountsText.setVisibility(View.VISIBLE);
            mBankAccountsAdapter.setData(bankAccountList);
        }
        hideProgressDialog();


    }

    private void sortListInAplhabeticalOrder(List<BankAccountDetails> bankAccountList) {
        Collections.sort(bankAccountList, new
                Comparator<BankAccountDetails>() {
                    @Override
                    public int compare(BankAccountDetails s1, BankAccountDetails s2) {
                        return s1.getBankName().toLowerCase().
                                compareTo(s2.getBankName().toLowerCase());
                    }
                });
    }

    private void setupUi() {
        showEmptyStateView();
    }

    private void showEmptyStateView() {
        if (getActivity() != null) {
            vStateView.setVisibility(View.VISIBLE);
            Resources res = getResources();
            ivTransactionsStateIcon
                    .setImageDrawable(res.getDrawable(R.drawable.ic_accounts));
            tvTransactionsStateTitle
                    .setText(res.getString(R.string.empty_no_accounts_title));
            tvTransactionsStateSubtitle
                    .setText(res.getString(R.string.empty_no_accounts_subtitle));
        }
    }

    private void hideEmptyStateView() {
        vStateView.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(BankContract.BankAccountsPresenter presenter) {
        mBankAccountsPresenter = presenter;
    }
}

