package tumaini.tumaini;

import java.util.Date;

/**
 * Created by George on 5/3/2019.
 */

public class All_Members_List extends postID {
    private String id_number,fname,lname;
    private Date timeStamp;

    public All_Members_List() {
    }

    public All_Members_List(String id_number, String fname, String lname, Date timeStamp) {
        this.id_number = id_number;
        this.fname = fname;
        this.lname = lname;
        this.timeStamp = timeStamp;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}