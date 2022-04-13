package uz.card.card_transfer.repozitary;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.card.card_transfer.entity.Outcome;

import java.util.List;

public interface OutcomeRepository extends JpaRepository<Outcome,Integer> {
    List<Outcome>findByFromCard_UserName(String fromCard_userName);
}
