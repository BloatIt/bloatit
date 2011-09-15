package com.bloatit.data;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.EnumSet;

import org.junit.Test;

import com.bloatit.data.DaoExternalServiceMembership.RightLevel;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.i18n.Language;

public class DaoExternalServiceTest extends DataTestUnit {

    @Test
    public final void testDaoExternalServiceDaoMemberStringStringEnumSetOfRightLevel() {
        final EnumSet<RightLevel> set = EnumSet.of(RightLevel.CONTRIBUTE, RightLevel.CREATE_OFFER);
        DaoExternalService service = DaoExternalService.createAndPersist(fred,
                                                                         null,
                                                                         DaoDescription.createAndPersist(fred, null, Language.FR, "title", "coucou"));

        final DaoExternalServiceMembership membership = DaoExternalServiceMembership.createAndPersist(fred, service, "Test", set);

        assertEquals(membership.getLevels(), set);
        assertEquals(membership.getToken(), "Test");
        assertEquals(membership.isAuthorized(), false);
    }

    @Test
    public final void testAuthorize() {
        final EnumSet<RightLevel> set = EnumSet.of(RightLevel.CONTRIBUTE, RightLevel.CREATE_OFFER);
        DaoExternalService service = DaoExternalService.createAndPersist(fred,
                                                                         null,
                                                                         DaoDescription.createAndPersist(fred, null, Language.FR, "title", "coucou"));

        final DaoExternalServiceMembership membership = DaoExternalServiceMembership.createAndPersist(fred, service, "Test", set);
        final Date tomorrow = DateUtils.tomorrow();
        membership.authorize("accesstoken", "refreshtoken", tomorrow);

        assertEquals(membership.isAuthorized(), true);

        assertEquals(membership.getToken(), "accesstoken");
        assertEquals(membership.getRefreshToken(), "refreshtoken");
        assertEquals(membership.getExpirationDate(), tomorrow);
        assertEquals(membership.getLevels(), set);
    }
}
