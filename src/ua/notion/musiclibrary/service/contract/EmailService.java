package ua.notion.musiclibrary.service.contract;

public interface EmailService {

    String sendPasswordCode(String toAddress, String username);

}
