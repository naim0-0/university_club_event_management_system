package com.eventmgmt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.eventmgmt.dao.ClubDAO;
import com.eventmgmt.dao.EventDAO;
import com.eventmgmt.dao.UserDAO;
import com.eventmgmt.utils.DatabaseInitializer;
import com.eventmgmt.utils.DatabaseSeeder;
import com.eventmgmt.service.AuthService;

public class DatabaseSmokeTest {
    @BeforeClass
    public static void initializeSchema() { DatabaseInitializer.initialize(); DatabaseSeeder.seed(); }

    @Test
    public void existingDatabaseCanBeReadThroughDaos() throws Exception {
        assertNotNull(new UserDAO().getAll());
        assertNotNull(new ClubDAO().getAll());
        assertNotNull(new EventDAO().getAll());
        assertFalse(new UserDAO().getAll().isEmpty());
    }

    @Test
    public void migratedAdminAccountCanSignIn() {
        assertNotNull(new AuthService(new UserDAO()).login("admin@campus.edu", "admin123"));
    }
}
