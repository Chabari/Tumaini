package tumaini.tumaini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mwarachael on 3/14/2019.
 */

public class ExpandableListDataPump {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();



        List<String> Shares_and_dividends = new ArrayList<String>();
        Shares_and_dividends.add("Shares");
        Shares_and_dividends.add("Dividends");

        List<String> Loans = new ArrayList<String>();
        Loans.add("Normal Loan");
        Loans.add("Loan Advance");
        Loans.add("Wakulima Loan");
        Loans.add("Members with Loan");


        List<String> Funds = new ArrayList<String>();
        Funds.add("Risk Fund");
        Funds.add("Project Funds");


        List<String> Members = new ArrayList<String>();
        Members.add("Add new member");
        Members.add("Sacco Members");


        List<String> FinesOthers = new ArrayList<String>();
        FinesOthers.add("Fine");
        FinesOthers.add("Members with fine");



        expandableListDetail.put("Members", Members);
        expandableListDetail.put("Loans", Loans);
        expandableListDetail.put("Funds", Funds);
        expandableListDetail.put("Shares and Dividends", Shares_and_dividends);
        expandableListDetail.put("Fines", FinesOthers);
        return expandableListDetail;
    }
}
