package org.kobus.spring.domain.board;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.kobus.spring.domain.member.UserMapper;
import org.kobus.spring.service.board.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BoardController {

	private final BoardService boardService;
	private final UserMapper userMapper;

	public BoardController(BoardService boardService, UserMapper userMapper) {
		this.boardService = boardService;
		this.userMapper = userMapper;

	}

	@GetMapping("/board/list.do")
	public String list(Model model) {
		List<BoardDTO> boardList = boardService.getBoardList();
		model.addAttribute("list", boardList);
		return "board.boardList";
	}

	@GetMapping("/board/view.do")
	public String view(@RequestParam("brdID") int brdID, Model model, HttpSession session) {
		BoardDTO board = boardService.getBoardById(brdID);
		model.addAttribute("dto", board);

		// 세션에서 로그인 ID 꺼내서 kusID 조회 후 모델에 담기
		String loginId = (String) session.getAttribute("auth");
		if (loginId != null) {
			String loginKusID = userMapper.getKusIDById(loginId);
			model.addAttribute("loginKusID", loginKusID);
		}

		return "board.boardView";
	}

	@GetMapping("/board/write.do")
	public String writeForm() {
		return "board.boardWrite";
	}

	@PostMapping("/board/write.do")
	public String write(BoardDTO dto, HttpSession session) {
		String id = (String) session.getAttribute("auth");
		if (id == null)
			return "redirect:/page/logonMain.do";

		String kusID = userMapper.getKusIDById(id);
		dto.setKusID(kusID);

		boardService.saveBoard(dto);
		return "redirect:/board/list.do";
	}

	@PostMapping("/board/edit.do")
	public String edit(BoardDTO dto, HttpSession session) {
		String loginId = (String) session.getAttribute("auth");
		if (loginId == null) {
			return "redirect:/user/login.do";
		}

		String loginKusID = userMapper.getKusIDById(loginId); // 로그인 사용자
		BoardDTO original = boardService.getBoardById(dto.getBrdID()); // 원래 게시글 조회

		if (!loginKusID.equals(original.getKusID())) {
			// 작성자가 아닌데 수정 시도 → 거부
			return "redirect:/board/view.do?brdID=" + dto.getBrdID();
		}

		dto.setKusID(loginKusID); // 작성자 본인이면 kusID 설정
		boardService.updateBoard(dto);
		return "redirect:/board/view.do?brdID=" + dto.getBrdID();
	}

	@GetMapping("/board/edit.do")
	public String editForm(@RequestParam("brdID") int brdID, Model model) {
		BoardDTO dto = boardService.getBoardById(brdID);
		model.addAttribute("dto", dto);
		return "board.boardEdit"; // Tiles or ViewResolver로 연결
	}

	@PostMapping("/board/delete.do")
	public String delete(@RequestParam("brdID") int brdID, HttpSession session, RedirectAttributes rttr) {
	    String loginId = (String) session.getAttribute("auth");
	    if (loginId == null) {
	        return "redirect:/user/login.do";
	    }

	    boardService.deleteBoard(brdID);
	    rttr.addFlashAttribute("msg", "게시글이 삭제되었습니다.");
	    return "redirect:/board/list.do";
	}


}
