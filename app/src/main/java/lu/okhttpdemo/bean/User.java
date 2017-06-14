package lu.okhttpdemo.bean;

/**
 * Created by 82172 on 2017/5/20.
 */
public class User {
    private String name;
    private String password;
    private int age;

    public User(String name, String password, int age) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
