package org.mifos.mobilewallet.auth.presenter;

import org.mifos.mobilewallet.auth.AuthContract;
import org.mifos.mobilewallet.auth.domain.model.Bank;
import org.mifos.mobilewallet.auth.domain.usecase.FetchBanks;
import org.mifos.mobilewallet.base.BaseView;
import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;

import javax.inject.Inject;

/**
 * Created by naman on 20/6/17.
 */

public class AddAccountPresenter implements AuthContract.AddAcountPresenter {

    private final UseCaseHandler mUsecaseHandler;
    @Inject
    FetchBanks fetchBanks;
    private AuthContract.AddAccountView mAddAccountView;

    @Inject
    public AddAccountPresenter(UseCaseHandler useCaseHandler) {
        this.mUsecaseHandler = useCaseHandler;
    }

    @Override
    public void attachView(BaseView baseView) {
        this.mAddAccountView = (AuthContract.AddAccountView) baseView;
        mAddAccountView.setPresenter(this);

    }

    @Override
    public void loadBankData() {

        mUsecaseHandler.execute(fetchBanks, null,
                new UseCase.UseCaseCallback<FetchBanks.ResponseValue>() {
                    @Override
                    public void onSuccess(FetchBanks.ResponseValue response) {
                        mAddAccountView.showBanks(response.getPopularBanks(),
                                response.getOtherBanks());
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
    }

    @Override
    public void bankSelected(Bank bank) {
        mAddAccountView.openBankAccount();
    }
}
