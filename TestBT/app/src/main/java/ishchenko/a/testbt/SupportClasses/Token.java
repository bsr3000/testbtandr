package ishchenko.a.testbt.SupportClasses;

/**
 * Created by andrey on 08.03.17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Token implements Serializable, Parcelable {

    @SerializedName("token")
    @Expose
    private String token;
    public final static Parcelable.Creator<Token> CREATOR = new Creator<Token>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Token createFromParcel(Parcel in) {
            Token instance = new Token();
            instance.token = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Token[] newArray(int size) {
            return (new Token[size]);
        }

    };
    private final static long serialVersionUID = 5494440776749125751L;

    /**
     * No args constructor for use in serialization
     */
    public Token() {
    }

    /**
     * @param token
     */
    public Token(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Token withToken(String token) {
        this.token = token;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(token);
    }

    public int describeContents() {
        return 0;
    }

}