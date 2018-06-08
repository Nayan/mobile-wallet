package org.mifos.mobilewallet.core.data.fineract.repository;

import org.mifos.mobilewallet.core.data.fineract.api.FineractApiManager;
import org.mifos.mobilewallet.core.data.fineract.api.GenericResponse;
import org.mifos.mobilewallet.core.data.fineract.api.SelfServiceApiManager;
import org.mifos.mobilewallet.core.data.fineract.entity.Invoice;
import org.mifos.mobilewallet.core.data.fineract.entity.Page;
import org.mifos.mobilewallet.core.data.fineract.entity.SearchedEntity;
import org.mifos.mobilewallet.core.data.fineract.entity.UserEntity;
import org.mifos.mobilewallet.core.data.fineract.entity.accounts.savings.SavingsWithAssociations;
import org.mifos.mobilewallet.core.data.fineract.entity.accounts.savings.TransferDetail;
import org.mifos.mobilewallet.core.data.fineract.entity.beneficary.Beneficiary;
import org.mifos.mobilewallet.core.data.fineract.entity.beneficary.BeneficiaryPayload;
import org.mifos.mobilewallet.core.data.fineract.entity.beneficary.BeneficiaryUpdatePayload;
import org.mifos.mobilewallet.core.data.fineract.entity.client.Client;
import org.mifos.mobilewallet.core.data.fineract.entity.client.ClientAccounts;
import org.mifos.mobilewallet.core.data.fineract.entity.kyc.KYCLevel1Details;
import org.mifos.mobilewallet.core.data.fineract.entity.payload.TransferPayload;
import org.mifos.mobilewallet.core.data.fineract.entity.payload.UpdateVpaPayload;
import org.mifos.mobilewallet.core.data.fineract.entity.register.RegisterPayload;
import org.mifos.mobilewallet.core.data.fineract.entity.register.UserVerify;
import org.mifos.mobilewallet.core.data.fineract.entity.savedcards.Card;
import org.mifos.mobilewallet.core.domain.model.twofactor.AccessToken;
import org.mifos.mobilewallet.core.domain.model.twofactor.DeliveryMethod;
import org.mifos.mobilewallet.core.utils.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by naman on 16/6/17.
 */

@Singleton
public class FineractRepository {

    private final FineractApiManager fineractApiManager;
    private final SelfServiceApiManager selfApiManager;

    @Inject
    public FineractRepository(FineractApiManager fineractApiManager) {
        this.fineractApiManager = fineractApiManager;
        this.selfApiManager = FineractApiManager.getSelfApiManager();
    }

    public Observable<ResponseBody> registerUser(RegisterPayload registerPayload) {
        return fineractApiManager.getRegistrationAPi().registerUser(registerPayload);
    }

    public Observable<ResponseBody> verifyUser(UserVerify userVerify) {
        return fineractApiManager.getRegistrationAPi().verifyUser(userVerify);
    }

    public Observable<List<SearchedEntity>> searchResources(String query, String resources,
            Boolean exactMatch) {
        return fineractApiManager.getSearchApi().searchResources(query, resources, exactMatch);
    }


