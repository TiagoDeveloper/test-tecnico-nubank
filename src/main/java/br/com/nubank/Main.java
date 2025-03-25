package br.com.nubank;
import br.com.nubank.model.FinancialOperation;
import br.com.nubank.service.OperationProcessingService;
import br.com.nubank.service.TaxProcessingService;
import br.com.nubank.util.JsonParse;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {

                String json = scanner.nextLine();

                if (json.isEmpty()) {
                    break;
                }

                var processOperation = new OperationProcessingService();
                var taxProcessingService = new TaxProcessingService();
                var financialOperations = JsonParse.jsonToObject(json, new TypeReference<List<FinancialOperation>>() {
                });

                assert financialOperations != null;

                var financialTimelineData = processOperation.process(financialOperations);
                var result = taxProcessingService.process(financialTimelineData);

                System.out.println(JsonParse.objectToJson(result));
            }
        } catch (NoSuchElementException e){
            System.exit(0);
        }
    }
}
