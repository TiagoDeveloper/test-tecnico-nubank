package service;

import br.com.nubank.model.FinancialOperation;
import br.com.nubank.service.OperationProcessingService;
import br.com.nubank.util.JsonParse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class OperationProcessingServiceTest {

    OperationProcessingService operationProcessingService = new OperationProcessingService();


    @Test
    public void caseOneTest() {

        var list = JsonParse.jsonToObject(
                "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 100},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50}]",
                new TypeReference<List<FinancialOperation>>() {}
        );

        assert list != null;

        var result = operationProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0"), result.get(0).lucro());
        Assert.assertEquals(new BigDecimal("250.00"), result.get(1).lucro());
        Assert.assertEquals(new BigDecimal("250.00"), result.get(2).lucro());

    }

    @Test
    public void caseTwoTest() {

        var list = JsonParse.jsonToObject(
                "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}]",
                new TypeReference<List<FinancialOperation>>() {}
        );

        assert list != null;

        var result = operationProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0"), result.get(0).lucro());
        Assert.assertEquals(new BigDecimal("50000.00"), result.get(1).lucro());
        Assert.assertEquals(new BigDecimal("-25000.00"), result.get(2).lucro());

    }

    @Test
    public void caseThreeTest() {

        var list = JsonParse.jsonToObject(
                "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}]",
                new TypeReference<List<FinancialOperation>>() {}
        );

        assert list != null;

        var result = operationProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0"), result.get(0).lucro());
        Assert.assertEquals(new BigDecimal("-25000.00"), result.get(1).lucro());
        Assert.assertEquals(new BigDecimal("30000.00"), result.get(2).lucro());
        Assert.assertEquals(new BigDecimal("5000.00"), result.get(2).saldo());
    }


    @Test
    public void caseFourTest() {

        var list = JsonParse.jsonToObject(
                "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}]",
                new TypeReference<List<FinancialOperation>>() {}
        );

        assert list != null;

        var result = operationProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0"), result.get(0).lucro());
        Assert.assertEquals(new BigDecimal("0"), result.get(1).lucro());
        Assert.assertEquals(new BigDecimal("0.00"), result.get(2).lucro());
        Assert.assertEquals(new BigDecimal("0.00"), result.get(2).saldo());
        Assert.assertEquals(new BigDecimal("15.00"), result.get(2).mediaPonderada());
    }

    @Test
    public void caseFiveTest() {

        var list = JsonParse.jsonToObject(
                "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 5000}]",
                new TypeReference<List<FinancialOperation>>() {}
        );

        assert list != null;

        var result = operationProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0"), result.get(0).lucro());
        Assert.assertEquals(new BigDecimal("0"), result.get(1).lucro());
        Assert.assertEquals(new BigDecimal("15.00"), result.get(2).mediaPonderada());
        Assert.assertEquals(new BigDecimal("15.00"), result.get(3).mediaPonderada());
    }

    @Test
    public void caseSixTest() {

        var list = JsonParse.jsonToObject(
                "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000}]",
                new TypeReference<List<FinancialOperation>>() {}
        );

        assert list != null;

        var result = operationProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0"), result.get(0).lucro());
        Assert.assertEquals(new BigDecimal("-40000.00"), result.get(1).lucro());
        Assert.assertEquals(new BigDecimal("20000.00"), result.get(2).lucro());
        Assert.assertEquals(new BigDecimal("-20000.00"), result.get(2).saldo());
        Assert.assertEquals(new BigDecimal("20000.00"), result.get(3).lucro());
        Assert.assertEquals(new BigDecimal("0.00"), result.get(3).saldo());
        Assert.assertEquals(new BigDecimal("15000.00"), result.get(4).lucro());
    }

    @Test
    public void caseSevenTest() {

        var list = JsonParse.jsonToObject(
                "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000},{\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 4350},{\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 650}]",
                new TypeReference<List<FinancialOperation>>() {}
        );

        assert list != null;

        var result = operationProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0"), result.get(0).lucro());
        Assert.assertEquals(new BigDecimal("-40000.00"), result.get(1).lucro());
        Assert.assertEquals(new BigDecimal("20000.00"), result.get(2).lucro());
        Assert.assertEquals(new BigDecimal("-20000.00"), result.get(2).saldo());
        Assert.assertEquals(new BigDecimal("20000.00"), result.get(3).lucro());
        Assert.assertEquals(new BigDecimal("0.00"), result.get(3).saldo());
        Assert.assertEquals(new BigDecimal("15000.00"), result.get(4).lucro());
    }

    @Test
    public void caseEightTest() {

        var list = JsonParse.jsonToObject(
                "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":50.00, \"quantity\": 10000},{\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":50.00, \"quantity\": 10000}]",
                new TypeReference<List<FinancialOperation>>() {}
        );

        assert list != null;

        var result = operationProcessingService.process(list);

        Assert.assertEquals(new BigDecimal("0"), result.get(0).lucro());
        Assert.assertEquals(new BigDecimal("400000.00"), result.get(1).lucro());
        Assert.assertEquals(new BigDecimal("0"), result.get(2).lucro());
        Assert.assertEquals(new BigDecimal("300000.00"), result.get(3).lucro());
    }

}
