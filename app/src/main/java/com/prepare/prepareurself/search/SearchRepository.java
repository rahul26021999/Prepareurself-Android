package com.prepare.prepareurself.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.Api;
import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    ApiInterface apiInterface;

    public SearchRepository(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    public LiveData<SearchResponseModel> search(String token, String query){

        final MutableLiveData<SearchResponseModel> data = new MutableLiveData<>();

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
