import cn.hutool.core.io.FileUtil;
import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.TransactionInfoResponse;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import com.apple.itunes.storekit.verification.VerificationException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lineying.common.CommonConstant;
import com.lineying.common.SecureConfig;
import com.lineying.entity.AppEntity;
import com.lineying.manager.AppcodeManager;
import com.lineying.util.JsonUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test {

    public static void main(String[] args) throws Exception {

        //获取到产品id:1234239300 订单号:2000000748615906 - 2000000577510234
        String appcode = "mathcalc";
        String transactionId = "2000000748615906";

        JWSTransactionDecodedPayload payload = new Test().getTransactionFromApple(appcode, transactionId);

        System.out.println("======>>>" + payload);
    }


    /**
     * 解签交易信息
     * @param transactionId
     * @throws IOException
     * @throws APIException
     * @throws VerificationException
     * @throws VerificationException
     */
    public JWSTransactionDecodedPayload getTransactionFromApple(String appcode, String transactionId) throws IOException, APIException, VerificationException, VerificationException {

        AppEntity entity = AppcodeManager.getEntity(appcode);
        if (entity == null) {
            return null;
        }
        String bundleId = entity.getBundleId();
        long appleId = entity.getAppleId();

        String keyId = SecureConfig.APPLE_KEY_ID;
        String issuerId = SecureConfig.APPLE_ISSUER_ID;

        ClassLoader loader = getClass().getClassLoader();
        System.out.println("loader::" + loader);
        URL authKeyPath = loader.getResource(SecureConfig.APPLE_AUTH_KEY_PATH);
        URL comRootCert = loader.getResource(SecureConfig.APPLE_COMPUTER_ROOT_CERT_PATH);
        URL incRootCert = loader.getResource(SecureConfig.APPLE_INC_ROOT_CERT_PATH);
        URL rootCaG2 = loader.getResource(SecureConfig.APPLE_ROOT_CA_G2);
        URL rootCaG3 = loader.getResource(SecureConfig.APPLE_ROOT_CA_G3);
        System.out.println("authKeyPath::" + authKeyPath);
        System.out.println("authKeyPath2::" + authKeyPath.getPath());
        System.out.println("comRootCert::" + comRootCert);
        System.out.println("comRootCert2::" + comRootCert.getPath());
        System.out.println("incRootCert::" + incRootCert);
        System.out.println("incRootCert2::" + incRootCert.getPath());
        System.out.println("rootCaG2::" + rootCaG2);
        System.out.println("rootCaG2-2::" + rootCaG2.getPath());
        System.out.println("rootCaG3::" + rootCaG3);
        System.out.println("rootCaG3-2::" + rootCaG3.getPath());
        String encodedKey = FileUtil.readString(authKeyPath, CommonConstant.CHARSET);
        Environment environment = Environment.SANDBOX;
        Set<InputStream> rootCAs =  Set.of(
                new FileInputStream(comRootCert.getPath()),
                new FileInputStream(incRootCert.getPath()),
                new FileInputStream(rootCaG2.getPath()),
                new FileInputStream(rootCaG3.getPath())
        );

        //创建appleStoreServer对象
        AppStoreServerAPIClient client = new AppStoreServerAPIClient(encodedKey, keyId, issuerId, bundleId, environment);
        //根据传输的订单号获取订单信息
        TransactionInfoResponse sendResponse = client.getTransactionInfo(transactionId);
        Boolean onlineChecks = false ;
        SignedDataVerifier signedDataVerifier = new SignedDataVerifier(rootCAs, bundleId, appleId, environment, onlineChecks);
        String signedPayLoad = sendResponse.getSignedTransactionInfo();
        //对订单信息进行解析得到订单信息
        JWSTransactionDecodedPayload payload = signedDataVerifier.verifyAndDecodeTransaction(signedPayLoad);
        //进行订单信息处理
        System.out.println("payload::" + signedPayLoad + "\n" + payload);
        return payload;
    }

}
