package com.bloatit.framework;

import java.math.BigDecimal;
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

    private static final int nbUsers = 1000;

    public BigDB() {

        SessionManager.beginWorkUnit();

        // Create some users
        for (int i = 0; i < nbUsers; i++) {
            DaoMember member;
            member = DaoMember.createAndPersist("member " + i, new Integer(i).toString(), "mail@nowhere.com");
            member.setFullname("User " + i + " Fullname");
            member.setExternalAccount(DaoExternalAccount.createAndPersist(member, AccountType.IBAN, "code"));
            try {
                DaoTransaction.createAndPersist(member.getInternalAccount(), member.getExternalAccount(), new BigDecimal("-1000"));
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
            group.addMember(DaoMember.getByLogin("member " + i), true);
            group.addMember(DaoMember.getByLogin("member " + (i + 1)), false);
            group.addMember(DaoMember.getByLogin("member " + (i + 2)), false);
            group.addMember(DaoMember.getByLogin("member " + (i + 3)), false);
            if (i % 500 == 0) {
                SessionManager.flush();
                SessionManager.clear();
            }
        }
        for (int i = 0; i < nbUsers; i++) {
            final DaoDemand demand1 = DaoDemand.createAndPersist(DaoMember.getByLogin("member " + i), new DaoDescription(DaoMember.getByLogin("member " + i),
                                                                                                                         new Locale("fr"),
                                                                                                                         "titre " + i,
                                                                                                                         "Description " + i));
            final DaoDemand demand2 = DaoDemand.createAndPersist(DaoMember.getByLogin("member " + i), new DaoDescription(DaoMember.getByLogin("member " + i),
                                                                                                                         new Locale("fr"),
                                                                                                                         "titre " + (i * 2),
                                                                                                                         "Description " + (i * 2)));

            final DaoComment comment1 = DaoComment.createAndPersist(DaoMember.getByLogin("member " + i),
                                                                    Long.toHexString(Double.doubleToLongBits(Math.random())));
            final DaoComment comment2 = DaoComment.createAndPersist(DaoMember.getByLogin("member " + i),
                                                                    Long.toHexString(Double.doubleToLongBits(Math.random())));
            createComment(comment1);
            createComment(comment2);
            demand1.addComment(comment1);
            demand2.addComment(comment2);

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
            final DaoComment other = DaoComment.createAndPersist(DaoMember.getByLogin("member " + memberNum),
                                                                 Long.toHexString(Double.doubleToLongBits(Math.random())));
            comment.addChildComment(other);
            if ((int) (Math.random() * 15) % 15  == 0) {
                createComment(other);
            }
        }
    }

    public static void main(String[] args) {
        new BigDB();
    }
}
