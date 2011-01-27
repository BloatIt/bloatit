package com.bloatit.framework;

import com.bloatit.mail.MailServer;

public class FrameworkLauncher {
    public static void launch(){
        MailServer.init();
    }
}
