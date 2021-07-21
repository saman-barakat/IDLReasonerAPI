package idlreasonerchoco.controller;

import idlreasonerchoco.analyzer.Analyzer;
import idlreasonerchoco.analyzer.OASAnalyzer;
import idlreasonerchoco.configuration.IDLException;
import idlreasonerchoco.model.OperationAnalysisResponse;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

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
        Analyzer analyzer = new OASAnalyzer("oas", oasSpec, operationPath, operationType, true);
        Map<String, String> response = analyzer.getRandomValidRequest();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generateRandomValidRequest")
    public ResponseEntity<Map<String, String>> generateRandomValidRequestGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath, @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }
        Analyzer analyzer = new OASAnalyzer("oas", oasSpecUrl, operationPath, operationType, false);
        Map<String, String> response = analyzer.getRandomValidRequest();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generateRandomInvalidRequest")
    public ResponseEntity<Map<String, String>> generateRandomInvalidRequest(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                            @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", oasSpec, operationPath, operationType, true);
        Map<String, String> response = analyzer.getRandomInvalidRequest();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generateRandomInvalidRequest")
    public ResponseEntity<Map<String, String>> generateRandomInvalidRequestGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                               @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }
        Analyzer analyzer = new OASAnalyzer("oas", oasSpecUrl, operationPath, operationType, false);
        Map<String, String> response = analyzer.getRandomInvalidRequest();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isValidRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidRequest(@RequestBody String oasSpec, @RequestParam Map<String, String> requestParams) throws IDLException {
        OperationAnalysisResponse response = isValidRequest(oasSpec, requestParams, true, false);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/isValidRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidRequestGet(@RequestParam Map<String, String> requestParams) throws IDLException {
        OperationAnalysisResponse response = isValidRequest(null, requestParams, false, false);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/isValidPartialRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidPartialRequest(@RequestBody String oasSpec, @RequestParam Map<String, String> requestParams) throws IDLException {
        OperationAnalysisResponse response = isValidRequest(oasSpec, requestParams, true, true);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/isValidPartialRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidPartialRequestGet(@RequestParam Map<String, String> requestParams) throws IDLException {
        if (!requestParams.containsKey(SPEC_URL) || isUrlInvalid(requestParams.get(SPEC_URL))) {
            return ResponseEntity.badRequest().build();
        }

        OperationAnalysisResponse response = isValidRequest(null, requestParams, false, true);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/isConsistent")
    public ResponseEntity<OperationAnalysisResponse> isConsistent(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                  @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setConsistent(analyzer.isConsistent());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isConsistent")
    public ResponseEntity<OperationAnalysisResponse> isConsistentGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                     @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }

        Analyzer analyzer = new OASAnalyzer("oas", oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setConsistent(analyzer.isConsistent());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isDeadParameter")
    public ResponseEntity<OperationAnalysisResponse> isDeadParameter(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                     @RequestParam(name = OPERATION_TYPE) String operationType,
                                                                     @RequestParam(name = PARAMETER) String parameter) throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setDeadParameter(analyzer.isDeadParameter(parameter));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isDeadParameter")
    public ResponseEntity<OperationAnalysisResponse> isDeadParameterGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                        @RequestParam(name = OPERATION_TYPE) String operationType,
                                                                        @RequestParam(name = PARAMETER) String parameter) throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }

        Analyzer analyzer = new OASAnalyzer("oas", oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setDeadParameter(analyzer.isDeadParameter(parameter));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isFalseOptional")
    public ResponseEntity<OperationAnalysisResponse> isFalseOptional(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                     @RequestParam(name = OPERATION_TYPE) String operationType,
                                                                     @RequestParam(name = PARAMETER) String parameter) throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", oasSpec, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setFalseOptional(analyzer.isFalseOptional(parameter));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isFalseOptional")
    public ResponseEntity<OperationAnalysisResponse> isFalseOptionalGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                        @RequestParam(name = OPERATION_TYPE) String operationType,
                                                                        @RequestParam(name = PARAMETER) String parameter) throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }

        Analyzer analyzer = new OASAnalyzer("oas", oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setFalseOptional(analyzer.isFalseOptional(parameter));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/isValidSpecification")
    public ResponseEntity<OperationAnalysisResponse> isValidIDL(@RequestBody String oasSpec, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        Analyzer analyzer = new OASAnalyzer("oas", oasSpec, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(analyzer.isValidIDL());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/isValidSpecification")
    public ResponseEntity<OperationAnalysisResponse> isValidIDLGet(@RequestParam(name = SPEC_URL) String oasSpecUrl, @RequestParam(name = OPERATION_PATH) String operationPath,
                                                                   @RequestParam(name = OPERATION_TYPE) String operationType) throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }

        Analyzer analyzer = new OASAnalyzer("oas", oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(analyzer.isValidIDL());
        return ResponseEntity.ok(response);
    }

    private OperationAnalysisResponse isValidRequest(String oasSpec, Map<String, String> requestParams, boolean specAsString, boolean partial) throws IDLException {
        if ((!specAsString && !requestParams.containsKey(SPEC_URL)) || !requestParams.containsKey(OPERATION_PATH) || !requestParams.containsKey(OPERATION_TYPE)
                || requestParams.keySet().stream().noneMatch(x -> x.startsWith("request["))) {
            return null;
        }

        Map<String, String> request = requestParams.keySet().stream()
                .filter(x -> x.startsWith("request["))
                .collect(Collectors.toMap(x -> x.replaceAll("^request\\[(.*)]$", "$1"), requestParams::get));
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        Analyzer analyzer = new OASAnalyzer("oas", specAsString ? oasSpec : requestParams.get(SPEC_URL), requestParams.get(OPERATION_PATH),
                requestParams.get(OPERATION_TYPE), specAsString);

        if (partial) {
            response.setValid(analyzer.isValidPartialRequest(request));
        } else {
            response.setValid(analyzer.isValidRequest(request));
        }

        return response;
    }

    private boolean isUrlInvalid(String oasSpecUrl) {
        UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS + UrlValidator.ALLOW_2_SLASHES + UrlValidator.ALLOW_ALL_SCHEMES);
        return !validator.isValid(oasSpecUrl);
    }
}