    public Observable<ResponseBody> updateClientVpa(long clientId, UpdateVpaPayload payload) {
        return fineractApiManager.getClientsApi().updateClientVpa(clientId, payload)
                .map(new Func1<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody call(ResponseBody responseBody) {
                        return responseBody;
                    }
                });
    }

    public Observable<ClientAccounts> getAccounts(long clientId) {
        return fineractApiManager.getClientsApi().getAccounts(clientId, Constants.SAVINGS);
    }

    public Observable<Client> getClientDetails(long clientId) {
        return fineractApiManager.getClientsApi().getClientForId(clientId);
    }

    public Observable<GenericResponse> addSavedCards(long clientId,
            Card card) {
        return fineractApiManager.getSavedCardApi().addSavedCard((int) clientId, card);
    }

    public Observable<List<Card>> fetchSavedCards(long clientId) {
        return fineractApiManager.getSavedCardApi().getSavedCards((int) clientId);
    }

    public Observable<GenericResponse> editSavedCard(int clientId, Card card) {
        return fineractApiManager.getSavedCardApi().updateCard(clientId, card.getId(), card);
    }

    public Observable<GenericResponse> deleteSavedCard(int clientId, int cardId) {
        return fineractApiManager.getSavedCardApi().deleteCard(clientId, cardId);
    }

    public Observable<GenericResponse> uploadKYCDocs(String entityType, long entityId, String name,
            String desc, MultipartBody.Part file) {
        return fineractApiManager.getDocumentApi().createDocument(entityType, entityId, name, desc,
                file);
    }

    public Observable<TransferDetail> getAccountTransfer(long transferId) {
        return fineractApiManager.getAccountTransfersApi().getAccountTransfer(transferId);
    }

    public Observable<GenericResponse> uploadKYCLevel1Details(int clientId,
            KYCLevel1Details kycLevel1Details) {
        return fineractApiManager.getKycLevel1Api().addKYCLevel1Details(clientId,
                kycLevel1Details);
    }

    public Observable<List<KYCLevel1Details>> fetchKYCLevel1Details(int clientId) {
        return fineractApiManager.getKycLevel1Api().fetchKYCLevel1Details(clientId);
    }

    public Observable<GenericResponse> updateKYCLevel1Details(int clientId,
            KYCLevel1Details kycLevel1Details) {
        return fineractApiManager.getKycLevel1Api().updateKYCLevel1Details(clientId,
                kycLevel1Details);
    }

    public Observable<List<DeliveryMethod>> getDeliveryMethods() {
        return fineractApiManager.getTwoFactorAuthApi().getDeliveryMethods();
    }

    public Observable<String> requestOTP(String deliveryMethod) {
        return fineractApiManager.getTwoFactorAuthApi().requestOTP(deliveryMethod);
    }

    public Observable<AccessToken> validateToken(String token) {
        return fineractApiManager.getTwoFactorAuthApi().validateToken(token);
    }

    public Observable<ResponseBody> getTransactionReceipt(String outputType,
            String transactionId) {
        return fineractApiManager.getRunReportApi().getTransactionReceipt(outputType,
                transactionId);
    }

    public Observable<GenericResponse> addInvoice(String clientId, Invoice invoice) {
        return fineractApiManager.getInvoiceApi().addInvoice(clientId, invoice);
    }

    public Observable<List<Invoice>> fetchInvoices(String clientId) {
        return fineractApiManager.getInvoiceApi().getInvoices(clientId);
    }

    public Observable<List<Invoice>> fetchInvoice(String clientId, String invoiceId) {
        return fineractApiManager.getInvoiceApi().getInvoice(clientId, invoiceId);
    }


    public Observable<GenericResponse> editInvoice(String clientId, Invoice invoice) {
        return fineractApiManager.getInvoiceApi().updateInvoice(clientId, invoice.getId(), invoice);
    }

    public Observable<GenericResponse> deleteInvoice(String clientId, int invoiceId) {
        return fineractApiManager.getInvoiceApi().deleteInvoice(clientId, invoiceId);
    }

    //self user apis

    public Observable<UserEntity> loginSelf(String username, String password) {
        return selfApiManager.getAuthenticationApi().authenticate(username, password);
    }

    public Observable<Client> getSelfClientDetails(long clientId) {
        return selfApiManager.getClientsApi().getClientForId(clientId);
    }

    public Observable<Page<Client>> getSelfClientDetails() {
        return selfApiManager.getClientsApi().getClients();
    }

    public Observable<SavingsWithAssociations> getSelfAccountTransactions(long accountId) {
        return selfApiManager
                .getSavingAccountsListApi().getSavingsWithAssociations(accountId,
                        Constants.TRANSACTIONS);
    }

    public Observable<ClientAccounts> getSelfAccounts(long clientId) {
        return selfApiManager.getClientsApi().getAccounts(clientId, Constants.SAVINGS);
    }

    public Observable<List<Beneficiary>> getBeneficiaryList() {
        return selfApiManager.getBeneficiaryApi().getBeneficiaryList();
    }

    public Observable<ResponseBody> createBeneficiary(BeneficiaryPayload beneficiaryPayload) {
        return selfApiManager.getBeneficiaryApi().createBeneficiary(beneficiaryPayload);
    }

    public Observable<ResponseBody> updateBeneficiary(long beneficiaryId,
            BeneficiaryUpdatePayload payload) {
        return selfApiManager.getBeneficiaryApi().updateBeneficiary(beneficiaryId, payload);
    }

    public Observable<ResponseBody> makeThirdPartyTransfer(TransferPayload transferPayload) {
        return selfApiManager.getThirdPartyTransferApi().makeTransfer(transferPayload);
    }
}
