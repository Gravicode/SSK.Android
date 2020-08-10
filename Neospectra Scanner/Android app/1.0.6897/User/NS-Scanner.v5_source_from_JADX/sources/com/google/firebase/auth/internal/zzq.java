package com.google.firebase.auth.internal;

import android.support.annotation.Nullable;
import android.support.p001v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.internal.zzdvu;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class zzq {
    private static List<Object> zze(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONArray) {
                obj = zze((JSONArray) obj);
            } else if (obj instanceof JSONObject) {
                obj = zzz((JSONObject) obj);
            }
            arrayList.add(obj);
        }
        return arrayList;
    }

    @Nullable
    public static Map<String, Object> zzpd(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject != JSONObject.NULL) {
                return zzz(jSONObject);
            }
            return null;
        } catch (Exception e) {
            Log.d("RawUserInfoParser", "Failed to parse JSONObject into Map.");
            throw new zzdvu(e);
        }
    }

    private static Map<String, Object> zzz(JSONObject jSONObject) throws JSONException {
        ArrayMap arrayMap = new ArrayMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            Object obj = jSONObject.get(str);
            if (obj instanceof JSONArray) {
                obj = zze((JSONArray) obj);
            } else if (obj instanceof JSONObject) {
                obj = zzz((JSONObject) obj);
            }
            arrayMap.put(str, obj);
        }
        return arrayMap;
    }
}
