package ishchenko.a.testbt.SupportClasses;

/**
 * Created by andrey on 07.03.17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Group implements Serializable, Parcelable {

    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("gr_name")
    @Expose
    private String grName;
    public final static Parcelable.Creator<Group> CREATOR = new Creator<Group>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Group createFromParcel(Parcel in) {
            Group instance = new Group();
            instance.pk = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.grName = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Group[] newArray(int size) {
            return (new Group[size]);
        }

    };
    private final static long serialVersionUID = -4933365966867453107L;

    /**
     * No args constructor for use in serialization
     */
    public Group() {
    }

    /**
     * @param grName
     * @param pk
     */
    public Group(Integer pk, String grName) {
        super();
        this.pk = pk;
        this.grName = grName;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Group withPk(Integer pk) {
        this.pk = pk;
        return this;
    }

    public String getGrName() {
        return grName;
    }

    public void setGrName(String grName) {
        this.grName = grName;
    }

    public Group withGrName(String grName) {
        this.grName = grName;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pk);
        dest.writeValue(grName);
    }

    public int describeContents() {
        return 0;
    }

}
