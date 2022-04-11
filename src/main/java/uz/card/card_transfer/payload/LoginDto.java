package uz.card.card_transfer.payload;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
}