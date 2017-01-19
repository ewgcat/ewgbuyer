package com.ewgvip.buyer.android.net.converterfactory;


import com.ewgvip.buyer.android.net.convert.JSONObjectConverter;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/8/19.
 */
public  class JSONObjectConverterFactory extends Converter.Factory {

    public static final JSONObjectConverterFactory INSTANCE = new JSONObjectConverterFactory();

    public static JSONObjectConverterFactory create() {
        return INSTANCE;
    }

    // 我们只关实现从ResponseBody 到 String 的转换，所以其它方法可不覆盖
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == JSONObject.class) {
            return JSONObjectConverter.INSTANCE;
        }
        //其它类型我们不处理，返回null就行
        return null;
    }
}
