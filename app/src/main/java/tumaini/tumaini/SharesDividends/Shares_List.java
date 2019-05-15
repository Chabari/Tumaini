package tumaini.tumaini.SharesDividends;

import java.util.Date;

import tumaini.tumaini.postID;

/**
 * Created by George on 5/8/2019.
 */

public class Shares_List extends tumaini.tumaini.postID {

    private String id_number;
    private Date timeStamp;

    public Shares_List() {
    }

    public Shares_List(String id_number, Date timeStamp) {
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
