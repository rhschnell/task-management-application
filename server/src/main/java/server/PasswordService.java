package server;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private String password = "group1";

    public boolean isPasswordCorrect(String enteredPassword) {
        return enteredPassword.equals(password);
    }
}