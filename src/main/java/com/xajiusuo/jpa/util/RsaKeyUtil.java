package com.xajiusuo.jpa.util;

import com.alibaba.fastjson.JSON;
import com.xajiusuo.jpa.param.e.Configs;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hadoop on 19-8-23.
 */
public final class RsaKeyUtil {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RASPrivateKey";
    public static final String SIGNATURE_ALGORITHM = "MD5WithRSA";

    private static final int MAX_ENCRYPT_BLOCK = 117;

    public static final int MAX_DECRYPT_BLOCK = 128;

    public static String getPublicKeyStr(Map<String,Object> keyMap) throws Exception {
        Key key = (Key)keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    public static String getPrivateKeyStr(Map<String,Object> keyMap) throws Exception {
        Key key = (Key)keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }



    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey publicKey = keyFactory.generatePrivate(keySpec);
        return publicKey;
    }

    public static String encryptBASE64(byte[] key) throws Exception{
        return (new BASE64Encoder()).encode(key);
    }


    public static byte[] sign(byte[] data,String privateKeyStr) throws Exception{
        PrivateKey prik = getPrivateKey(privateKeyStr);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initSign(prik);
        sig.update(data);
        return sig.sign();
    }

    public static boolean verify(byte[] data,byte[] sign,String publicKeyStr) throws Exception{
        PublicKey pubk = getPublicKey(publicKeyStr);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initVerify(pubk);
        sig.update(data);
        return sig.verify(sign);
    }

