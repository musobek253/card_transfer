package uz.card.card_transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.card.card_transfer.entity.Card;
import uz.card.card_transfer.entity.Income;
import uz.card.card_transfer.entity.Outcome;
import uz.card.card_transfer.payload.ApiResponse;
import uz.card.card_transfer.payload.OutcomeDto;
import uz.card.card_transfer.repozitary.CardRepository;
import uz.card.card_transfer.repozitary.IncomeRepository;
import uz.card.card_transfer.repozitary.OutcomeRepository;
import uz.card.card_transfer.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OutcomeService {
    private final OutcomeRepository outcomeRepository;
    private final JwtProvider jwtProvider;
    private final CardRepository cardRepository;
    private final IncomeRepository incomeRepository;
    @Autowired(required = false)
    public OutcomeService(OutcomeRepository outcomeRepository, JwtProvider jwtProvider, CardRepository cardRepository, IncomeRepository incomeRepository) {
        this.outcomeRepository = outcomeRepository;
        this.jwtProvider = jwtProvider;
        this.cardRepository = cardRepository;
        this.incomeRepository = incomeRepository;
    }
    public List<Outcome> getAll(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        return outcomeRepository.findByFromCard_UserName(username);
    }
    public Outcome getById(Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        Optional<Outcome> outcomeOptional = outcomeRepository.findById(id);
        if (!outcomeOptional.isPresent()) return new Outcome();
        if (!username.equals(outcomeOptional.get().getFromCard().getUserName())) return new Outcome();
        return outcomeOptional.get();
    }
    public ApiResponse addOutcome(OutcomeDto outcomeDto,HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        token = token.substring(7);

        String username = jwtProvider.getUsernameFromToken(token);
        Optional<Card> optionalFromCard = cardRepository.findById(outcomeDto.getFromCardId());
        if (!optionalFromCard.isPresent()) return new ApiResponse("from Card not found", false);
        Optional<Card> toCardOpti = cardRepository.findById(outcomeDto.getToCardId());
        if (!toCardOpti.isPresent()) return new ApiResponse("to Card not found", false);
        if (outcomeDto.getAmount() < 0) return new ApiResponse("the value must be greater than zero", false);
        if (!username.equals(optionalFromCard.get().getUserName()))
            return new ApiResponse(" This is not your Card !", false);
        if (outcomeDto.getAmount() < 1000)
            return new ApiResponse("It is not possible to send less than 1000 funds", false);
        if (outcomeDto.getAmount() > (optionalFromCard.get().getBalance() - outcomeDto.getAmount() * outcomeDto.getCommission()))
            return new ApiResponse("there is not enough money in your account", false);
        if (optionalFromCard.get().getCardNumber().equals(toCardOpti.get().getCardNumber()))
            return new ApiResponse("Choose another card to send money because the sender's card number is the same", false);
        if (optionalFromCard.get().isActive() && toCardOpti.get().isActive()) {
            Outcome outcome = new Outcome();
            outcome.setAmount(outcomeDto.getAmount());
            outcome.setCommissionPercent(outcomeDto.getCommission());
            outcome.setFromCard(optionalFromCard.get());
            outcome.setToCard(toCardOpti.get());
            outcome.setDate(new java.sql.Date(new Date(System.currentTimeMillis()).getTime()));

            outcomeRepository.save(outcome);
            Income income = new Income();

            income.setAmount(outcomeDto.getAmount());
            income.setFromCard(optionalFromCard.get());
            income.setToCard(toCardOpti.get());
            income.setDate((new java.sql.Date(new Date(System.currentTimeMillis()).getTime())));

            incomeRepository.save(income);
            Card fromCard = optionalFromCard.get();
            Card toCard = toCardOpti.get();
            fromCard.setBalance(fromCard.getBalance() - (outcomeDto.getAmount() + outcomeDto.getAmount() * outcomeDto.getCommission()));
            cardRepository.save(fromCard);
            toCard.setBalance(toCard.getBalance() + outcomeDto.getAmount());
            cardRepository.save(toCard);
            return new ApiResponse("Succsesfully add", true);
        }
        return new ApiResponse("one of the carrd is blocked",false);
    }
    public ApiResponse delete(Integer id,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        Optional<Outcome> byId = outcomeRepository.findById(id);
        if (!byId.isPresent()) return new ApiResponse("Id not found!",false);
        if (!username.equals(byId.get().getFromCard().getUserName()))
            return new ApiResponse("Username not found!",false);
        outcomeRepository.deleteById(id);
        return new ApiResponse("Delete success",true);
    }

}



