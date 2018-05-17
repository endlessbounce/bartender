package test.khlebnikov.bartender.service;

import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.utility.CodeGenerator;
import by.khlebnikov.bartender.utility.HashCoder;
import by.khlebnikov.bartender.utility.TimeGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserServiceTest {
    private UserService userService = new UserService();
    private HashCoder hashCoder = new HashCoder();

    private String userEmail = "email@aaa.a";
    private String cookie = "008f3112-ca2c-463-b061-85ef8ad69b19";
    private String password = "poiw751he_hjgf2r";
    private int userId;

    private String prospectEmail = "ddd@aam.a";
    private String code;

    @Test
    public void testSaveUser() throws Exception {
        byte[] salt = hashCoder.getNextSalt();
        byte[] hash = hashCoder.hash(password.toCharArray(), salt).get();

        User user = new User("some name", userEmail, hash, salt);
        user.setUniqueCookie(cookie);
        Assert.assertTrue(userService.saveUser(user));
    }

    @Test
    public void testSaveProspectUser() throws Exception {
        code = CodeGenerator.uniqueId();

        ProspectUser prospectUser = new ProspectUser("new prospect",
                prospectEmail,
                new byte[1],
                new byte[1],
                TimeGenerator.expirationTime(),
                code
        );

        Assert.assertTrue(userService.saveProspectUser(prospectUser));
    }

    @Test
    public void testFindUserByEmail() throws Exception {
        userId = userService.findUserByEmail(userEmail)
                .map(User::getId)
                .orElse(0);

        Assert.assertNotEquals(userId, 0);
    }

    @Test
    public void testFindUserById() throws Exception {
        Assert.assertTrue(userService.findUserById(String.valueOf(userId))
                .isPresent());
    }

    @Test
    public void testFindUserByCookie() throws Exception {
        Assert.assertTrue(userService.findUserByCookie(cookie)
                .isPresent());
    }

    @Test
    public void testFindProspectByCode() throws Exception {
        Assert.assertTrue(userService.findProspectByCode(code)
                .isPresent());
    }

    @Test
    public void testCheckUser() throws Exception {
        Assert.assertTrue(userService.checkUser(userEmail, password)
                .isPresent());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User("updated name", userEmail, new byte[1], new byte[1]);
        user.setId(userId);
        Assert.assertTrue(userService.updateUser(user));
    }

    @Test
    public void testIsProspectRegistered() throws Exception {
        Assert.assertTrue(userService.isProspectRegistered(prospectEmail));
    }

    @Test
    public void testDeleteProspectUser() throws Exception {
        Assert.assertTrue(userService.deleteProspectUser(code));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Assert.assertTrue(userService.deleteUser(userId));
    }

    @Test
    public void testIsFavouriteCocktail() throws Exception {
        Assert.assertFalse(userService.isFavouriteCocktail(userId, 1));
    }

}