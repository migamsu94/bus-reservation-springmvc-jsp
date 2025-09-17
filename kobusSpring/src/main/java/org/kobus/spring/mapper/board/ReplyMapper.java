package org.kobus.spring.mapper.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kobus.spring.domain.board.CommentDTO;

@Mapper
public interface ReplyMapper {
    void insertComment(CommentDTO dto);

	List<CommentDTO> getCommentsByBoardId(int brdID);
	int updateComment(CommentDTO commentDTO);
	int deleteComment(@Param("bcmID") int bcmID, @Param("kusID") String kusID);

}


