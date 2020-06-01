package com.prepare.prepareurself.search.models;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.search.db.SearchDbRespository;
import com.prepare.prepareurself.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository{

    ApiInterface apiInterface;
    private SearchDbRespository searchDbRespository;
    Context context;
    public MutableLiveData<SearchResponseModel> searchData;

    public SearchRepository(Application application){
        context = application;
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        searchDbRespository = new SearchDbRespository(application);
        searchData = new MutableLiveData<>();
    }

    public MutableLiveData<SearchResponseModel> search(String token, String query){

        final MutableLiveData<SearchResponseModel> data = new MutableLiveData<>();

        Log.d("paging_url", apiInterface.search(token, query).request().url().toString());

        apiInterface.search(token, query).enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                SearchResponseModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.getError_code()==0){
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


    public MutableLiveData<SearchResponseModel> searchWithPagination(final String token, final String query, final int page){

        apiInterface.searchWithPagination(token, query,10,page).enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                SearchResponseModel responseModel = response.body();
                Log.d("paging_debug",responseModel+"");
                Log.d("paging_url", apiInterface.searchWithPagination(token, query,10,page).request().url().toString());
                if (responseModel!=null){
                    if (responseModel.getError_code()==0){
                        searchData.setValue(responseModel);
                    }else{
                        searchData.setValue(null);
                    }
                }else {
                    searchData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                searchData.setValue(null);
            }
        });

        return searchData;

    }

}
