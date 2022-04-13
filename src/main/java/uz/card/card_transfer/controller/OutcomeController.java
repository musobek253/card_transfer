package uz.card.card_transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.card.card_transfer.entity.Outcome;
import uz.card.card_transfer.payload.ApiResponse;
import uz.card.card_transfer.payload.OutcomeDto;
import uz.card.card_transfer.service.OutcomeService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/outcome")
public class OutcomeController {


   private final OutcomeService outcomeService;
    @Autowired
    public OutcomeController(OutcomeService outcomeService) {
        this.outcomeService = outcomeService;
    }

    @GetMapping
    public HttpEntity<?> getAll(HttpServletRequest request){
        List<Outcome> all = outcomeService.getAll(request);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id, HttpServletRequest request){
        Outcome one = outcomeService.getById(id, request);
        return ResponseEntity.ok(one);
    }

    @PostMapping
    public HttpEntity<?> add(@RequestBody OutcomeDto outcomeDto, HttpServletRequest httpServletRequest) {

        ApiResponse response = outcomeService.addOutcome(outcomeDto, httpServletRequest);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id,HttpServletRequest request){
        ApiResponse delete = outcomeService.delete(id, request);
        return ResponseEntity.status(delete.isSuccess()?200:409).body(delete);
    }

}
