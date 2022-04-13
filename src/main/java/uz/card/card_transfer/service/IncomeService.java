package uz.card.card_transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.card.card_transfer.entity.Income;
import uz.card.card_transfer.payload.ApiResponse;
import uz.card.card_transfer.repozitary.IncomeRepository;
import uz.card.card_transfer.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    private final JwtProvider jwtProvider;
    private final IncomeRepository incomeRepository;

    @Autowired(required = false)
    public IncomeService(JwtProvider jwtProvider, IncomeRepository incomeRepository) {
        this.jwtProvider = jwtProvider;
        this.incomeRepository = incomeRepository;
    }

    public List<Income> getAll(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        return incomeRepository.findByFromCard_UserName(username);
    }

    public Income getById(Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        Optional<Income> optionalIncome = incomeRepository.findById(id);
        if (!optionalIncome.isPresent())
            return new Income();
        if (!username.equals(optionalIncome.get().getFromCard().getUserName())) return new Income();
        return optionalIncome.get();
    }

    public ApiResponse delete(Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String username = jwtProvider.getUsernameFromToken(token);
        Optional<Income> byId = incomeRepository.findById(id);
        if (!byId.isPresent()) return new ApiResponse("Id not found!", false);
        if (!username.equals(byId.get().getFromCard().getUserName()))
            return new ApiResponse("Username not found!", false);
        incomeRepository.deleteById(id);
        return new ApiResponse("Delete success", true);
    }
}
