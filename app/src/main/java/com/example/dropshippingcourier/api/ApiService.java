package com.example.dropshippingcourier.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("uploadProofPayment")
    Call<ResponseBody> uploadProofPayment(
            @Part("payment_id") RequestBody paymentId,
            @Part MultipartBody.Part proofImage
    );
}


