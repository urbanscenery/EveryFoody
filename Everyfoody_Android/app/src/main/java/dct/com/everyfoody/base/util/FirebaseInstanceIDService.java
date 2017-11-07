package dct.com.everyfoody.base.util;

/**
 * Created by jyoung on 2017. 8. 18..
 */

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        SharedPreferencesService.getInstance().load(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtil.d(this, "Refreshed token: " + refreshedToken);
        SharedPreferencesService.getInstance().setPrefData("fcm_token", refreshedToken);

    }
}


