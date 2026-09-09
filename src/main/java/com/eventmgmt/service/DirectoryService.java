package com.eventmgmt.service;

import java.sql.SQLException;
import java.util.List;

import com.eventmgmt.dao.ClubDAO;
import com.eventmgmt.dao.UserDAO;
import com.eventmgmt.models.Club;
import com.eventmgmt.models.User;

public class DirectoryService {
    private final UserDAO userDAO;
    private final ClubDAO clubDAO;

    public DirectoryService(UserDAO userDAO, ClubDAO clubDAO) {
        this.userDAO = userDAO;
        this.clubDAO = clubDAO;
    }

    public List<User> users() {
        try { return userDAO.getAll(); }
        catch (SQLException e) { throw new BusinessException("Could not load users.", e); }
    }

    public List<Club> clubs() {
        try { return clubDAO.getAll(); }
        catch (SQLException e) { throw new BusinessException("Could not load clubs.", e); }
    }
}
