package com.example.infoapp;

public class MessageInformation {
    String sender, message, time, date, id;

    public MessageInformation(String M_date, String M_message, String M_sender,String M_time,String M_id){
        this.sender=M_sender;
        this.message=M_message;
        this.date=M_date;
        this.time=M_time;
        this.id=M_id;
    }
    public MessageInformation(){

    }
    //getters
    public String getSender() {
        return sender;
    }
    public String getMessage() {
        return message;
    }
    public String getTime() {
        return time;
    }
    public String getDate() {
        return date;
    }
    public String getId(){return  id;}

    //setters
    public void setSender(String M_sender) {
        this.sender = M_sender;
    }
    public void setMessage(String M_message) {
        this.message = M_message;
    }
    public void setTime(String M_time) {
        this.time = M_time;
    }
    public void setDate(String M_date) {
        this.date = M_date;
    }
    public void setId(String M_id){this.id=M_id;}
}
