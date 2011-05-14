package com.bloatit.model.right;

import com.bloatit.model.Rights;

public interface RestrictedInterface {

    void authenticate(final AuthenticatedUserToken token);

    Rights getRights();
}
