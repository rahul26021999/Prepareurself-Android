package com.prepare.prepareurself.search.models;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.search.db.SearchDbRespository;

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
}
