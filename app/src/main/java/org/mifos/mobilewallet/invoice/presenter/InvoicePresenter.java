package org.mifos.mobilewallet.invoice.presenter;

import org.mifos.mobilewallet.base.BaseView;
import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.invoice.InvoiceContract;
import org.mifos.mobilewallet.invoice.domain.model.Invoice;
import org.mifos.mobilewallet.invoice.domain.usecase.CreateInvoice;
import org.mifos.mobilewallet.invoice.domain.usecase.FetchPaymentMethods;

import javax.inject.Inject;

/**
 * Created by naman on 20/6/17.
 */

public class InvoicePresenter implements InvoiceContract.InvoicePresenter {

    private final UseCaseHandler mUsecaseHandler;
    @Inject
    FetchPaymentMethods fetchPaymentMethods;
    @Inject
    CreateInvoice createInvoiceUseCase;
    private InvoiceContract.InvoiceView mInvoiceView;

    @Inject
    public InvoicePresenter(UseCaseHandler useCaseHandler) {
        this.mUsecaseHandler = useCaseHandler;
    }

    @Override
    public void attachView(BaseView baseView) {
        mInvoiceView = (InvoiceContract.InvoiceView) baseView;
        mInvoiceView.setPresenter(this);
    }

    @Override
    public void getPaymentMethods() {
        mUsecaseHandler.execute(fetchPaymentMethods, null,
                new UseCase.UseCaseCallback<FetchPaymentMethods.ResponseValue>() {
                    @Override
                    public void onSuccess(FetchPaymentMethods.ResponseValue response) {
                        mInvoiceView.showPaymentMethods(response.getPaymentMethods());
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
    }

    @Override
    public void createInvoice(Invoice invoice) {
        mUsecaseHandler.execute(createInvoiceUseCase, new CreateInvoice.RequestValues(invoice),
                new UseCase.UseCaseCallback<CreateInvoice.ResponseValue>() {
                    @Override
                    public void onSuccess(CreateInvoice.ResponseValue response) {
                        mInvoiceView.invoiceCreated(response.getInvoice());
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
    }
}
