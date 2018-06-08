package org.mifos.mobilewallet.mifospay.kyc.presenter;

import android.telephony.PhoneNumberUtils;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.data.fineract.entity.kyc.KYCLevel1Details;
import org.mifos.mobilewallet.core.domain.usecase.UploadKYCLevel1Details;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.data.local.LocalRepository;
import org.mifos.mobilewallet.mifospay.kyc.KYCContract;

import javax.inject.Inject;

/**
 * Created by ankur on 17/May/2018
 */

public class KYCLevel1Presenter implements KYCContract.KYCLevel1Presenter {

    private final UseCaseHandler mUseCaseHandler;
    private final LocalRepository mLocalRepository;
    @Inject
    UploadKYCLevel1Details uploadKYCLevel1DetailsUseCase;
    private KYCContract.KYCLevel1View mKYCLevel1View;

    @Inject
    public KYCLevel1Presenter(UseCaseHandler useCaseHandler, LocalRepository localRepository) {
        mUseCaseHandler = useCaseHandler;
        mLocalRepository = localRepository;
    }

    @Override
    public void attachView(BaseView baseView) {
        mKYCLevel1View = (KYCContract.KYCLevel1View) baseView;
        mKYCLevel1View.setPresenter(this);
    }


    @Override
    public void submitData(String fname, String lname, String address1, String address2,
            String phonecode, String phoneno, String dob) {

        mKYCLevel1View.showProgressDialog("Please wait..");

        if (PhoneNumberUtils.isGlobalPhoneNumber(phonecode + phoneno) || true) {

            KYCLevel1Details kycLevel1Details = new KYCLevel1Details(fname, lname, address1,
                    address2, phonecode + phoneno, dob, "1");

            uploadKYCLevel1DetailsUseCase.setRequestValues(new UploadKYCLevel1Details.RequestValues(
                    (int) mLocalRepository.getClientDetails().getClientId(), kycLevel1Details));

            final UploadKYCLevel1Details.RequestValues requestValues =
                    uploadKYCLevel1DetailsUseCase.getRequestValues();

            mUseCaseHandler.execute(uploadKYCLevel1DetailsUseCase, requestValues,
                    new UseCase.UseCaseCallback<UploadKYCLevel1Details.ResponseValue>() {
                        @Override
                        public void onSuccess(UploadKYCLevel1Details.ResponseValue response) {

                            mKYCLevel1View.hideProgressDialog();
                            mKYCLevel1View.showToast("KYC Level 1 details added successfully.");
                            mKYCLevel1View.goBack();
                        }

                        @Override
                        public void onError(String message) {

                            mKYCLevel1View.hideProgressDialog();
                            mKYCLevel1View.showToast("Error adding details.");
                        }
                    }
            );
        }
    }
}
