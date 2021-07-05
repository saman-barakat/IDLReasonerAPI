package idlreasonerchoco.controller;

import idlreasonerchoco.analyzer.Analyzer;
import idlreasonerchoco.configuration.model.IDLException;
import idlreasonerchoco.model.OperationAnalysisResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class IDLReasonerChocoController {

    private static final String OPERATION_PATH = "operationPath";
    private static final String OPERATION_TYPE = "operationType";
    private static final String PARAMETER = "parameter";

    @PostMapping("/generateRandomRequest")
    public ResponseEntity<Map<String, String>> generateRandomRequest(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                   @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, false);
        Map<String, String> response = analyzer.getRandomRequest();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isValidRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidRequest(@RequestBody String oasSpec, @RequestParam Map<String, String> requestParams) throws IDLException {
        if (!requestParams.containsKey(OPERATION_PATH) || !requestParams.containsKey(OPERATION_TYPE)) {
            return ResponseEntity.badRequest().build();
        }

        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(prepareValidRequestValidation(oasSpec, requestParams).isValidRequest(requestParams));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isValidPartialRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidPartialRequest(@RequestBody String oasSpec, @RequestParam Map<String, String> requestParams) throws IDLException {
        if (!requestParams.containsKey(OPERATION_PATH) || !requestParams.containsKey(OPERATION_TYPE)) {
            return ResponseEntity.badRequest().build();
        }

        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(prepareValidRequestValidation(oasSpec, requestParams).isValidPartialRequest(requestParams));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isConsistent")
    public ResponseEntity<OperationAnalysisResponse> isConsistent(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                  @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setConsistent(analyzer.isConsistent());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isDeadParameter")
    public ResponseEntity<OperationAnalysisResponse> isDeadParameter(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                     @RequestParam(name = OPERATION_TYPE) String operationType,
                                                                     @RequestParam(name = PARAMETER) String parameter) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setDeadParameter(analyzer.isDeadParameter(parameter));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isFalseOptional")
    public ResponseEntity<OperationAnalysisResponse> isFalseOptional(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                     @RequestParam(name = OPERATION_TYPE) String operationType,
                                                                     @RequestParam(name = PARAMETER) String parameter) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setFalseOptional(analyzer.isFalseOptional(parameter));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isValidSpecification")
    public ResponseEntity<OperationAnalysisResponse> isValidIDL(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(analyzer.isValidIDL());
        return ResponseEntity.ok(response);
    }

    private Analyzer prepareValidRequestValidation(String oasSpec, Map<String, String> requestParams) throws IDLException {
        String operationPath = requestParams.get(OPERATION_PATH);
        String operationType = requestParams.get(OPERATION_TYPE);

        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, false);
        requestParams.remove(OPERATION_PATH);
        requestParams.remove(OPERATION_TYPE);
        return analyzer;
    }
}
