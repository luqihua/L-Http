package lu.httpdemo.bean;

/**
 * Author: luqihua
 * Time: 2017/6/19
 * Description: HttpResult
 */

public class HttpResult<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "code=" + code
                + "\nmsg=" + msg
                + "\ndata=" + data;
    }
}
