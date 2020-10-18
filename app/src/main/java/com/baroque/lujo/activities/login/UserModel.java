package com.baroque.lujo.activities.login;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//Implementing serializable so that it can be passed using intents to other activity
public class UserModel implements Serializable {
    private String fullName;
    @SerializedName("firstname")
    private String firstName;
    @SerializedName("lastname")
    private String lastName;
    private String email ;
    private String phone_prefix;
    private String phone;
    private String avatar;
    private String approved;
    private int customer_id;

    @SerializedName("membership_plan")
    private MembershipPlan membershipPlan;

    private String referral_code;
    private String baroque_id;
    private double points;
    private String preferences;

    private String oldPhonePrefix;
    private String oldPhoneNumber;
    private String alpha2Code;

    private SignUpErrors errorType;
    private String errorMessage;

    public String getPhonePrefix() {
        return phone_prefix;
    }
    public void setPhonePrefix(String phonePrefix) {
        this.phone_prefix = phonePrefix;
    }

    public enum SignUpErrors{
        FIRST_NAME,LAST_NAME, EMAIL, PHONE_NUMBER, TERMS_AND_CONDITIONS
    }

    public SignUpErrors getErrorType() {
        return errorType;
    }
    public void setErrorType(SignUpErrors errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFullName() { return firstName + " " + lastName ; }
    public String getFirstName() {
        return firstName ;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName ;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phone;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phone = phoneNumber;
    }

    public String getOldPhonePrefix() { return oldPhonePrefix; }
    public void setOldPhonePrefix(String oldPhonePrefix) { this.oldPhonePrefix = oldPhonePrefix; }

    public String getOldPhoneNumber() { return oldPhoneNumber; }
    public void setOldPhoneNumber(String oldPhoneNumber) { this.oldPhoneNumber = oldPhoneNumber; }

    public String getAlpha2Code() { return alpha2Code; }
    public void setAlpha2Code(String oldPhoneNumber) { this.alpha2Code = alpha2Code; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getApproved() { return approved; }
    public void setApproved(String approved) { this.approved = approved; }

    public int getCustomer_id() { return customer_id; }
    public void setCustomer_id(int customer_id) { this.customer_id = customer_id; }

    public MembershipPlan getMembershipPlan() { return membershipPlan; }
    public void setMembershipPlan(MembershipPlan membershipPlan) { this.membershipPlan = membershipPlan; }

    public String getReferral_code() { return referral_code; }
    public void setReferral_code(String referral_code) { this.referral_code = referral_code; }

    public String getBaroque_id() { return baroque_id; }
    public void setBaroque_id(String baroque_id) { this.baroque_id = baroque_id; }

    public double getPoints() { return points; }
    public void setPoints(double points) { this.points = points; }

    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }

    public UserModel(String firstName, String lastName, String email, String phonePrefix, String phone, String oldPhonePrefix, String oldPhoneNumber, String alpha2Code) {
        this.firstName= firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone_prefix = phonePrefix;
        this.phone = phone;
        this.oldPhonePrefix = oldPhonePrefix;
        this.oldPhoneNumber = oldPhoneNumber;
        this.alpha2Code = alpha2Code;
    }

    //This method is used at change phone number click
    public UserModel getUserWithSwapPhoneNumbers(){
        //keep the phone prefix and just empty the phone number
        UserModel user = new UserModel(this.firstName, this.lastName,  this.email, this.phone_prefix, "", this.phone_prefix, this.phone, this.alpha2Code);
        return user;
    }

    public class MembershipPlan implements Serializable{
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public int id;
        private String plan;
        private String price;
        private String target;
        private String expiration;
    }

}

