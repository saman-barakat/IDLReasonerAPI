package idlreasonerchoco.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class IDLReasonerChocoControllerTests {

    private static final String SPEC_URL = "specificationUrl";
    private static final String OPERATION_PATH = "operationPath";
    private static final String OPERATION_TYPE = "operationType";
    private static final String PARAMETER = "parameter";
    private static final String NO_VALID_REQUEST_MSG = "There is no valid request for this operation";
    private static final String NO_INVALID_REQUEST_MSG = "There is no invalid request for this operation";

    @Autowired
    private MockMvc mockMvc;

    // ############## RANDOM VALID REQUEST GENERATION TESTS ##############

    @Test
    void generateRandomValidRequestGetSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "https://raw.githubusercontent.com/josferde5/IDLReasoner-choco-API/develop/src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/generateRandomValidRequest").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.anEmptyMap()));
    }

    @Test
    void generateRandomValidRequestGetBadUrl() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "./src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/generateRandomValidRequest").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void generateRandomValidRequestPostSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/generateRandomValidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.anEmptyMap()));
    }

    @Test
    void generateRandomValidRequestPostSuccessfulNoDeps() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamBoolean");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/generateRandomValidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()));
    }

    @Test
    void generateRandomValidRequestPostSuccessfulComplex() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneDependencyComplex");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/generateRandomValidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.aMapWithSize(Matchers.greaterThan(0))));
    }

    // ############## RANDOM INVALID REQUEST GENERATION TESTS ##############

    @Test
    void generateRandomInvalidRequestGetNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "https://raw.githubusercontent.com/josferde5/IDLReasoner-choco-API/develop/src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/generateRandomInvalidRequest").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(NO_INVALID_REQUEST_MSG)));
    }

    @Test
    void generateRandomInvalidRequestGetBadUrl() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "./src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/generateRandomInvalidRequest").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void generateRandomInvalidRequestPostNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/generateRandomInvalidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(NO_INVALID_REQUEST_MSG)));
    }

    @Test
    void generateRandomInvalidRequestPostNoDeps() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamString");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/generateRandomInvalidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.anEmptyMap()));
    }

    @Test
    void generateRandomInvalidRequestPostComplex() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamString");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/generateRandomInvalidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()));
    }

    // ############## DEAD PARAMETER TESTS ##############

    @Test
    void deadParameterGetSuccessfulNoDeps() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "https://raw.githubusercontent.com/josferde5/IDLReasoner-choco-API/develop/src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/oneParamString");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p1");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isDeadParameter").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadParameter", Matchers.equalTo(false)));
    }

    @Test
    void deadParameterGetBadUrl() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "./src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/oneParamString");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p1");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isDeadParameter").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deadParameterPostSuccessfulNoDeps() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamString");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isDeadParameter").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadParameter", Matchers.equalTo(false)));
    }

    @Test
    void deadParameterPostSuccessfulRequires() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneDependencyRequires");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isDeadParameter").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadParameter", Matchers.equalTo(false)));
    }

    @Test
    void deadParameterPostSuccessfulParameterIsDead() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/combinatorial2");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p2");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isDeadParameter").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadParameter", Matchers.equalTo(true)));
    }

    // ############## FALSE OPTIONAL TESTS ##############

    @Test
    void falseOptionalGetSuccessfulNoDeps() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "https://raw.githubusercontent.com/josferde5/IDLReasoner-choco-API/develop/src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/oneParamString");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p1");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isFalseOptional").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.falseOptional", Matchers.equalTo(false)));
    }

    @Test
    void falseOptionalGetBadUrl() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "./src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/oneParamString");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p1");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isFalseOptional").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void falseOptionalPostSuccessfulNoDeps() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamString");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isFalseOptional").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.falseOptional", Matchers.equalTo(false)));
    }

    @Test
    void falseOptionalPostSuccessfulOr() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneDependencyOr");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isFalseOptional").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.falseOptional", Matchers.equalTo(false)));
    }

    @Test
    void falseOptionalPostSuccessfulParameterIsFalseOptional() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/combinatorial2");
        query.add(OPERATION_TYPE, "get");
        query.add(PARAMETER, "p1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isFalseOptional").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.falseOptional", Matchers.equalTo(true)));
    }

    // ############## VALID SPECIFICATION TESTS ##############

    @Test
    void validIDLGetSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "https://raw.githubusercontent.com/josferde5/IDLReasoner-choco-API/develop/src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isValidSpecification").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validIDLGetBadUrl() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "./src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isValidSpecification").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void validIDLPostSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidSpecification").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validIDLPostSuccessfulNoDeps() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamInt");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidSpecification").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validIDLPostSuccessfulOnlyOne() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneDependencyOnlyOne");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidSpecification").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validIDLPostSuccessfulInvalidSpec() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/combinatorial10");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidSpecification").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(false)));
    }

    // ############## VALID REQUEST TESTS ##############

    @Test
    void validRequestGetSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "https://raw.githubusercontent.com/josferde5/IDLReasoner-choco-API/develop/src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isValidRequest").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validRequestGetBadUrl() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "./src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isValidRequest").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void validRequestPostSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validRequestPostSuccessfulNoDepsValid() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamEnumString");
        query.add(OPERATION_TYPE, "get");
        query.add("request[p1]", "value1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validRequestPostSuccessfulNoDepsInvalid() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamEnumString");
        query.add(OPERATION_TYPE, "get");
        query.add("request[p1]", "string not in enum alternatives");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(false)));
    }

    @Test
    void validRequestPostSuccessfulAllOrNoneValid() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneDependencyAllOrNone");
        query.add(OPERATION_TYPE, "get");
        query.add("request[p3]", "-5");
        query.add("request[p4]", "value5");
        query.add("request[p5]", "1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validRequestPostSuccessfulAllOrNoneInvalid() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneDependencyAllOrNone");
        query.add(OPERATION_TYPE, "get");
        query.add("request[p1]", "true");
        query.add("request[p3]", "10");
        query.add("request[p4]", "value5");
        query.add("request[p5]", "1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(false)));
    }

    // ############## PARTIAL VALID REQUEST TESTS ##############

    @Test
    void validPartialRequestGetSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "https://raw.githubusercontent.com/josferde5/IDLReasoner-choco-API/develop/src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isValidPartialRequest").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validPartialRequestGetBadUrl() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "./src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isValidPartialRequest").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void validPartialRequestPostSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidPartialRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validPartialRequestPostSuccessfulNoDepsValid() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamInt");
        query.add(OPERATION_TYPE, "get");
        query.add("request[p1]", "1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidPartialRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validPartialRequestPostSuccessfulNoDepsInvalid() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamInt");
        query.add(OPERATION_TYPE, "get");
        query.add("request[p1]", "not an integer");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidPartialRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(false)));
    }

    @Test
    void validPartialRequestPostSuccessfulZeroOrOneValid() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneDependencyZeroOrOne");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidPartialRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(true)));
    }

    @Test
    void validPartialRequestPostSuccessfulZeroOrOneInvalid() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneDependencyZeroOrOne");
        query.add(OPERATION_TYPE, "get");
        query.add("request[p1]", "true");
        query.add("request[p2]", "1");
        query.add("request[p3]", "1");
        query.add("request[p4]", "1");
        query.add("request[p5]", "1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isValidPartialRequest").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Matchers.equalTo(false)));
    }

    // ############## CONSISTENT TESTS ##############

    @Test
    void consistentRequestGetSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "https://raw.githubusercontent.com/josferde5/IDLReasoner-choco-API/develop/src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isConsistent").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.consistent", Matchers.equalTo(true)));
    }

    @Test
    void consistentRequestGetBadUrl() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(SPEC_URL, "./src/test/resources/OAS_test_suite.yaml");
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/isConsistent").queryParams(query))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void consistentRequestPostSuccessfulNoParams() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/noParams");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isConsistent").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.consistent", Matchers.equalTo(true)));
    }

    @Test
    void consistentRequestPostSuccessfulNoDeps() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneParamEnumInt");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isConsistent").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.consistent", Matchers.equalTo(true)));
    }

    @Test
    void consistentRequestPostSuccessfulArithRel() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/oneDependencyArithRel");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isConsistent").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.consistent", Matchers.equalTo(true)));
    }

    @Test
    void consistentRequestPostSuccessfulInconsistent() throws Exception {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add(OPERATION_PATH, "/combinatorial10");
        query.add(OPERATION_TYPE, "get");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/isConsistent").queryParams(query)
                .contentType("application/x-yaml")
                .content(Files.lines(Paths.get("./src/test/resources/OAS_test_suite.yaml")).collect(Collectors.joining(System.lineSeparator()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.consistent", Matchers.equalTo(false)));
    }

}
