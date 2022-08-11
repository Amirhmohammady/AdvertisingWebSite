package com.mycompany.advertising.service.api;

import com.mycompany.advertising.service.util.OnlineAdvertiseData;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Amir on 8/7/2022.
 */
public interface OnlineAdvertiseDataService {
    OnlineAdvertiseData getData(URL url) throws IOException;
}
