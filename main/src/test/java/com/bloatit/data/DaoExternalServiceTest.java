package com.bloatit.data;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.EnumSet;

import org.junit.Test;

import com.bloatit.data.DaoExternalService.RightLevel;
import com.bloatit.framework.utils.datetime.DateUtils;

public class DaoExternalServiceTest extends DataTestUnit {

    @Test
    public final void testDaoExternalServiceDaoMemberStringStringEnumSetOfRightLevel() {
        final EnumSet<RightLevel> set = EnumSet.of(RightLevel.CREATE_FEATURE, RightLevel.CONTRIBUTE, RightLevel.CREATE_OFFER);
        final DaoExternalService service = new DaoExternalService(fred, "Test", "azertyuiop", set);

        assertEquals(service.getLevels(), set);
        assertEquals(service.getName(), "Test");
        assertEquals(service.getToken(), "azertyuiop");
        assertEquals(service.isAuthorized(), false);
    }

    @Test
    public final void testAuthorize() {
        final EnumSet<RightLevel> set = EnumSet.of(RightLevel.CREATE_FEATURE, RightLevel.CONTRIBUTE, RightLevel.CREATE_OFFER);
        final DaoExternalService service = new DaoExternalService(fred, "Test", "azertyuiop", set);
        final Date tomorrow = DateUtils.tomorrow();
        service.authorize("accesstoken", "refreshtoken", tomorrow);

        assertEquals(service.isAuthorized(), true);

        assertEquals(service.getToken(), "accesstoken");
        assertEquals(service.getRefreshToken(), "refreshtoken");
        assertEquals(service.getExpirationDate(), tomorrow);
        assertEquals(service.getLevels(), set);
        assertEquals(service.getName(), "Test");
    }
}
