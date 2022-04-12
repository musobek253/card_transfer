package uz.card.card_transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.card.card_transfer.entity.Card;
import uz.card.card_transfer.payload.ApiResponse;
import uz.card.card_transfer.payload.CardDto;
import uz.card.card_transfer.repozitary.CardRepository;
import uz.card.card_transfer.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    @Autowired(required = false)
    CardRepository cardRepository;
    @Autowired
    JwtProvider jwtProvider;



    public List<Card> getAll(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        return cardRepository.findAllByUserName(username);
    }
    public Card getId(Integer id, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        Optional<Card> cardOptional = cardRepository.findById(id);
        if (!cardOptional.isPresent()) return new Card();
        if (!username.equals(cardOptional.get().getUserName())) return new Card();
        return cardOptional.get();
    }
    public ApiResponse addCard(CardDto cardDto,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        if(cardRepository.existsByCardNumber(cardDto.getCardNumber()))
            return new ApiResponse("Alreadiy exist by cardNumber",false);
        Card card = new Card();
        card.setCardNumber(cardDto.getCardNumber());
        card.setBalance(cardDto.getBalance());
        card.setUserName(username);
        card.setExpiredDate(cardDto.getExpiredDate());
        cardRepository.save(card);
        return new ApiResponse("succsessfully added",true);

    }
    public ApiResponse editCard(Integer id, CardDto cardDto, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        Optional<Card> cardOptional = cardRepository.findById(id);
        if (!cardOptional.isPresent())
            return new ApiResponse("CardNumber not found",false);
        if (!username.equals(cardOptional.get().getUserName()))
            return new ApiResponse("CardNumber not found",false);
        Card card = cardOptional.get();
        if (card.isActive()){
            if (cardDto.getBalance() >0)
                card.setBalance(cardDto.getBalance());
            else {
                return new ApiResponse("card balance is zero!", false);
            }
            if (cardDto.getExpiredDate() != null) {
                card.setExpiredDate(cardDto.getExpiredDate());
            }
        } else {
            return new ApiResponse("card blocked!", false);
        }
        cardRepository.save(card);
        return new ApiResponse("Card Edited!", true);
        }
    public ApiResponse delete(Integer id,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);//username
        Optional<Card> byId = cardRepository.findById(id);
        if (!byId.isPresent()) return new ApiResponse("This id not found",false);
        if (!username.equals(byId.get().getUserName())) return new ApiResponse(" username not found",false);
        cardRepository.deleteById(id);
        return new ApiResponse("Deleting successfully",false);
    }

    }