    /***
     * 公钥加密算法
     * @param plainText
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] plainText,String publicKeyStr) throws Exception{
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        int inputLen = plainText.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        int i = 0;
        byte[] cache;
        while (inputLen - offset > 0){
            if(inputLen - offset > MAX_ENCRYPT_BLOCK){
                cache = cipher.doFinal(plainText,offset,MAX_ENCRYPT_BLOCK);
            }else{
                cache = cipher.doFinal(plainText,offset,inputLen - offset);
            }
            out.write(cache,0,cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encrypText = out.toByteArray();
        out.close();
        return encrypText;
    }


    /***
     * 私钥加密算法
     * @param plainText
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] encrypt0(byte[] plainText,String privateKeyStr) throws Exception{
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,privateKey);
        int inputLen = plainText.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        int i = 0;
        byte[] cache;
        while (inputLen - offset > 0){
            if(inputLen - offset > MAX_ENCRYPT_BLOCK){
                cache = cipher.doFinal(plainText,offset,MAX_ENCRYPT_BLOCK);
            }else{
                cache = cipher.doFinal(plainText,offset,inputLen - offset);
            }
            out.write(cache,0,cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encrypText = out.toByteArray();
        out.close();
        return encrypText;
    }

    /***
     * 公钥解密算法
     * @param encryptText
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] decrypt0(byte[] encryptText,String publicKeyStr) throws Exception{
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        int inputLen = encryptText.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        int i = 0;
        byte[] cache;
        while (inputLen - offset > 0){
            if(inputLen - offset > MAX_DECRYPT_BLOCK){
                cache = cipher.doFinal(encryptText,offset,MAX_DECRYPT_BLOCK);
            }else{
                cache = cipher.doFinal(encryptText,offset,inputLen - offset);
            }
            out.write(cache,0,cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] plainText = out.toByteArray();
        out.close();
        return plainText;
    }


    /***
     * 私钥解密算法
     * @param encryptText
     * @param priateKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encryptText,String priateKeyStr) throws Exception{
        PrivateKey privateKey = getPrivateKey(priateKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        int inputLen = encryptText.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        int i = 0;
        byte[] cache;
        while (inputLen - offset > 0){
            if(inputLen - offset > MAX_DECRYPT_BLOCK){
                cache = cipher.doFinal(encryptText,offset,MAX_DECRYPT_BLOCK);
            }else{
                cache = cipher.doFinal(encryptText,offset,inputLen - offset);
            }
            out.write(cache,0,cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] plainText = out.toByteArray();
        out.close();
        return plainText;
    }

    public static Map<String,Object> initKey() throws Exception{
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String,Object> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY,publicKey);
        keyMap.put(PRIVATE_KEY,privateKey);
        return keyMap;
    }

    public static String byteToHex(byte b){
        String hex = Integer.toHexString(b & 0xFF);
        if(hex.length() < 2){
            return "0" + hex;
        }
        return hex;
    }

    public static String bytesToHex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static byte hexToByte(String inHex){
        return (byte) Integer.parseInt(inHex,16);
    }

    public static byte[] hexToByteArray(String inHex){
        int hexLen = inHex.length();
        if(hexLen % 2 == 1){
            hexLen++;
            inHex = "0" + inHex;
        }
        byte[] result = new byte[hexLen / 2];
        int j = 0;
        for (int i = 0; i < hexLen; i+=2) {
            result[j] = hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }

    public final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTx9hZYxjLTt6QMlVs/+5OeAkNf38sGblYHzGv" +
            "C6KNBjjTZ6AUtuoH9CHSgkohLRHhMimRki1pB39n3NA+m43/ObXWr/AkfuMcLOIslo9NyRvuI2BE" +
            "QKWjmRVSut7BAgyK6DIod72q1My11s4ZYcKBx8ydD2hvB+RqNUEOtXpYJQIDAQAB";

    public static void seri(String str){
        String ser = MD5utils.getMd5(str);
        if(StringUtils.isBlank(Configs.COM_SERIAL.getValue())){//新增序列号
            int n = new Random().nextInt(50);
            MD5utils.getMd5(str);
            while(n-- > 0){
                ser = MD5utils.getMd5(ser);
            }
            Configs.update(Configs.COM_SERIAL.name(),ser);
        }else{//校验序列号
            String ser0 = Configs.COM_SERIAL.getValue();
            int n = 50;
            while (n-- >= 0){
                if(ser0.equals(ser)){
                    break;
                }
                ser = MD5utils.getMd5(ser);
            }
            if(n <= 0){
                Configs.COM_LICENCE.getValue();
                Configs.update(Configs.COM_LICENCE.name(),"");
                seri(str);
                return;
            }
        }
        String str1 = ser0(Configs.COM_SERIAL.getValue());
        if(!verify(str1,Configs.COM_LICENCE.getValue(), publicKey)){
            Configs.update(Configs.COM_LICENCE.name(),"");
        }
        if(StringUtils.isBlank(Configs.COM_LICENCE.getValue())){
            System.out.println("产品未注册,请将序列号发送给管理员:" + str1);
        }

    }

    public static String ser0(String s){
        s = MD5utils.getMd5(s);
        s = s.substring(0,8) + "-" + s.substring(8,16) + "-" + s.substring(16,24) + "-" + s.substring(24);
        return s;
    }

    private static Date validity = null;

    public static boolean verify(String ser,String lince,String key){
        if(StringUtils.isBlank(lince) || StringUtils.isBlank(ser)){
            return false;
        }
        try {
            Map map = JSON.parseObject(new String(decrypt0(hexToByteArray(lince),key)));
            String proName = (String) map.get("proName");
            if(proName.equals(ser)){
                if("forever".equals(map.get("date"))){
                    validity = new Date(System.currentTimeMillis() + 99L * 365 * 24 * 60 * 60 * 1000);
                    Configs.update(Configs.COM_CURR_TIME.name(),"");
                }else{
                    validity = sdf.parse((String) map.get("date"));
                    validity.setTime(validity.getTime() + 24L * 60 * 60 * 1000);
                    if(StringUtils.isBlank(Configs.COM_CURR_TIME.getValue()) || Long.parseLong(Configs.COM_CURR_TIME.getValue()) < System.currentTimeMillis()){
                        Configs.update(Configs.COM_CURR_TIME.name(),System.currentTimeMillis() + "");
                    }
                }
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /***
     * 进行有效期认证检查
     * @return
     */
    public static boolean authenticate(){
        if(StringUtils.isBlank(Configs.COM_CURR_TIME.getValue())){
            return true;
        }
        if(Long.parseLong(Configs.COM_CURR_TIME.getValue()) - 24L * 60 * 60 * 1000 > System.currentTimeMillis()){
            throw new RuntimeException("服务时钟异常,请调整准确时间.");
        }
        return validity != null && validity.after(new Date());
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


//    public static void main(String[] args) throws Exception{
//        Map<String, Object> map = initKey();
//        byte[] cipherText;
//        String input = "proName:village,date:2019-10-01";
//
//        String publicKey = getPublicKeyStr(map);
//        String publicKey = getPrivateKeyStr(map);
//        System.out.println("公钥 === \n" + publicKey);
//        System.out.println("私钥 === \n" + publicKey);
//
//        System.out.println("明文 === " + input);
//
//        cipherText = encrypt(input.getBytes(),publicKey);
//
//        String mw = bytesToHex(cipherText);
//        System.out.println("密文 === " + mw);
//
////        byte[] plainText = decrypt(cipherText,publicKey);
//        byte[] plainText = decrypt(hexToByteArray(mw),publicKey);
//        System.out.println("解密 === " + new String(plainText));
//
//        System.out.println("验证签名 ......");
//
//        String str = "被签名的内容";
//        System.out.println("\n原文:" + str);
//
//        byte[] signature = sign(str.getBytes(),publicKey);
//        boolean status = verify(str.getBytes(),signature,publicKey);
//        System.out.println("结果 === " + status);
//
//    }

//    public static void main(String[] args) throws Exception {
//        Map<String,String> map = new HashMap<>();
////        map.put("proName","%0NVPO53-*ZO*-+3V-113*10V4-P/0O442O");//village
//        map.put("proName","PZ3/*2O%-NP%-V3-/-NOV%V-24-P%2*NO45");//substation
//
//        map.put("date","forever");
////        map.put("date","2019-10-31");
//
//        String lince = JSON.toJSONString(map);
//        String pk = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJPH2FljGMtO3pAyVWz/7k54CQ1/" +
//                "fywZuVgfMa8Loo0GONNnoBS26gf0IdKCSiEtEeEyKZGSLWkHf2fc0D6bjf85tdav8CR+4xws4iyW" +
//                "j03JG+4jYERApaOZFVK63sECDIroMih3varUzLXWzhlhwoHHzJ0PaG8H5Go1QQ61elglAgMBAAEC" +
//                "gYAn+/ZpmQpoV9qYIUZDy6tqx8eLQ2fhQsSHN0l2FyvQBFLzwCHr+OGxbTiJgH8WIt+nkq70mBx5" +
//                "QnISZAhkKCpu7b9AgsNMYR3Z3Z0BeiDlNq9rPtqKqUX6KJoqfCmQwlMuYwUK11sDlJ+YFBH+fYFp" +
//                "yjrnuDeGsAXsjDtUbqbrMQJBAN3mK4TtRTgGljvM0kqSIHYK2pk3eujMEvG7Q22wNo44pzUlnwrm" +
//                "cmYvUTMIAnvUTCKL3BWo0ojoayGAsnri7ysCQQCqfb+U+5u0b659MrPDFx2fdYf8aw9lH3L9zALa" +
//                "FoEKfrMoojHQi8IcOCZGpygsGLkHamuT8srf74Ox3n7Zd63vAkAhNxz4jtO3qTS/ys8HnowBX2qN" +
//                "WMKrKjEDwjY7qw7fhxdwWytNZGVnalnLO72zb3neCfVtwJGTMMcogq00u9d1AkEAl/6xnSXPg8aJ" +
//                "Xi6kmonyK+ZBTOU5fWLTRrbp1/p2IqAgI1quZIkkjbn1Tcnag62qz5StweGTwp8q3plIp6IN2wJA" +
//                "SyGVAh4/S2iMip1Vxc0NeuLO9RTzouCOfz7iAEVnKX/FgDPDhKMM/O751tMT2Lck7cgWRs4C+6rD" +
//                "A0Ri1e0s6g==";
//
//        byte[] cipherText = encrypt0(lince.getBytes(),pk);
//        String mw = bytesToHex(cipherText);
//        System.out.println(mw);
//    }

}
