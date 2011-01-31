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
    
    public String getHeaderField(String key){
        return header.get(key);
    }
    
    public String toString(){
        char CR = '\r';
        char LF = '\n';
        String result = "";
        
        result += "[HEADER]\n";
        for(Entry<String, String> entry : header.entrySet()){
            result += "\t["+entry.getKey()+"]: "+entry.getValue() + "\n";
        }
        result += "[CONTENT] \n \t";
        
        char previousChar = '0';
        for(byte b : content){
            char currentChar = (char)(b&0xff);
            if(currentChar == CR){
                result += "{CR}";
            } else if (currentChar == LF) {
                result += "{LF}";
            } else {
                if(previousChar == CR || previousChar == LF){
                    result += "\n\t";
                }
                result += currentChar;
            }
            previousChar = currentChar;
        }
        
        return result;
    }
}
