package edu.du.teamportpoilo_pyl;

import edu.du.teamportpoilo_pyl.mapper.BoardMapper;
import edu.du.teamportpoilo_pyl.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class TeamPortpoiloPylApplicationTests {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpSession httpSession;

    @Test
    void contextLoads() {
        boardMapper.boardSelectAllByPage(1, 10);
        log.info(boardMapper.boardSelectAllByPage(1, 10).toString());
        log.info(boardMapper.boardSelectById(1L).toString());
    }

    @Test
    void userLoginInfo() {
        log.info(userMapper.selectByUserId("admin").toString());
    }

}
