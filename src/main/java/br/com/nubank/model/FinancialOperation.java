package br.com.nubank.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record FinancialOperation(String operation, @JsonProperty("unit-cost") BigDecimal unitCost, BigDecimal quantity) {

    public BigDecimal totalOperation() {
        return quantity.multiply(unitCost);
    }

}
