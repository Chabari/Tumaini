package tumaini.tumaini.FinesOthers;

import java.util.Date;

import tumaini.tumaini.postID;

/**
 * Created by George on 5/12/2019.
 */

public class Members_With_Fines_List extends tumaini.tumaini.postID {

    private String id_no;
    private Date timeStamp;

    public Members_With_Fines_List() {
    }

    public Members_With_Fines_List(String id_no, Date timeStamp) {
        this.id_no = id_no;
        this.timeStamp = timeStamp;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
