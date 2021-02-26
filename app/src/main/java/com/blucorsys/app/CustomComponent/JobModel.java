package com.blucorsys.app.CustomComponent;

import android.os.Parcel;
import android.os.Parcelable;

public class JobModel implements Parcelable {
    private String id;
    private String location ;
    private String create_datetime;
    private String cattype_id;
    private String wage;
    private String cat_english;
    private String cat_hindi;
    private String cat_marathi;

    public JobModel() {

    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    private String no;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getCattype_id() {
        return cattype_id;
    }

    public void setCattype_id(String cattype_id) {
        this.cattype_id = cattype_id;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public String getCat_english() {
        return cat_english;
    }

    public void setCat_english(String cat_english) {
        this.cat_english = cat_english;
    }

    public String getCat_hindi() {
        return cat_hindi;
    }

    public void setCat_hindi(String cat_hindi) {
        this.cat_hindi = cat_hindi;
    }

    public String getCat_marathi() {
        return cat_marathi;
    }

    public void setCat_marathi(String cat_marathi) {
        this.cat_marathi = cat_marathi;
    }

    public static Creator<JobModel> getCREATOR() {
        return CREATOR;
    }

    public JobModel(Parcel in) {
        id = in.readString();
        location = in.readString();
        create_datetime = in.readString();
        cattype_id = in.readString();
        wage = in.readString();
        cat_english = in.readString();
        cat_hindi = in.readString();
        cat_marathi = in.readString();
    }

    public static final Creator<JobModel> CREATOR = new Creator<JobModel>() {
        @Override
        public JobModel createFromParcel(Parcel in) {
            return new JobModel(in);
        }

        @Override
        public JobModel[] newArray(int size) {
            return new JobModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(location);
        dest.writeString(create_datetime);
        dest.writeString(cattype_id);
        dest.writeString(wage);
        dest.writeString(cat_english);
        dest.writeString(cat_hindi);
        dest.writeString(cat_marathi);
    }
}
