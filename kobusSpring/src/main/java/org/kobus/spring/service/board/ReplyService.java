package org.kobus.spring.service.board;

import java.util.List;

import org.kobus.spring.domain.board.CommentDTO;

public interface ReplyService {
    void insertComment(CommentDTO dto);
    List<CommentDTO> getCommentsByBoardId(int brdID);
    int updateComment(CommentDTO dto);
    int deleteComment(int bcmID, String kusID);

}