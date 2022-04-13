package uz.card.card_transfer.repozitary;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.card.card_transfer.entity.Card;
import uz.card.card_transfer.entity.Income;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Integer> {
    List<Income> findByFromCard_UserName(String fromCard_userName);
}
