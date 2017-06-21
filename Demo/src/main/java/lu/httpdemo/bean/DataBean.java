package lu.httpdemo.bean;

/**
 * Author: luqihua
 * Time: 2017/6/20
 * Description: Test
 */

public class DataBean {
    /**
     * userId : 2567
     * userType : 1
     */

    private String userId;
    private String userType;

    public DataBean(String userId, String userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
