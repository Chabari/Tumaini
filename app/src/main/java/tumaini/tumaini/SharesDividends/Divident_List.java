package tumaini.tumaini.SharesDividends;

import java.util.Date;

import tumaini.tumaini.postID;

/**
 * Created by George on 5/10/2019.
 */

public class Divident_List extends tumaini.tumaini.postID {

    private String id_number;
    private Date timeStamp;

    public Divident_List() {
    }

    public Divident_List(String id_number, Date timeStamp) {
        this.id_number = id_number;
        this.timeStamp = timeStamp;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
