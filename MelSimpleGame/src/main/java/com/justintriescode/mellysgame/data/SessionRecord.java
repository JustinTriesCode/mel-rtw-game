package com.justintriescode.mellysgame.data;

import java.time.LocalDate;

public class SessionRecord {
    public String date;  
    public String gameName; 
    public String severity; 
    public boolean isHard; 
    public int score;
    
    public SessionRecord() {} 

    public SessionRecord(String gameName, String severity, boolean isHard, int score) {
        this.date = LocalDate.now().toString();
        this.gameName = gameName;
        this.severity = severity;
        this.isHard = isHard;
        this.score = score;
    }
}
