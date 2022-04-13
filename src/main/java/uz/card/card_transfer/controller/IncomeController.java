package uz.card.card_transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.card.card_transfer.entity.Income;
import uz.card.card_transfer.payload.ApiResponse;
import uz.card.card_transfer.service.IncomeService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    IncomeService incomeService;

    @GetMapping
    public HttpEntity<?> getAll(HttpServletRequest request){
        List<Income> all = incomeService.getAll(request);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Integer id, HttpServletRequest request){
        Income one = incomeService.getById(id, request);
        return ResponseEntity.ok(one);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id,HttpServletRequest request){
        ApiResponse delete = incomeService.delete(id, request);
        return ResponseEntity.status(delete.isSuccess()?200:409).body(delete);
    }
}
