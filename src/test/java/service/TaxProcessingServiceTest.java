package service;

import br.com.nubank.model.FinancialTimelineData;
import br.com.nubank.service.TaxProcessingService;
import br.com.nubank.util.JsonParse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class TaxProcessingServiceTest {

    TaxProcessingService taxProcessingService = new TaxProcessingService();

    @Test
    public void caseOneTest() {
        var list = JsonParse.jsonToObject(
        "[{\"operationType\":\"buy\",\"mediaPonderada\":10.00,\"lucro\":0,\"quantidade\":100,\"saldo\":0,\"totalOperation\":1000.00},{\"operationType\":\"sell\",\"mediaPonderada\":10.00,\"lucro\":250.00,\"quantidade\":50,\"saldo\":250.00,\"totalOperation\":750.00},{\"operationType\":\"sell\",\"mediaPonderada\":10.00,\"lucro\":250.00,\"quantidade\":0,\"saldo\":500.00,\"totalOperation\":750.00}]",
                new TypeReference<List<FinancialTimelineData>>() {}
        );

        assert list != null;

        var result = taxProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0.00"), result.get(0).tax());
        Assert.assertEquals(new BigDecimal("0.00"), result.get(1).tax());
        Assert.assertEquals(new BigDecimal("0.00"), result.get(2).tax());

    }

    @Test
    public void caseTwoTest() {
        var list = JsonParse.jsonToObject(
        "[{\"operationType\":\"buy\",\"mediaPonderada\":10.00,\"lucro\":0,\"quantidade\":10000,\"saldo\":0,\"totalOperation\":100000.00},{\"operationType\":\"sell\",\"mediaPonderada\":10.00,\"lucro\":50000.00,\"quantidade\":5000,\"saldo\":50000.00,\"totalOperation\":100000.00},{\"operationType\":\"sell\",\"mediaPonderada\":10.00,\"lucro\":-25000.00,\"quantidade\":0,\"saldo\":25000.00,\"totalOperation\":25000.00}]",
                new TypeReference<List<FinancialTimelineData>>() {}
        );

        assert list != null;

        var result = taxProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0.00"), result.get(0).tax());
        Assert.assertEquals(new BigDecimal("10000.00"), result.get(1).tax());
        Assert.assertEquals(new BigDecimal("0.00"), result.get(2).tax());
    }

    @Test
    public void caseThreeTest() {
        var list = JsonParse.jsonToObject(
                "[{\"operationType\":\"buy\",\"mediaPonderada\":10.00,\"lucro\":0,\"quantidade\":10000,\"saldo\":0,\"totalOperation\":100000.00},{\"operationType\":\"sell\",\"mediaPonderada\":10.00,\"lucro\":-25000.00,\"quantidade\":5000,\"saldo\":-25000.00,\"totalOperation\":25000.00},{\"operationType\":\"sell\",\"mediaPonderada\":10.00,\"lucro\":30000.00,\"quantidade\":2000,\"saldo\":5000.00,\"totalOperation\":60000.00}]",
                new TypeReference<List<FinancialTimelineData>>() {}
        );

        assert list != null;

        var result = taxProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0.00"), result.get(0).tax());
        Assert.assertEquals(new BigDecimal("0.00"), result.get(1).tax());
        Assert.assertEquals(new BigDecimal("1000.00"), result.get(2).tax());
    }

    @Test
    public void caseEightTest() {
        var list = JsonParse.jsonToObject(
                "[{\"operationType\":\"buy\",\"mediaPonderada\":10.00,\"lucro\":0,\"quantidade\":10000,\"saldo\":0,\"totalOperation\":100000.00},{\"operationType\":\"sell\",\"mediaPonderada\":10.00,\"lucro\":400000.00,\"quantidade\":0,\"saldo\":400000.00,\"totalOperation\":500000.00},{\"operationType\":\"buy\",\"mediaPonderada\":20.00,\"lucro\":0,\"quantidade\":10000,\"saldo\":0,\"totalOperation\":200000.00},{\"operationType\":\"sell\",\"mediaPonderada\":20.00,\"lucro\":300000.00,\"quantidade\":0,\"saldo\":300000.00,\"totalOperation\":500000.00}]",
                new TypeReference<List<FinancialTimelineData>>() {}
        );

        assert list != null;

        var result = taxProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0.00"), result.get(0).tax());
        Assert.assertEquals(new BigDecimal("80000.00"), result.get(1).tax());
        Assert.assertEquals(new BigDecimal("0.00"), result.get(2).tax());
        Assert.assertEquals(new BigDecimal("60000.00"), result.get(3).tax());
    }
}
