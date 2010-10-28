package com.bloatit.framework.right;

public class ActorRight extends RightManager {

    public static class Email extends Private {}

    public static class ExternalAccount extends Private {}

    public static class InternalAccount extends PrivateReadOnly {}

    // public static class Login extends ReadOnly {}
    // public static class DateCreation extends ReadOnly {}

}
