package org.mifos.mobilewallet.mifospay.common.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.common.TransferContract;
import org.mifos.mobilewallet.mifospay.common.presenter.MakeTransferPresenter;
import org.mifos.mobilewallet.mifospay.data.local.LocalRepository;
import org.mifos.mobilewallet.mifospay.home.ui.TransferFragment;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class defines the UI components of the Transfer Fragment.
 * @author naman
 * @since 30/8/17
 */

public class MakeTransferFragment extends BottomSheetDialogFragment
        implements TransferContract.TransferView {

    @Inject
    MakeTransferPresenter mPresenter;

    @Inject
    LocalRepository localRepository;

    TransferContract.TransferPresenter mTransferPresenter;

    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @BindView(R.id.tv_client_name)
    TextView tvClientName;

    @BindView(R.id.tv_client_vpa)
    TextView tvClientVpa;

    @BindView(R.id.tv_transfer_status)
    TextView tvTransferStatus;

    @BindView(R.id.ll_content)
    View contentView;

    @BindView(R.id.view_transfer_success)
    View viewTransferSuccess;

    @BindView(R.id.view_transfer_failure)
    View viewTransferFailure;

    private BottomSheetBehavior mBehavior;
    private long toClientId;
    private double amount;

    public static MakeTransferFragment newInstance(String toExternalId, double amount) {

        Bundle args = new Bundle();
        args.putString(Constants.TO_EXTERNAL_ID, toExternalId);
        args.putDouble(Constants.AMOUNT, amount);
        MakeTransferFragment fragment = new MakeTransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    /**
     * This function dismisses the dialog interface.
     * @param dialog : The dialog to be dismissed
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    /**
     * This function creates a dialog.
     * @return : The dialog created
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.fragment_make_transfer, null);

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        ButterKnife.bind(this, view);
        mPresenter.attachView(this);

        amount = getArguments().getDouble(Constants.AMOUNT);

        mTransferPresenter.fetchClient(getArguments().getString(Constants.TO_EXTERNAL_ID));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTransferPresenter.makeTransfer(localRepository.getClientDetails().getClientId(),
                        toClientId, amount);
                tvTransferStatus.setText(Constants.SENDING_MONEY);
                progressBar.setVisibility(View.VISIBLE);
                contentView.setVisibility(View.GONE);
            }
        });

        return dialog;
    }

    @Override
    public void showToClientDetails(long clientId, String name, String externalId) {
        this.toClientId = clientId;

        tvClientName.setText(name);
        tvAmount.setText(Constants.RUPEE + " " + amount);
        tvClientVpa.setText(externalId);

        contentView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }

    /**
     * This function is called when the transfer made is successful.
     * This function will set the transfer state to successful.
     */
    @Override
    public void transferSuccess() {
        tvTransferStatus.setText(Constants.TRANSACTION_SUCCESSFUL);
        progressBar.setVisibility(View.GONE);
        viewTransferSuccess.setVisibility(View.VISIBLE);
    }

    /**
     * This function is called when the transfer made is a failure.
     * This function will set the transfer state to failure.
     */
    @Override
    public void transferFailure() {
        tvTransferStatus.setText(Constants.UNABLE_TO_PROCESS_TRANSFER);
        progressBar.setVisibility(View.GONE);
        viewTransferFailure.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    /**
     * An overridden function to set Presenter reference in this UI Component.
     * @param presenter : Presenter component reference for this package
     */
    @Override
    public void setPresenter(TransferContract.TransferPresenter presenter) {
        this.mTransferPresenter = presenter;
    }

    /**
     * This function is called when Vpa is not found.
     */
    @Override
    public void showVpaNotFoundSnackbar() {
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(TransferFragment.REQUEST_SHOW_DETAILS,
                    Activity.RESULT_CANCELED, null);
            dismiss();
        }
    }
}