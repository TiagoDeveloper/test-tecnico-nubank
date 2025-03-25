package br.com.nubank.service;

import br.com.nubank.model.FinancialOperation;
import br.com.nubank.model.FinancialTimelineData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class OperationProcessingService {


    public OperationProcessingService() {

    }

    public List<FinancialTimelineData> process(List<FinancialOperation> financialOperations) {
        var result = new ArrayList<FinancialTimelineData>();
        var media = new BigDecimal(0);
        var lucro = new BigDecimal(0);
        var receita = new BigDecimal(0);
        var quantidade = new BigDecimal(0);
        var saldo = new BigDecimal(0);

        for (FinancialOperation financialOperation : financialOperations) {

            if (financialOperation.operation().equals("buy")) {
                receita = quantidade.compareTo(BigDecimal.ZERO) > 0 ? receita : BigDecimal.ZERO;
                saldo = quantidade.compareTo(BigDecimal.ZERO) > 0 ? saldo : BigDecimal.ZERO;

                quantidade = quantidade.add(financialOperation.quantity());
                receita = receita.add(financialOperation.totalOperation());
                media = quantidade.compareTo(BigDecimal.ZERO) > 0 ? receita.divide(quantidade, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                lucro = BigDecimal.ZERO;
            } else {
                quantidade = quantidade.subtract(financialOperation.quantity());
                lucro = profitCalculation(financialOperation.quantity(), media, financialOperation.unitCost());
            }

            saldo = saldo.add(lucro);

            result.add(new FinancialTimelineData(financialOperation.operation(), media, lucro, quantidade, saldo, financialOperation.totalOperation()));
        }

        return result;
    }

    private static BigDecimal profitCalculation(BigDecimal quantidade, BigDecimal valorCompra, BigDecimal valorVenda) {
        return quantidade.multiply(valorVenda).subtract(quantidade.multiply(valorCompra));
    }

}
