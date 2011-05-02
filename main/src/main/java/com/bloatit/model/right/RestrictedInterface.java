package com.bloatit.model.right;

import com.bloatit.model.Rights;

public interface RestrictedInterface {

    void authenticate(final AuthToken token);

    Rights getRights();
}
