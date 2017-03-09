package ishchenko.a.testbt.SupportClasses;

/**
 * Created by andrey on 07.03.17.
 */


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable, Parcelable
{

    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("position")
    @Expose
    private Position position;
    @SerializedName("salary")
    @Expose
    private Integer salary;
    @SerializedName("group")
    @Expose
    private Group group;
    public final static Parcelable.Creator<User> CREATOR = new Creator<User>() {


        @SuppressWarnings({
                "unchecked"
        })
        public User createFromParcel(Parcel in) {
            User instance = new User();
            instance.pk = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.username = ((String) in.readValue((String.class.getClassLoader())));
            instance.email = ((String) in.readValue((String.class.getClassLoader())));
            instance.firstName = ((String) in.readValue((String.class.getClassLoader())));
            instance.lastName = ((String) in.readValue((String.class.getClassLoader())));
            instance.photo = ((String) in.readValue((String.class.getClassLoader())));
            instance.position = ((Position) in.readValue((Position.class.getClassLoader())));
            instance.salary = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.group = ((Group) in.readValue((Group.class.getClassLoader())));
            return instance;
        }

        public User[] newArray(int size) {
            return (new User[size]);
        }

    }
            ;
    private final static long serialVersionUID = -1010374155573669719L;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param position
     * @param lastName
     * @param username
     * @param email
     * @param salary
     * @param group
     * @param firstName
     * @param photo
     * @param pk
     */
    public User(Integer pk, String username, String email, String firstName, String lastName, String photo, Position position, Integer salary, Group group) {
        super();
        this.pk = pk;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.position = position;
        this.salary = salary;
        this.group = group;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public User withPk(Integer pk) {
        this.pk = pk;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public User withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User withPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public User withPosition(Position position) {
        this.position = position;
        return this;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public User withSalary(Integer salary) {
        this.salary = salary;
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User withGroup(Group group) {
        this.group = group;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pk);
        dest.writeValue(username);
        dest.writeValue(email);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(photo);
        dest.writeValue(position);
        dest.writeValue(salary);
        dest.writeValue(group);
    }

    public int describeContents() {
        return 0;
    }

}