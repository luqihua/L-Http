package lu.httpdemo.bean;

/**
 * Author: luqihua
 * Time: 2017/6/19
 * Description: User
 */

public class User {

    /**
     * name : luqihua
     * password : helloworld
     * age : 20
     */

    private String name;
    private String password;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
