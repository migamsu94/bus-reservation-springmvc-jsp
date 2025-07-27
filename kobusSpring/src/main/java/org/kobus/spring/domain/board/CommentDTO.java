package org.kobus.spring.domain.board;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CommentDTO {
    private int bcmID;          // 댓글 ID (PK)
    private int brdID;          // 게시글 ID (FK)
    private String kusID;       // 작성자 ID
    private String content;     // 댓글 내용
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp cmtDate;  // 작성일

    public int getBcmID() {
        return bcmID;
    }

    public void setBcmID(int bcmID) {
        this.bcmID = bcmID;
    }

    public int getBrdID() {
        return brdID;
    }

    public void setBrdID(int brdID) {
        this.brdID = brdID;
    }

    public String getKusID() {
        return kusID;
    }

    public void setKusID(String kusID) {
        this.kusID = kusID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCmtDate() {
        return cmtDate;
    }

    public void setCmtDate(Timestamp cmtDate) {
        this.cmtDate = cmtDate;
    }
}