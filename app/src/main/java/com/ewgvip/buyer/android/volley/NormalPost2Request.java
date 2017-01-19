package com.ewgvip.buyer.android.volley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.LoginActivity;
import com.ewgvip.buyer.android.volley.Response.ErrorListener;
import com.ewgvip.buyer.android.volley.Response.Listener;
import com.ewgvip.buyer.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class NormalPost2Request extends Request<JSONArray> {
    private Map<String, String> mMap;
    private Listener<JSONArray> mListener;
    private Context context;

    public NormalPost2Request(Context context, String url,
                              Listener<JSONArray> listener, ErrorListener errorListener, Map map) {
        super(Method.POST, url, errorListener);
        this.context = context;
        mListener = listener;
        mMap = new HashMap<String, String>();
        if (map != null) {
            for (Object key : map.keySet()) {
                if (map.get(key) != null)
                    mMap.put(key.toString(), map.get(key).toString());
            }
            mMap.put("device_type", "Android");
            String device_id =  BaseActivity.getDeviceID(context);
            if (device_id==null){
                device_id = BaseActivity.getDeviceId(context);
            }
            if (device_id != null && !device_id.equals(""))
                mMap.put("device_id", device_id);
        }
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> map = new HashMap<String, String>();
        SharedPreferences preferences = context.getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String verify = preferences.getString("verify", "");
        map.put("verify", verify);
        return map;
    }

    // mMap是已经按照前面的方式,设置了参数的实例
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    // 此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            if (jsonString.contains("验证信息错误")) {
                SharedPreferences preferences = context.getSharedPreferences(
                        "user", Context.MODE_PRIVATE);
                Editor editor = preferences.edit();
                editor.remove("userName");
                editor.remove("user_id");
                editor.remove("token");
                editor.remove("verify");
                editor.commit();
                context.startActivity(new Intent(context, LoginActivity.class).putExtra("msg", "登录已过期,请重新登录"));
                return Response.error(null);
            }

            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        mListener.onResponse(response);
    }
}
