package lu.httpdemo.bean;

/**
 * Author: luqihua
 * Time: 2017/6/20
 * Description: JsonBody
 */

public class JsonBody<T> {

    /**
     * sysName : postman
     * operator : hulei
     * timestamp : 201705271532
     * randomString : 25A4b1
     * sgin : q9JcN7Uk3NcnBgGJJCICbwc6pPk=
     * data : {"userId":"2567","userType":"1"}
     */

    private String sysName;
    private String operator;
    private String timestamp;
    private String random;
    private String sign;
    private T data;

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return sysName + operator + timestamp + random;
    }
}
