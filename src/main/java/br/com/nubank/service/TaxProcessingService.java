package br.com.nubank.service;

import br.com.nubank.model.FinancialTax;
import br.com.nubank.model.FinancialTimelineData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TaxProcessingService {

    public TaxProcessingService() {

    }

    public List<FinancialTax> process(List<FinancialTimelineData> financialTimelineData) {
        return financialTimelineData.stream().map(e -> {
            var tax = new BigDecimal("0.00");

            if(e.operationType().equals("buy"))
                return new FinancialTax(tax);

            var test = e.saldo().add(e.lucro());

            if(test.compareTo(new BigDecimal("20000.00")) < 1 || e.totalOperation().compareTo(new BigDecimal("20000.00")) < 1)
                return new FinancialTax(tax);

            var taxValue = e.saldo().multiply(new BigDecimal("0.2")).setScale(2, RoundingMode.HALF_UP);

            tax = tax.add(taxValue);

            return new FinancialTax(tax);
        }).toList();
    }
}
