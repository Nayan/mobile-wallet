package org.mifos.mobilewallet.core.data.fineract.api.services;

import org.mifos.mobilewallet.core.data.fineract.api.ApiEndPoints;
import org.mifos.mobilewallet.core.data.fineract.api.GenericResponse;
import org.mifos.mobilewallet.core.data.fineract.entity.Page;
import org.mifos.mobilewallet.core.data.fineract.entity.accounts.savings.SavingAccount;
import org.mifos.mobilewallet.core.data.fineract.entity.accounts.savings.SavingsWithAssociations;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface SavingAccountsListService {

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}")
    Observable<SavingsWithAssociations> getSavingsWithAssociations(
            @Path("accountId") long accountId,
            @Query("associations") String associationType);

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS)
    Observable<Page<SavingsWithAssociations>> getSavingsAccounts(
            @Query("limit") int limit);

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS)
    Observable<GenericResponse> createSavingsAccount(@Body SavingAccount savingAccount);

    @POST(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}")
    Observable<GenericResponse> blockUnblockCommand(
            @Path("accountId") long accountId,
            @Query("command") String command);
}
