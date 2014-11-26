package pl.mg.cfm.pl.mg.cfm.client;

import org.apache.http.client.HttpClient;

import pl.mg.cfm.network.HttpClientFactory;
import pl.mg.cfm.pojo.EmployeePojo;

/**
 * Created by m on 2014-11-25.
 */
public class CFMClientImpl implements CFMClient{

    private HttpClient httpClient;

    @Override
    public EmployeePojo login(String id, String password) {

        if(this.httpClient ==null){
            httpClient = HttpClientFactory.getHttpClient();
        }


        return null;
    }


}
