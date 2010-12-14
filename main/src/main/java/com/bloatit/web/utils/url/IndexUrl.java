package com.bloatit.web.utils.url;

import java.util.Map;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public class IndexUrl extends Url {
public IndexUrl() {
    super("index"); 
}
public IndexUrl(Map<String, String> params) {
    super("index"); 
    parseParameterMap(params);
}


@Override 
protected void doRegister(Messages messages) { 
}
}
