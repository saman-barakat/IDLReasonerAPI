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

    private static final String SPEC_URL = "specificationUrl";
    private static final String OPERATION_PATH = "operationPath";
    private static final String OPERATION_TYPE = "operationType";
    private static final String PARAMETER = "parameter";

    @PostMapping("/generateRandomValidRequest")
    public ResponseEntity<Map<String, String>> generateRandomValidRequest(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                          @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, true);
        Map<String, String> response = analyzer.getRandomValidRequest();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generateRandomValidRequest")
    public ResponseEntity<Map<String, String>> generateRandomValidRequestGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                             @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpecUrl, operationPath, operationType, false);
        Map<String, String> response = analyzer.getRandomValidRequest();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generateRandomInvalidRequest")
    public ResponseEntity<Map<String, String>> generateRandomInvalidRequest(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                            @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, true);
        Map<String, String> response = analyzer.getRandomInvalidRequest();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generateRandomInvalidRequest")
    public ResponseEntity<Map<String, String>> generateRandomInvalidRequestGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                               @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpecUrl, operationPath, operationType, false);
        Map<String, String> response = analyzer.getRandomInvalidRequest();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isValidRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidRequest(@RequestBody String oasSpec, @RequestParam Map<String, String> requestParams) throws IDLException {
        if (!requestParams.containsKey(OPERATION_PATH) || !requestParams.containsKey(OPERATION_TYPE)) {
            return ResponseEntity.badRequest().build();
        }

        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(prepareValidRequestValidation(oasSpec, requestParams, true).isValidRequest(requestParams));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isValidRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidRequestGet(@RequestParam Map<String, String> requestParams) throws IDLException {
        if (!requestParams.containsKey(SPEC_URL) || !requestParams.containsKey(OPERATION_PATH) || !requestParams.containsKey(OPERATION_TYPE)) {
            return ResponseEntity.badRequest().build();
        }

        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(prepareValidRequestValidation(requestParams.get(SPEC_URL), requestParams, false).isValidRequest(requestParams));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isValidPartialRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidPartialRequest(@RequestBody String oasSpec, @RequestParam Map<String, String> requestParams) throws IDLException {
        if (!requestParams.containsKey(OPERATION_PATH) || !requestParams.containsKey(OPERATION_TYPE)) {
            return ResponseEntity.badRequest().build();
        }

        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(prepareValidRequestValidation(oasSpec, requestParams, true).isValidPartialRequest(requestParams));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isValidPartialRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidPartialRequestGet(@RequestParam Map<String, String> requestParams) throws IDLException {
        if (!requestParams.containsKey(SPEC_URL) || !requestParams.containsKey(OPERATION_PATH) || !requestParams.containsKey(OPERATION_TYPE)) {
            return ResponseEntity.badRequest().build();
        }

        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(prepareValidRequestValidation(requestParams.get(SPEC_URL), requestParams, false).isValidPartialRequest(requestParams));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isConsistent")
    public ResponseEntity<OperationAnalysisResponse> isConsistent(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                  @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setConsistent(analyzer.isConsistent());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isConsistent")
    public ResponseEntity<OperationAnalysisResponse> isConsistentGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                     @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setConsistent(analyzer.isConsistent());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isDeadParameter")
    public ResponseEntity<OperationAnalysisResponse> isDeadParameter(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                     @RequestParam(name = OPERATION_TYPE) String operationType,
                                                                     @RequestParam(name = PARAMETER) String parameter) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setDeadParameter(analyzer.isDeadParameter(parameter));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isDeadParameter")
    public ResponseEntity<OperationAnalysisResponse> isDeadParameterGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                        @RequestParam(name = OPERATION_TYPE) String operationType,
                                                                        @RequestParam(name = PARAMETER) String parameter) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpecUrl, operationPath, operationType, false);
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

    @GetMapping("/isFalseOptional")
    public ResponseEntity<OperationAnalysisResponse> isFalseOptionalGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                        @RequestParam(name = OPERATION_TYPE) String operationType,
                                                                        @RequestParam(name = PARAMETER) String parameter) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpecUrl, operationPath, operationType, false);
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

    @GetMapping("/isValidSpecification")
    public ResponseEntity<OperationAnalysisResponse> isValidIDLGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                   @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new Analyzer("oas", oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(analyzer.isValidIDL());
        return ResponseEntity.ok(response);
    }

    private Analyzer prepareValidRequestValidation(String oasSpec, Map<String, String> requestParams, boolean specAsString) throws IDLException {
        String operationPath = requestParams.get(OPERATION_PATH);
        String operationType = requestParams.get(OPERATION_TYPE);

        Analyzer analyzer = new Analyzer("oas", oasSpec, operationPath, operationType, specAsString);
        requestParams.remove(OPERATION_PATH);
        requestParams.remove(OPERATION_TYPE);
        return analyzer;
    }
}
