package org.kobus.spring.service.board;

import java.util.List;

import org.kobus.spring.domain.board.CommentDTO;
import org.kobus.spring.mapper.board.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public void insertComment(CommentDTO dto) {
        replyMapper.insertComment(dto);
    }
    
    @Override
    public List<CommentDTO> getCommentsByBoardId(int brdID) {
        return replyMapper.getCommentsByBoardId(brdID);
    }
    @Override
    public int updateComment(CommentDTO dto) {
        return replyMapper.updateComment(dto);
    }

    @Override
    public int deleteComment(int bcmID, String kusID) {
        return replyMapper.deleteComment(bcmID, kusID);
    }

}
