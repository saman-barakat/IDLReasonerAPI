package idlreasoner.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import es.us.isa.idlreasonerchoco.analyzer.Analyzer;
import es.us.isa.idlreasonerchoco.analyzer.OASAnalyzer;
import es.us.isa.idlreasonerchoco.configuration.IDLException;
import idlreasoner.advice.ErrorResponse;
import idlreasoner.model.OperationAnalysisResponse;
import io.swagger.models.Swagger;
import io.swagger.parser.OpenAPIParser;
import io.swagger.parser.SwaggerParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.converter.SwaggerConverter;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//import static jdk.internal.logger.DefaultLoggerFinder.SharedLoggers.system;

@CrossOrigin
@RestController
@RequestMapping("/api")
@Tag(name = "IDLReasoner API", description = "IDLReasoner API")
public class IDLReasonerController {

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
    public ResponseEntity<?> generateRandomValidRequest(
            @Parameter(description = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        try {
            if (isSwagger2(oasSpec)) {
                oasSpec = convertSwaggerToOpenAPI(oasSpec);
            }

        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        Map<String, String> response = analyzer.getRandomValidRequest();

        if (response == null) {
            throw new IDLException(NO_VALID_REQUEST_MSG);
        }

        return ResponseEntity.ok(response);

        } catch (IDLException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IDLException", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Processing Error", e.getMessage()));
        }
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Generates random invalid request", description = "Generates a random invalid request")
    @PostMapping("/generateRandomInvalidRequest")
    public ResponseEntity<?> generateRandomInvalidRequest(
            @Parameter(description = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        try {
            if (isSwagger2(oasSpec)) {
                oasSpec = convertSwaggerToOpenAPI(oasSpec);
            }

        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        Map<String, String> response = analyzer.getRandomInvalidRequest();

        if (response == null) {
            throw new IDLException(NO_INVALID_REQUEST_MSG);
        }

        return ResponseEntity.ok(response);

        } catch (IDLException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IDLException", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Processing Error", e.getMessage()));
        }
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
    @Operation(summary = "Check if the request is valid or not", description = "Returns true if the request is valid. A request is valid if it satisfies all the dependencies of the IDL specification.")
    public ResponseEntity<?> isValidRequest(
            @Parameter(description = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyOnlyOne", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Request parameters", example = "{\"p1\":\"false\",\"p3\":100}", required = false) @RequestParam(required = false) String requestParameters)
            throws IDLException {
        try {
            if (isSwagger2(oasSpec)) {
                oasSpec = convertSwaggerToOpenAPI(oasSpec);
            }

        Map<String, String> request = new HashMap<>();

        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();

        if(requestParameters != null)
            request = getMap(requestParameters);

        response.setValid(analyzer.isValidRequest(request));
        return ResponseEntity.ok(response);

        } catch (IDLException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IDLException", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Processing Error", e.getMessage()));
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @GetMapping("/isValidRequest")
    @Operation(summary = "Check if the request is valid or not", description = "Returns true if the request is valid. A request is valid if it satisfies all the dependencies of the IDL specification.")
    public ResponseEntity<OperationAnalysisResponse> isValidRequestGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyOnlyOne", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Request parameters", example = "{\"p1\":\"false\",\"p3\":100}", required = false) @RequestParam(required = false) String requestParameters)
            throws IDLException {
        if (isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }
        Map<String, String> request = new HashMap<>();

        Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();

        if(requestParameters != null)
            request = getMap(requestParameters);

        response.setValid(analyzer.isValidRequest(request));
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @PostMapping("/isValidPartialRequest")
    @Operation(summary = "Check if the request is partially valid or not", description = " Returns true if the request is partially valid. A request is partially valid means that some other parameters should still be included to make it a full valid request.")
    public ResponseEntity<?> isValidPartialRequest(
            @Parameter(description = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyOnlyOne", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Request parameters", example = "{\"p1\":\"false\",\"p3\":100}", required = false) @RequestParam(required = false) String requestParameters)
            throws IDLException {
        try {
            if (isSwagger2(oasSpec)) {
                oasSpec = convertSwaggerToOpenAPI(oasSpec);
            }
        Map<String, String> request = new HashMap<>();

        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();

        if(requestParameters != null)
            request = getMap(requestParameters);

        response.setValid(analyzer.isValidPartialRequest(request));
        return ResponseEntity.ok(response);

        } catch (IDLException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IDLException", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Processing Error", e.getMessage()));
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @GetMapping("/isValidPartialRequest")
    @Operation(summary = "Check if the request is partially valid or not", description = " Returns true if the request is partially valid. A request is partially valid means that some other parameters should still be included to make it a full valid request.")
    public ResponseEntity<OperationAnalysisResponse> isValidPartialRequestGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyOnlyOne", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Request parameters", example = "{\"p1\":\"false\",\"p3\":100}", required = false) @RequestParam(required = false) String requestParameters)
            throws IDLException {
        if (isUrlInvalid(oasSpecUrl)) {
            return ResponseEntity.badRequest().build();
        }
        Map<String, String> request = new HashMap<>();

        Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();

        if(requestParameters != null)
            request = getMap(requestParameters);

        response.setValid(analyzer.isValidRequest(request));
        return ResponseEntity.ok(response);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Failure operation")
    })
    @Operation(summary = "Check if the IDL is consistent or not", description = "Returns true if the IDL is consistent. An IDL is consistent if there exists at least one request satisfying all the dependencies of the specification.")
    @PostMapping(value = "/isConsistent",consumes = "application/x-yaml")
    public ResponseEntity<?> isConsistent(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        try {
            if (isSwagger2(oasSpec)) {
                oasSpec = convertSwaggerToOpenAPI(oasSpec);
            }

        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setConsistent(analyzer.isConsistent());
        return ResponseEntity.ok(response);

        } catch (IDLException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IDLException", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Processing Error", e.getMessage()));
        }
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
    public ResponseEntity<?> isDeadParameter(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Parameter name", example = "p1", required = true) @RequestParam(name = PARAMETER) String parameter)
            throws IDLException {
        try {
            if (isSwagger2(oasSpec)) {
                oasSpec = convertSwaggerToOpenAPI(oasSpec);
            }

        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setDeadParameter(analyzer.isDeadParameter(parameter));
        return ResponseEntity.ok(response);

        } catch (IDLException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IDLException", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Processing Error", e.getMessage()));
        }

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
    public ResponseEntity<?> isFalseOptional(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType,
            @Parameter(description = "Parameter name", example = "p1", required = true) @RequestParam(name = PARAMETER) String parameter)
            throws IDLException {
        try {
            if (isSwagger2(oasSpec)) {
                oasSpec = convertSwaggerToOpenAPI(oasSpec);
            }

            Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
            OperationAnalysisResponse response = new OperationAnalysisResponse();
            response.setFalseOptional(analyzer.isFalseOptional(parameter));

            return ResponseEntity.ok(response);
        } catch (IDLException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IDLException", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Processing Error", e.getMessage()));
        }
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
    public ResponseEntity<?> isValidIDL(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        try {
            if (isSwagger2(oasSpec)) {
                oasSpec = convertSwaggerToOpenAPI(oasSpec);
            }

        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(analyzer.isValidIDL());
        return ResponseEntity.ok(response);

        } catch (IDLException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IDLException", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Processing Error", e.getMessage()));
        }
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

        Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType, false);
        OperationAnalysisResponse response = new OperationAnalysisResponse();
        response.setValid(analyzer.isValidIDL());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/operationAnalysis")
    @Operation(summary = "Analyze an operation", description = "Returns analysis result of an operation.")
    public ResponseEntity<?> operationAnalysis(
            @Parameter(description = "Open API specification", example = "Open API specification", required = true) @RequestBody String oasSpec,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {

        try {
            if (isSwagger2(oasSpec)) {
                oasSpec = convertSwaggerToOpenAPI(oasSpec);
            }

        Analyzer analyzer = new OASAnalyzer(oasSpec, operationPath, operationType, true);

        OperationAnalysisResponse response = new OperationAnalysisResponse();

        boolean isValid = analyzer.isValidIDL() && analyzer.isConsistent();

        if(isValid) {
            response.setValid(true);
        }
        else
        {
            response.setValid(false);
            response.setAnalysisResult(analyzer.getExplanationMessage(null).replaceAll("\n", "<br>"));
        }

        return ResponseEntity.ok(response);

        } catch (IDLException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IDLException", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Processing Error", e.getMessage()));
        }
    }

    @GetMapping("/operationAnalysis")
    @Operation(summary = "Analyze an operation", description = "Returns analysis result of an operation.")
    public ResponseEntity<OperationAnalysisResponse> operationAnalysisGet(
            @Parameter(description = "Open API specification URL", example = "https://raw.githubusercontent.com/isa-group/IDLReasonerChoco/master/src/test/resources/OAS_test_suite_orig.yaml", required = true) @RequestParam(name = SPEC_URL) String oasSpecUrl,
            @Parameter(description = "Operation path", example = "/oneDependencyRequires", required = true) @RequestParam(name = OPERATION_PATH) String operationPath,
            @Parameter(description = "Operation type", example = "GET", required = true) @RequestParam(name = OPERATION_TYPE) String operationType)
            throws IDLException {
        try {

            if (isUrlInvalid(oasSpecUrl)) {
                return ResponseEntity.badRequest().build();
            }

            Analyzer analyzer = new OASAnalyzer(oasSpecUrl, operationPath, operationType);
            OperationAnalysisResponse response = new OperationAnalysisResponse();

            boolean isValid = analyzer.isValidIDL() && analyzer.isConsistent();

            if (isValid)
                response.setValid(true);
            else {
                response.setValid(false);
                response.setAnalysisResult(analyzer.getExplanationMessage(null));
            }
            return ResponseEntity.ok(response);
        } catch (Exception e){
            OperationAnalysisResponse response = new OperationAnalysisResponse();
            response.setValid(false);
            response.setAnalysisResult(e.getMessage());
            return ResponseEntity.ok(response);
        }
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

    private boolean isValidOAS(String oasSpec, boolean isUrl) {
        try {
            OpenAPI openapi;

            ParseOptions parseOptions = new ParseOptions();
            parseOptions.setResolve(true);           // Resolve $ref references
            parseOptions.setResolveFully(true);      // Resolve all sub-references
            parseOptions.setResolveCombinators(true);// Resolve anyOf, allOf, oneOf

            // Check if the input is a URL or a document string based on the parameter
            if (isUrl) {
                openapi = new OpenAPIV3Parser().read(oasSpec, null, parseOptions);
            } else {
               openapi = new OpenAPIV3Parser().readContents(oasSpec, null, parseOptions).getOpenAPI();
            }
            if(openapi == null)
                return false;
            System.out.println("Successfully rerere-parsed the OpenAPI document." + openapi.getInfo().getTitle());

            return true;

        } catch (Exception e) {
            System.err.println("Cannot parse the OAS document: " + e.getMessage());
            return false;
        }
    }

    private String convertSwaggerToOpenAPI(String oasSpec) throws Exception{

            // Parse the Swagger 2.0 document
            Swagger swagger = new SwaggerParser().parse(oasSpec);
            if (swagger == null) {
                throw new IllegalArgumentException("Failed to parse the Swagger 2.0 document.");
            }

            // Convert Swagger 2.0 to OpenAPI 3.0
            OpenAPI openAPI = new SwaggerConverter().readContents(oasSpec, null, null).getOpenAPI();
            if (openAPI == null) {
                throw new IllegalStateException("Conversion failed: Unable to convert Swagger to OpenAPI.");
            }

            // Serialize the OpenAPI object into YAML format
            return serializeToYaml(openAPI);
    }

    private String serializeToYaml(OpenAPI openAPI) throws Exception {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Ignore null values
        return yamlMapper.writeValueAsString(openAPI);
    }

    private boolean isSwagger2(String spec) {
        return spec.contains("\"swagger\": \"2.0\"") || spec.contains("swagger: \"2.0\"");
    }

}
