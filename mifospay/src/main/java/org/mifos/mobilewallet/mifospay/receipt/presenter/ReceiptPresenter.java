package org.mifos.mobilewallet.mifospay.receipt.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.usecase.RunReport;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.receipt.ReceiptContract;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import javax.inject.Inject;

/**
 * Created by ankur on 06/June/2018
 */

public class ReceiptPresenter implements ReceiptContract.ReceiptPresenter {

    private final UseCaseHandler mUseCaseHandler;
    @Inject
    RunReport runReportUseCase;
    private ReceiptContract.ReceiptView mReceiptView;

    @Inject
    public ReceiptPresenter(UseCaseHandler useCaseHandler) {
        this.mUseCaseHandler = useCaseHandler;
    }

    @Override
    public void attachView(BaseView baseView) {
        mReceiptView = (ReceiptContract.ReceiptView) baseView;
        mReceiptView.setPresenter(this);
    }

    @Override
    public void fetchReceipt(final String transactionId) {

        mUseCaseHandler.execute(runReportUseCase,
                new RunReport.RequestValues(transactionId),
                new UseCase.UseCaseCallback<RunReport.ResponseValue>() {
                    @Override
                    public void onSuccess(RunReport.ResponseValue response) {
                        mReceiptView.writeReceipt(response.getResponseBody(),
                                Constants.RECEIPT + transactionId + Constants.PDF);
                    }

                    @Override
                    public void onError(String message) {
                        mReceiptView.hideProgressDialog();
                        mReceiptView.showSnackbar(Constants.ERROR_FETCHING_RECEIPT);
                    }
                });
    }
}
