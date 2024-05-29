package com.epsi.epsistore.repository;


import com.epsi.core.entities.Role;
import com.epsi.core.repositories.RoleRepository;
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
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WebAppConfiguration
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts = {"classpath:Sqls/FullDatabase_DNL.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,scripts = {"classpath:Sqls/ClearDB.sql"})
public class RoleRepoTest {
    @Autowired
    private RoleRepository repo;

    @Test
    public void findAll_Test() {
        // GIVEN
        // WHEN
        List<Role> res = repo.findAll();
        // THEN
        assertEquals(3, res.size());
    }

    @Test
    public void findByNomRole_Test() {
        // GIVEN
        Role role = new Role("TEST", 5);
        // WHEN
        repo.save(role);
        Role res = repo.findByRoleName("TEST").orElse(null);
        // THEN
        assertNotNull(res);
        assertEquals(role, res);
    }

    @Test
    public void save_Test() {
        // GIVEN
        Role role = new Role("TEST", 1);
        // WHEN
        int before = repo.findAll().size();
        Role res = repo.save(role);
        int after = repo.findAll().size();
        // THEN
        assertEquals(after, before+1);
        assertEquals(role, res);
    }

    @Test
    public void delete_Test() {
        // GIVEN
        Role role = new Role("TEST", 1);
        // WHEN
        int before = repo.findAll().size();
        repo.save(role);
        int between = repo.findAll().size();
        repo.delete(role);
        int after = repo.findAll().size();
        // THEN
        assertEquals(3, before);
        assertEquals(4, between);
        assertEquals(3, after);
    }
}
