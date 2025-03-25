package br.com.nubank.model;

import java.math.BigDecimal;

public record FinancialTimelineData(String operationType, BigDecimal mediaPonderada, BigDecimal lucro, BigDecimal quantidade, BigDecimal saldo, BigDecimal totalOperation) {
}
