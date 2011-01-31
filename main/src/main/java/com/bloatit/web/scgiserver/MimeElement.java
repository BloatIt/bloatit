package com.bloatit.web.scgiserver;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MimeElement {
    private final List<Byte> content;
    private final Map<String, String> header;
    
    public MimeElement(List<Byte> content, Map<String, String> header) {
        super();
        this.content = content;
        this.header = header;
    }

    public List<Byte> getContent() {
        return content;
    }

    public Map<String, String> getHeader() {
        return header;
    }
    
    public String toString(){
        String result = "";
        
        result += "[HEADER]\n";
        for(Entry<String, String> entry : header.entrySet()){
            result += "\t ["+entry.getKey()+"]: "+entry.getValue() + "\n";
        }
        result += "[CONTENT] \n \t";
        for(byte b : content){
            result+= (char)(b&0xff);
        }
        
        return result;
    }
}
