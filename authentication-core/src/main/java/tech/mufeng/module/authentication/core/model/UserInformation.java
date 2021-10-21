package tech.mufeng.module.authentication.core.model;

import java.io.Serializable;

/**
 * @author T.J
 * @date 2021/10/14 11:29
 */
public class UserInformation {
    private Serializable uid;
    private String username;
    private String encodedPassword;

    public UserInformation(Serializable uid, String username, String encodedPassword) {
        this.uid = uid;
        this.username = username;
        this.encodedPassword = encodedPassword;
    }

    public Serializable getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setUid(Serializable uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }
}
