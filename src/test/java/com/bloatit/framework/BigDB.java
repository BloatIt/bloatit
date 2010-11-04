package com.bloatit.framework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoDescription;
import com.bloatit.model.data.DaoExternalAccount;
import com.bloatit.model.data.DaoExternalAccount.AccountType;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoMember;
import com.bloatit.model.data.DaoTransaction;
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

public class BigDB {

    private static final int nbUsers = 100;

    public BigDB() {

        SessionManager.beginWorkUnit();

        // Create some users
        List<DaoMember> members = new ArrayList<DaoMember>();
        for (int i = 0; i < nbUsers; i++) {
            DaoMember member;
            member = DaoMember.createAndPersist("member " + i, new Integer(i).toString(), "mail@nowhere.com");
            member.setFullname("User " + i + " Fullname");
            member.setExternalAccount(DaoExternalAccount.createAndPersist(member, AccountType.IBAN, "code"));
            members.add(member);
            
            try {
                DaoTransaction
                        .createAndPersist(member.getInternalAccount(), member.getExternalAccount(), new BigDecimal("-1000"));
            } catch (final NotEnoughMoneyException e) {
                e.printStackTrace();
            }
            if (i % 500 == 0) {
                SessionManager.flush();
                SessionManager.clear();
            }
        }

        // Create Some Groups
        for (int i = 0; i < nbUsers / 4; i += 4) {
            final DaoGroup group = DaoGroup.createAndPersiste("group " + (i / 4), "plop@plop.com", DaoGroup.Right.PUBLIC);
            group.addMember(members.get(i), true);
            group.addMember(members.get(i + 1), false);
            group.addMember(members.get(i + 2), false);
            group.addMember(members.get(i + 3), false);
            if (i % 500 == 0) {
                SessionManager.flush();
                SessionManager.clear();
            }
        }
        for (int i = 0; i < nbUsers; i++) {
            final DaoDemand demand1 = DaoDemand.createAndPersist(members.get(i), new DaoDescription(
                    members.get(i), new Locale("fr"), fortune(140), fortune() + fortune() + fortune()));
            DaoDemand.createAndPersist(members.get(i),
                    new DaoDescription(members.get(i), new Locale("fr"), fortune(140), fortune() + fortune()
                            + fortune() + fortune()));

            int commentCount = (int) (Math.random() * 5);

            for (int j = 0; j < commentCount; j++) {

                final DaoComment comment = DaoComment.createAndPersist(members.get(i), fortune());
                createComment(comment);
                demand1.addComment(comment);
            }

            // TODO add Transaction
            // TODO add Translation
            // TODO add Contributions
            // TODO add Offers

            if (i % 100 == 0) {
                SessionManager.flush();
                SessionManager.clear();
            }
        }

        SessionManager.endWorkUnitAndFlush();

    }

    private void createComment(DaoComment comment) {
        final int nbComment = (int) (Math.random() * 15);
        for (int i = 0; i < nbComment; i++) {
            final int memberNum = (int) (Math.random() * nbUsers);
            final DaoComment other = DaoComment.createAndPersist(DaoMember.getByLogin("member " + memberNum), fortune());
            comment.addChildComment(other);
            if ((int) (Math.random() * 15) % 15 == 0) {
                createComment(other);
            }
        }
    }

    public static void main(String[] args) {
        new BigDB();
    }

    public static String fortune() {
        String text = "";
        try {
            String line;
            Process p = Runtime.getRuntime().exec("fortune");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                text += line;
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

        if (text.isEmpty()) {
            System.err.println("please install fortune for more fun (sudo apt-get install fortune-mod)");
            text = Long.toHexString(Double.doubleToLongBits(Math.random()));
        }

        return text;
    }

    private String fortune(int i) {
        String text = fortune();
        if (text.length() > i) {
            text = text.substring(0, i);
        }

        return text;
    }
}
