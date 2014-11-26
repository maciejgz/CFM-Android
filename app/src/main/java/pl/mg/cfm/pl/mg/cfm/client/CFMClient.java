package pl.mg.cfm.pl.mg.cfm.client;

import pl.mg.cfm.pojo.EmployeePojo;

/**
 * Created by m on 2014-11-25.
 */
public interface CFMClient {

    public EmployeePojo login(String id, String password);


}
