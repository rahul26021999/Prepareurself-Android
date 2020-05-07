package com.prepare.prepareurself.search;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.google.android.gms.common.api.Api;
import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.search.db.SearchDbRespository;
import com.prepare.prepareurself.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository{

    ApiInterface apiInterface;
    private SearchDbRespository searchDbRespository;

    public SearchRepository(Application application){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        searchDbRespository = new SearchDbRespository(application);
    }

    public LiveData<SearchResponseModel> search(String token, String query, final int page){

        final MutableLiveData<SearchResponseModel> data = new MutableLiveData<>();

        Log.d("paging_url", apiInterface.search(token, query, page).request().url().toString());

        apiInterface.search(token, query, page).enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                SearchResponseModel responseModel = response.body();
                if (page == 1){
                    searchDbRespository.deleteAllSearches();
                }
                if (responseModel!=null){
                    if (responseModel.getError_code()==0){
                        if (responseModel.getData()!=null && !responseModel.getData().isEmpty()){
                            for (SearchModel searchModel : responseModel.getData()){
                                searchDbRespository.insertSearch(searchModel);
                            }
                        }
                        data.setValue(responseModel);
                    }else{
                        data.setValue(null);
                    }
                }else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;

    }
}
