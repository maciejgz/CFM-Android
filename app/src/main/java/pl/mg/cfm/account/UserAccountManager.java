package pl.mg.cfm.account;

import android.content.Context;

import java.io.Serializable;

import pl.mg.cfm.pojo.EmployeePojo;

/**
 * Klasa obudowująca obiekt uzytkownika i zapisująca dane lokalnie
 * Created by m on 2014-11-25.
 */
public class UserAccountManager implements Serializable{
    private EmployeePojo employee;

    public UserAccountManager() {

    }

    /**
     * Zapis ustawien do przyszłego logowania.
     *
     * @param context
     * @return
     */
    public boolean saveUserCredentials(Context context) {
        return false;
    }


    public void setEmployee(EmployeePojo employee) {
        this.employee = employee;
    }

    public EmployeePojo getEmployee() {
        return employee;
    }
}
