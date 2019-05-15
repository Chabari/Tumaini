package tumaini.tumaini.Loans;

import java.util.Date;

import tumaini.tumaini.postID;

/**
 * Created by George on 5/6/2019.
 */

public class Members_With_Loan_List extends tumaini.tumaini.postID {
    private String final_amount,amount,id,document,fname,lname;
    private Date timeStamp;

    public Members_With_Loan_List() {

    }

    public Members_With_Loan_List(String final_amount, String amount, String id, String document, String fname, String lname, Date timeStamp) {
        this.final_amount = final_amount;
        this.amount = amount;
        this.id = id;
        this.document = document;
        this.fname = fname;
        this.lname = lname;
        this.timeStamp = timeStamp;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
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


