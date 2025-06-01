package iti.jets.misk.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password";
        String storedHash = "$2a$10$y2g4ws7UJs.OaIDFxD.JnOYPpYpIzJiy57BdT.dYlb0hhsXp2eh/a";
        System.out.println("Encoded password: " + encoder.encode(rawPassword));
        System.out.println("Matches: " + encoder.matches(rawPassword, storedHash));
        System.out.println("New hash: " + encoder.encode("password"));
    }
}