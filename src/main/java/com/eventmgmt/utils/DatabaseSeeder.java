package com.eventmgmt.utils;

import com.eventmgmt.dao.ClubDAO;
import com.eventmgmt.dao.EventDAO;
import com.eventmgmt.dao.UserDAO;
import com.eventmgmt.models.Club;
import com.eventmgmt.models.Event;
import com.eventmgmt.models.User;
import com.eventmgmt.security.PasswordHasher;

public class DatabaseSeeder {
    public static void seed() {
        try {
            UserDAO userDAO = new UserDAO();
            ClubDAO clubDAO = new ClubDAO();
            EventDAO eventDAO = new EventDAO();

            if (userDAO.getAll().isEmpty()) {
                User admin = new User("System Admin", "admin@campus.edu", "ADMIN");
                User president = new User("Rahim Ahmed", "rahim@campus.edu", "PRESIDENT");
                User student = new User("Karim Ullah", "karim@campus.edu", "STUDENT");

                admin.setPasswordHash(PasswordHasher.hash("admin123"));
                president.setPasswordHash(PasswordHasher.hash("president123"));
                student.setPasswordHash(PasswordHasher.hash("student123"));

                userDAO.insert(admin);
                userDAO.insert(president);
                userDAO.insert(student);

                Club roboticsClub = new Club("Robotics Club", president.getId());
                clubDAO.insert(roboticsClub);

                Event techFest = new Event("Robo Carnival 2026", 50000.0, "APPROVED", roboticsClub.getId());
                eventDAO.insert(techFest);

            } else {
                for (User user : userDAO.getAll()) {
                    if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
                        String password = switch (user.getRole().toUpperCase()) {
                            case "ADMIN" -> "admin123";
                            case "PRESIDENT" -> "president123";
                            default -> "student123";
                        };
                        userDAO.updatePasswordHash(user.getId(), PasswordHasher.hash(password));
                    }
                }
            }
            ensureStudent(userDAO, "Ayesha Siddiqua", "ayesha@campus.edu");
            ensureStudent(userDAO, "Nusrat Jahan", "nusrat@campus.edu");
            ensureStudent(userDAO, "Tanvir Hasan", "tanvir@campus.edu");
            ensureStudent(userDAO, "Mehedi Rahman", "mehedi@campus.edu");
            User president = userDAO.findByEmail("rahim@campus.edu");
            if (president != null) {
                ensureClub(clubDAO, "IITSEC", president.getId());
                ensureClub(clubDAO, "IITDU CTF", president.getId());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Database seeding failed", e);
        }
    }

    private static void ensureStudent(UserDAO userDAO, String name, String email) throws Exception {
        if (userDAO.findByEmail(email) != null) return;
        User student = new User(name, email, "STUDENT");
        student.setPasswordHash(PasswordHasher.hash("student123"));
        userDAO.insert(student);
    }

    private static void ensureClub(ClubDAO clubDAO, String name, int presidentId) throws Exception {
        if (clubDAO.findByName(name) == null) clubDAO.insert(new Club(name, presidentId));
    }
}
