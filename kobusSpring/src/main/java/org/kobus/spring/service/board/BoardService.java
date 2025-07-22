package org.kobus.spring.service.board;

import org.springframework.stereotype.Service;

@Service
public class BoardService {
		
		// 승호님
		// BoardService 에서 오류가 나서 확인 해보니까 BoardMapper가 없는 상태에서
		// BoardMapper 를 주입하려고 하니까 해당 객체를 찾을수 없다는 오류가 떠서
		// 잠시 주석처리 했습니다..! 나중에 BoardMapper 만드시면 주석 풀고 계속 작업하시면 될거같습니다
		// 이정인드림
//    @Autowired BoardMapper boardMapper;
//    public List<BoardDTO> listAll() { return boardMapper.getBoardList(); }
//    public BoardDTO getDetail(int id) { 
//        boardMapper.increaseViews(id);
//        return boardMapper.getBoardDetail(id);
//    }
    // ... 기타 작성, 수정, 삭제 메서드
}

