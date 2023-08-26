package idlreasonerchoco.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.us.isa.idlreasonerchoco.analyzer.Analyzer;
import es.us.isa.idlreasonerchoco.analyzer.OASAnalyzer;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import idlreasonerchoco.model.OperationAnalysisResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.util.ResolverFully;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.EmptyString;

//import static jdk.internal.logger.DefaultLoggerFinder.SharedLoggers.system;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Tag(name = "IDLReasoner API", description = "IDLReasoner API")
public class IDLReasonerChocoController {

    private static final String SPEC_URL = "specificationUrl";
    private static final String OPERATION_PATH = "operationPath";
    private static final String OPERATION_TYPE = "operationType";
    private static final String PARAMETER = "parameter";
    private static final String NO_VALID_REQUEST_MSG = "There is no valid request for this operation";
    private static final String NO_INVALID_REQUEST_MSG = "There is no invalid request for this operation";


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
            })
    @Operation(summary = "Generate random valid request", description = "Generate random valid request")
    @PostMapping("/generateRandomValidRequest")
    public ResponseEntity<Map<String, String>> generateRandomValidRequest(
            @Parameter(description = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        Map<String, String> response = analyzer.getRandomValidRequest();

        if (response == null) {
            throw new IDLException(NO_VALID_REQUEST_MSG);
        }

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Generates random valid request", description = "Generates a random valid request")
    @GetMapping("/generateRandomValidRequest")
    public ResponseEntity<Map<String, String>> generateRandomValidRequestGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }
        Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType, false);
        Map<String, String> response = analyzer.getRandomValidRequest();

        if (response == null) {
            throw new IDLException(NO_VALID_REQUEST_MSG);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getTest")
    public ResponseEntity<String> getTest() {

        return ResponseEntity.ok("Test");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Generates random invalid request", description = "Generates a random invalid request")
    @PostMapping("/generateRandomInvalidRequest")
    public ResponseEntity<Map<String, String>> generateRandomInvalidRequest(
            @Parameter(description = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        Map<String, String> response = analyzer.getRandomInvalidRequest();

        if (response == null) {
            throw new IDLException(NO_INVALID_REQUEST_MSG);
        }

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Generates random invalid request", description = "Generates a random invalid request")
    @GetMapping("/generateRandomInvalidRequest")
    public ResponseEntity<Map<String, String>> generateRandomInvalidRequestGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }
        Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType, false);
        Map<String, String> response = analyzer.getRandomInvalidRequest();

        if (response == null) {
            throw new IDLException(NO_INVALID_REQUEST_MSG);
        }

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @PostMapping("/isValidRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidRequest(
            @RequestBody String oasSpec, @RequestParam Map<String, String> requestParams) throws IDLException {
        OperationAnalysisResponse response = isValidRequest(oasSpec, requestParams, true, false);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @GetMapping("/isValidRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidRequestGet(@RequestParam Map<String, String> requestParams) throws IDLException {
        if (!requestParams.containsKey(SPEC_URL) || isUrlInvalid(requestParams.get(SPEC_URL))) {
            return ResponseEntity.badRequest().build();
        }
        OperationAnalysisResponse response = isValidRequest(null, requestParams, false, false);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @PostMapping("/isValidPartialRequest")
    public ResponseEntity<OperationAnalysisResponse> isValidPartialRequest(@RequestBody String oasSpec, @RequestParam Map<String, String> requestParams) throws IDLException {
        OperationAnalysisResponse response = isValidRequest(oasSpec, requestParams, true, true);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
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


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Check if the IDL is consistent or not", description = "Returns true if the IDL is consistent. An IDL is consistent if there exists at least one request satisfying all the dependencies of the specification.")
    @PostMapping(value = "/isConsistent",consumes = "application/x-yaml")
    public ResponseEntity<OperationAnalysisResponse> isConsistent(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setConsistent(analyzer.isConsistent());
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Check if the IDL is consistent or not", description = "Returns true if the IDL is consistent. An IDL is consistent if there exists at least one request satisfying all the dependencies of the specification.")
    @GetMapping("/isConsistent")
    public ResponseEntity<OperationAnalysisResponse> isConsistentGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }

        Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setConsistent(analyzer.isConsistent());
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Check if a parameter is dead or not", description = "Returns true if the parameter is dead. A parameter is dead if it cannot be included in any valid call to the service.")
    @PostMapping(value = "/isDeadParameter")
    public ResponseEntity<OperationAnalysisResponse> isDeadParameter(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Parameter name", example = "p1", required = true) @RequestParam(name = PARAMETER) String parameter)
            throws IDLException {

    Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setDeadParameter(analyzer.isDeadParameter(parameter));
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Check if a parameter is dead or not", description = "Returns true if the parameter is dead. A parameter is dead if it cannot be included in any valid call to the service.")
    @GetMapping("/isDeadParameter")
    public ResponseEntity<OperationAnalysisResponse> isDeadParameterGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Parameter name", example = "p1", required = true) @RequestParam(name = PARAMETER) String parameter)
            throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println(oasSpecUrl);
        System.out.println(operationPath);
        System.out.println(operationType);
        System.out.println(parameter);

        Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setDeadParameter(analyzer.isDeadParameter(parameter));
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Check if a parameter is false optional or not", description = "Returns true if the parameter is a false optional. A parameter is false optional if it is required despite being defined as optional.")
    @PostMapping("/isFalseOptional")
    public ResponseEntity<OperationAnalysisResponse> isFalseOptional(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Parameter name", example = "p1", required = true) @RequestParam(name = PARAMETER) String parameter)
            throws IDLException {
        Analyzer analyzer = new OASAnalyzer( oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setFalseOptional(analyzer.isFalseOptional(parameter));
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Check if a parameter is false optional or not", description = "Returns true if the parameter is a false optional. A parameter is false optional if it is required despite being defined as optional.")
    @GetMapping("/isFalseOptional")
    public ResponseEntity<OperationAnalysisResponse> isFalseOptionalGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Parameter name", example = "p1", required = true) @RequestParam(name = PARAMETER) String parameter)
            throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }

        Analyzer analyzer = new OASAnalyzer( oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setFalseOptional(analyzer.isFalseOptional(parameter));
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Check if the specification is valid or not", description = "Returns true if the IDL is valid. An IDL specification is valid if it is consistent and it does not contain any dead or false optional parameters.")
    @PostMapping("/isValidSpecification")
    public ResponseEntity<OperationAnalysisResponse> isValidIDL(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(analyzer.isValidIDL());
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Check if the specification is valid or not", description = "Returns true if the IDL is valid. An IDL specification is valid if it is consistent and it does not contain any dead or false optional parameters.")
    @GetMapping("/isValidSpecification")
    public ResponseEntity<OperationAnalysisResponse> isValidIDLGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        if(isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("URL: " + oasSpecUrl);
        System.out.println("Operation path: " + operationPath);
        System.out.println("Operation type: " + operationType);

        Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(analyzer.isValidIDL());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/operationAnalysis")
    @Operation(summary = "Analyze an operation", description = "Returns analysis result of an operation.")
    public ResponseEntity<OperationAnalysisResponse> operationAnalysis(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {

        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);

        OperationAnalysisResponse response = new OperationAnalysisResponse();

        boolean isValid = analyzer.isValidIDL() && analyzer.isConsistent();

        if(isValid)
            response.setValid(true);
        else
        {
            response.setValid(false);
            response.setAnalysisResult(analyzer.getExplanationMessage(null).replaceAll("\n", "<br>"));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/operationAnalysis")
    @Operation(summary = "Analyze an operation", description = "Returns analysis result of an operation.")
    public ResponseEntity<OperationAnalysisResponse> operationAnalysisGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/saman-barakat/IDLAnalyzer/master/src/test/resources/OAS_test_suite.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {

        if (isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }

        Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType);
        OperationAnalysisResponse response = new OperationAnalysisResponse();

        boolean isValid = analyzer.isValidIDL() && analyzer.isConsistent();

        if(isValid)
            response.setValid(true);
        else
        {
            response.setValid(false);
            response.setAnalysisResult(analyzer.getExplanationMessage(null));
        }

        return ResponseEntity.ok(response);
    }

    private OperationAnalysisResponse isValidRequest(String oasSpec, Map<String, String> requestParams, boolean specAsString, boolean partial) throws IDLException {
        if ((!specAsString && !requestParams.containsKey(SPEC_URL)) || !requestParams.containsKey(OPERATION_PATH) || !requestParams.containsKey(OPERATION_TYPE)) {
            return null;
        }

        Map<String, String> request = requestParams.keySet().stream()
                .filter(x -> x.startsWith("request["))
                .collect(Collectors.toMap(x -> x.replaceAll("^request\\[(.*)]$", "$1"), requestParams::get));
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        Analyzer analyzer = new OASAnalyzer(specAsString ? oasSpec : requestParams.get(SPEC_URL), requestParams.get(OPERATION_PATH),
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

    private Map<String,String>getMap(String str){
        Map<String, String> map = new HashMap<String, String>();

        ObjectMapper mapper = new ObjectMapper();
        try
        {
            map = mapper.readValue(str, new TypeReference<Map<String, String>>(){});

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


}
