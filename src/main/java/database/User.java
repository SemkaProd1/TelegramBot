package database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity (name = "user")
public class User {


    @Id
    private
    int id;

    @Column
    private
    String userName;

    @Column
    private
    String authorName;

    @Column
    private
    String token;

    @Column
    private
    Boolean isAdmin;

    @Column
    private
    int userId;

    @Column
    private
    String info;

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id; }

    public int getUserId() {return userId;}

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getInfo() {return info;}

    public void setInfo(String info) {this.info = info;}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getisAdmin() {
        return isAdmin;
    }

    public void setisAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public User(int id, String userName, String authorName, String token, Boolean isAdmin,int userId, String info) {
        this.id = id;
        this.userName = userName;
        this.authorName = authorName;
        this.token = token;
        this.isAdmin = isAdmin;
        this.info = info;
        this.userId = userId;
    }
    public User(){

    }
    public void reset() {
        // Setup the instance
        this.id = 0;
        this.userName = null;
        this.authorName = null;
        this.token = null;
        this.isAdmin = null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", token='" + token + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}

