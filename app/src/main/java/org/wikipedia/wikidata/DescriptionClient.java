package org.wikipedia.wikidata;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import org.wikipedia.dataclient.Service;
import org.wikipedia.dataclient.ServiceFactory;
import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.dataclient.mwapi.MwException;
import org.wikipedia.dataclient.mwapi.MwQueryPage;
import org.wikipedia.dataclient.mwapi.MwQueryResponse;
import org.wikipedia.page.PageTitle;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DescriptionClient {
    public interface Callback {
        void success(@NonNull Call<MwQueryResponse> call, @NonNull List<MwQueryPage> results);
        void failure(@NonNull Call<MwQueryResponse> call, @NonNull Throwable caught);
    }

    public Call<MwQueryResponse> request(@NonNull WikiSite wiki, @NonNull List<PageTitle> titles,
                                         @NonNull Callback cb) {
        return request(ServiceFactory.get(wiki), titles, cb);
    }

    @VisibleForTesting
    Call<MwQueryResponse> request(@NonNull Service service, @NonNull final List<PageTitle> titles,
                                  @NonNull final Callback cb) {
        Call<MwQueryResponse> call = service.getDescription(TextUtils.join("|", titles));

        call.enqueue(new retrofit2.Callback<MwQueryResponse>() {
            @Override public void onResponse(@NonNull Call<MwQueryResponse> call,
                                             @NonNull Response<MwQueryResponse> response) {
                if (response.body().success()) {
                    // noinspection ConstantConditions
                    cb.success(call, response.body().query().pages());
                } else if (response.body().hasError()) {
                    // noinspection ConstantConditions
                    cb.failure(call, new MwException(response.body().getError()));
                } else {
                    cb.failure(call, new IOException("An unknown error occurred."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MwQueryResponse> call, @NonNull Throwable t) {
                cb.failure(call, t);
            }
        });
        return call;
    }
}
