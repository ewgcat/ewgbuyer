package com.ewgvip.buyer.android.net.apiservice;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


public interface BaseApiService {

    public static final String Base_URL = "http://www.ewgvip.com/";

    /**************************************************************************************/
     /*GET 请求  无参*/
    @GET
    Observable<JSONObject> executeJSONObjectGet1(
            @Url String url
    );

     /*GET 请求  有参*/
    @GET
    Observable<JSONObject> executeJSONObjectGet2(
            @Url String url,
            @QueryMap Map<String, String> maps
    );

    /*POST 请求 无参*/
    @POST
    Observable<JSONObject> executeJSONObjectPost1(
            @Url String url
    );

     /*POST 请求 有参*/
    @POST
    Observable<JSONObject> executeJSONObjectPost2(
            @Url String url,
            @QueryMap Map<String, String> maps
    );

    /**************************************************************************************/
  /*GET 请求  无参*/
    @GET
    Observable<ResponseBody> executeResponseBodyGet1(
            @Url String url
    );

     /*GET 请求  有参*/
    @GET
    Observable<ResponseBody> executeResponseBodyGet2(
            @Url String url,
            @QueryMap Map<String, String> maps
    );

    /*POST 请求 无参*/
    @POST
    Observable<ResponseBody> executeResponseBodyPost1(
            @Url String url
    );

     /*POST 请求 有参*/
    @POST
    Observable<ResponseBody> executeResponseBodyPost2(
            @Url String url,
            @QueryMap Map<String, String> maps
    );

    /**************************************************************************************/

    /*GET 请求 下载*/
    @Streaming
    @GET
    Observable<ResponseBody> downloadFileGET(
            @Url String fileUrl
    );

    /*POST 请求 下载*/
    @Streaming
    @POST
    Observable<ResponseBody> downloadFilePOST(
            @Url String fileUrl
    );

    /*POST 请求 上传单个文件*/
    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody
    );

    /*POST 请求 上传文件*/
    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Part("filename") String description,
            @PartMap() Map<String, RequestBody> maps
    );


}
