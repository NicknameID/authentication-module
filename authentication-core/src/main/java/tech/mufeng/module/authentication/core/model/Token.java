package tech.mufeng.module.authentication.core.model;

/**
 * @author T.J
 * @date 2021/10/14 15:40
 */
public class Token {
    private String uid;
    private String sign;

    public Token(String uid, String sign) {
        this.uid = uid;
        this.sign = sign;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
