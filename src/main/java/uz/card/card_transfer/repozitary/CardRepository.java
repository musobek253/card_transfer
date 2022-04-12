package uz.card.card_transfer.repozitary;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.card.card_transfer.entity.Card;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    boolean existsByCardNumber(String cardNumber);
    List<Card> findAllByUserName(String userName);
}
