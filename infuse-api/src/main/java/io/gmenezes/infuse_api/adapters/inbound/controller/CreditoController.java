package io.gmenezes.infuse_api.adapters.inbound.controller;

import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController (" /api/creditos/")
@RequiredArgsConstructor
public class CreditoController {


    @GetMapping("/{numeroNfse}")
    public List<CreditoResponse> buscarPorNfse(@NotBlank @PathVariable String numeroNfse) {

        return null;
    }


}
