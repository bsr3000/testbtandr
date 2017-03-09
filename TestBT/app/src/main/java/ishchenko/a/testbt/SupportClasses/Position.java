package ishchenko.a.testbt.SupportClasses;

/**
 * Created by andrey on 07.03.17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Position implements Serializable, Parcelable
{

    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("pos_name")
    @Expose
    private String posName;
    public final static Parcelable.Creator<Position> CREATOR = new Creator<Position>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Position createFromParcel(Parcel in) {
            Position instance = new Position();
            instance.pk = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.posName = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Position[] newArray(int size) {
            return (new Position[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7388926537751505535L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Position() {
    }

    /**
     *
     * @param posName
     * @param pk
     */
    public Position(Integer pk, String posName) {
        super();
        this.pk = pk;
        this.posName = posName;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Position withPk(Integer pk) {
        this.pk = pk;
        return this;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public Position withPosName(String posName) {
        this.posName = posName;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pk);
        dest.writeValue(posName);
    }

    public int describeContents() {
        return 0;
    }

}