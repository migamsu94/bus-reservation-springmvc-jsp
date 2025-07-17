package org.kobus.spring.mapper;

import org.kobus.spring.domain.FreePassPaymentDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FreePassPaymentDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    public int insert(FreePassPaymentDTO dto) {
        return sqlSession.insert("freepass.insertPayment", dto);
    }
}

