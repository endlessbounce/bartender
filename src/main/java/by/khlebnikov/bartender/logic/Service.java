package by.khlebnikov.bartender.logic;

import by.khlebnikov.bartender.repository.UserRepository;

public class Service {
    private UserRepository repo;

    public Service() {
        this.repo = new UserRepository();
    }

    public boolean checkUser(String email, String password){


        return false;
    }

    public boolean registerUser(String name, String email, String password){
        return repo.addUser(name, email, password);
    }
}
