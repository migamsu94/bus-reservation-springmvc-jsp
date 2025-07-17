package org.kobus.spring.service.board;

import java.util.List;

import org.kobus.spring.board.dto.BoardDTO;
import org.kobus.spring.mapper.board.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    @Autowired BoardMapper boardMapper;
    public List<BoardDTO> listAll() { return boardMapper.getBoardList(); }
    public BoardDTO getDetail(int id) { 
        boardMapper.increaseViews(id);
        return boardMapper.getBoardDetail(id);
    }
    // ... 기타 작성, 수정, 삭제 메서드
}

