package ishchenko.a.testbt.SupportClasses;

/**
 * Created by andrey on 08.03.17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Docs implements Serializable, Parcelable {

    @SerializedName("pk")
    @Expose
    private Integer pk;
    @SerializedName("doc")
    @Expose
    private String doc;
    public final static Parcelable.Creator<Docs> CREATOR = new Creator<Docs>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Docs createFromParcel(Parcel in) {
            Docs instance = new Docs();
            instance.pk = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.doc = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Docs[] newArray(int size) {
            return (new Docs[size]);
        }

    };
    private final static long serialVersionUID = 3298324136250486361L;

    /**
     * No args constructor for use in serialization
     */
    public Docs() {
    }

    /**
     * @param doc
     * @param pk
     */
    public Docs(Integer pk, String doc) {
        super();
        this.pk = pk;
        this.doc = doc;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Docs withPk(Integer pk) {
        this.pk = pk;
        return this;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public Docs withDoc(String doc) {
        this.doc = doc;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pk);
        dest.writeValue(doc);
    }

    public int describeContents() {
        return 0;
    }

}