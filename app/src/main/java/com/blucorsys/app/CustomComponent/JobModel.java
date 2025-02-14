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
    private String jobid_details;
    private String first_name;

    public String getAccept_jobid() {
        return accept_jobid;
    }

    public void setAccept_jobid(String accept_jobid) {
        this.accept_jobid = accept_jobid;
    }

    private String accept_jobid;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    private String mobileno;

    public String getWages() {
        return wages;
    }

    public void setWages(String wages) {
        this.wages = wages;
    }

    private String wages;

    public String getContractorid() {
        return contractorid;
    }

    public void setContractorid(String contractorid) {
        this.contractorid = contractorid;
    }

    private String contractorid;

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    private String payment_date;

    public String getRuserid() {
        return ruserid;
    }

    public void setRuserid(String ruserid) {
        this.ruserid = ruserid;
    }

    private String ruserid;

    public String getSite_detail() {
        return site_detail;
    }

    public void setSite_detail(String site_detail) {
        this.site_detail = site_detail;
    }

    private String site_detail;

    public String getWages_paid() {
        return wages_paid;
    }

    public void setWages_paid(String wages_paid) {
        this.wages_paid = wages_paid;
    }

    private String wages_paid;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String latitude;
    private String longitude;

    public String getLabour_id() {
        return labour_id;
    }

    public void setLabour_id(String labour_id) {
        this.labour_id = labour_id;
    }

    private String labour_id;

    public String getTrack_jobid() {
        return track_jobid;
    }

    public void setTrack_jobid(String track_jobid) {
        this.track_jobid = track_jobid;
    }

    private String track_jobid;

    public String getJob_applied_id() {
        return job_applied_id;
    }

    public void setJob_applied_id(String job_applied_id) {
        this.job_applied_id = job_applied_id;
    }

    private String job_applied_id;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }



    public String getJobid_details() {
        return jobid_details;
    }

    public void setJobid_details(String jobid_details) {
        this.jobid_details = jobid_details;
    }



    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    private String job_id;

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
