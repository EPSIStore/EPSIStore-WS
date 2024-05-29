package com.epsi.epsistore.repository;

import com.epsi.core.entities.Role;
import com.epsi.core.entities.User;
import com.epsi.core.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WebAppConfiguration
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts = {"classpath:Sqls/FullDatabase_DNL.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,scripts = {"classpath:Sqls/ClearDB.sql"})
public class UserRepoTest {
    @Autowired
    private UserRepository repo;

    @Test
    public void findAll_Test() {
        // GIVEN
        // WHEN
        List<User> res = repo.findAll();
        // THEN
        assertEquals(3, res.size());
    }

    @Test
    public void save_Test() {
        // GIVEN
        User user = new User(15, "username", "email", "password", new Role("ROLE_USER", 1), null);
        // WHEN
        int before = repo.findAll().size();
        User res = repo.save(user);
        int after = repo.findAll().size();
        // THEN
        assertEquals(after, before+1);
        assertEquals(user, res);
    }

    @Test
    public void delete_Test() {
        // GIVEN
        User user = new User(15, "username", "email", "password", new Role("ROLE_USER", 1), null);
        // WHEN
        int before = repo.findAll().size();
        repo.save(user);
        int between = repo.findAll().size();
        repo.delete(user);
        int after = repo.findAll().size();
        // THEN
        assertEquals(3, before);
        assertEquals(4, between);
        assertEquals(3, after);
    }
}
