package lu.httpdemo.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author: luqihua
 * Time: 2017/6/20
 * Description: HMacUtil
 */

public class HMacUtil {

    private static final String HMACSHA1_ALG = "HmacSHA1";


    public static String encodeParams(Context context, Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.append(entry.getValue());
        }

        return enCodeByKey(context, builder.toString());
    }

    /**
     * 加密字符串
     *
     * @param msg
     * @return base64字符串
     */
    @SuppressWarnings({"restriction", "static-access"})
    public static String enCodeByKey(Context context, String msg) {
        String base64 = "";
        try {
            // 密钥数组
            byte[] keyArray = getKeyByteArray(context);
            // 加密数据
            byte[] mac = hMacSha1(msg.getBytes(), keyArray);

            base64 = Base64.encodeToString(mac, Base64.DEFAULT);

        } catch (Exception e) {
            Log.d("HMacUtil", e.getMessage());
        }
        return base64;
    }

    private static byte[] getKeyByteArray(Context context) throws IOException {
        InputStream is = context.getAssets().open("fecar.key", AssetManager.ACCESS_BUFFER);

        if (is == null) return null;

        byte[] result = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[128];
            int len;
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            result = bos.toByteArray();
        } finally {
            if (bos != null) {
                bos.close();
            }
            is.close();
        }
        return result;
    }


    /**
     * 使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节.
     *
     * @param input 原始输入字符数组
     * @param key   HMAC-SHA1密钥
     */
    public static byte[] hMacSha1(byte[] input, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, HMACSHA1_ALG);
            Mac mac = Mac.getInstance(HMACSHA1_ALG);
            mac.init(secretKey);
            return mac.doFinal(input);
        } catch (GeneralSecurityException e) {
            Log.d("HMacUtil", e.getMessage());
        }
        return null;
    }
}
