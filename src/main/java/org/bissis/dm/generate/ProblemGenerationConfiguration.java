package org.bissis.dm.generate;

import org.bissis.dm.generate.data.ProblemData;
import org.bissis.dm.generate.data.ProblemGenerationConstraint;
import org.bissis.dm.generate.data.ProblemGenerationAttribute;
import org.bissis.dm.generate.data.ProblemGenerationParameter;
import org.bissis.dm.generate.types.ExpressionDataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Java object for the configuration of the problem instance generator.
 * @author Markus Ullrich
 */
public class ProblemGenerationConfiguration {

    private long globalSeed;
    private Random globalRandom;
    private String version = "v0.1"; //TODO: read version from pom? - probably not, since the version should not be utilized
    private List<String> comments; //TODO: ensure that future implementations will generate the same values every time - what about bug fixes?
    private String problemName;
    private boolean noDuplicates;
    private boolean printParameters;
    private long numberOfRows;

    private List<ProblemGenerationAttribute> problemGenerationAttributeList;
    private List<ProblemGenerationParameter> generateParameterList;
    private List<ProblemGenerationConstraint> problemGenerationConstraintList;
    private ProblemData problemData;

    private String separator = ";";
    private String commentPrefix = "#";

    private String alternativeHeader;

    /**
     * Constructor for the problem generator configuration.
     * Takes care of extracting the configuration values from a JSON object.
     * @param jsonConfig - The configuration values in a JSON object.
     */
    @SuppressWarnings("WhileLoopReplaceableByForEach")
    public ProblemGenerationConfiguration(JSONObject jsonConfig) {
        this.problemData = new ProblemData();
        this.version = (String) jsonConfig.get("version");
        Long globalSeed = (Long) jsonConfig.get("seed");
        if(globalSeed != null) {
            this.globalSeed = globalSeed;
            this.globalRandom = new Random(this.globalSeed);
        } else {
            this.globalRandom = new Random();
        }
        this.numberOfRows = (Long) jsonConfig.get("rows");
        this.problemName = (String) jsonConfig.get("problem");
        this.alternativeHeader = (String) jsonConfig.get("alternative_header");
        if(this.problemName == null) {
            this.problemName = "-";
        }
        Boolean noDuplicates = (Boolean) jsonConfig.get("no_duplicates");
        this.noDuplicates = noDuplicates == null ? false : noDuplicates;
        Boolean printParameters = (Boolean) jsonConfig.get("print_parameters");
        this.printParameters = printParameters == null ? false : printParameters;
        this.commentPrefix = (String) jsonConfig.get("comment_prefix");
        if (this.commentPrefix == null) {
            this.commentPrefix = "#";
        }
        this.separator = (String) jsonConfig.get("separator");
        if (this.separator == null) {
            this.separator = ";";
        }
        this.comments = new ArrayList<>();
        JSONArray commentArray = (JSONArray) jsonConfig.get("comments");
        Iterator commentIter = commentArray.iterator();
        while(commentIter.hasNext()) {
            final JSONObject comment = (JSONObject) commentIter.next();
            this.comments.add((String) comment.get("comment"));
        }
        this.generateParameterList = new ArrayList<>();
        JSONArray parameterArray = (JSONArray) jsonConfig.get("parameters");
        Iterator paramIter = parameterArray.iterator();
        while(paramIter.hasNext()) {
            final JSONObject parameter = (JSONObject) paramIter.next();
            this.generateParameterList.add(new ProblemGenerationParameter(parameter));
        }
        this.problemGenerationAttributeList = new ArrayList<>();
        JSONArray attributeArray = (JSONArray) jsonConfig.get("attributes");
        Iterator attrIter = attributeArray.iterator();
        while (attrIter.hasNext()) {
            final JSONObject attribute = (JSONObject) attrIter.next();
            ProblemGenerationAttribute problemGenerationAttribute = new ProblemGenerationAttribute(attribute, this.problemData, this.getGlobalRandom(), this.numberOfRows);
            this.problemData.addDataType(problemGenerationAttribute.getType());
            this.problemGenerationAttributeList.add(problemGenerationAttribute);
        }
        this.problemGenerationConstraintList = new ArrayList<>();
        JSONArray constraintArray = (JSONArray) jsonConfig.get("constraints");
        Iterator constIter = constraintArray.iterator();
        while (constIter.hasNext()) {
            final JSONObject constraint = (JSONObject) constIter.next();
            this.problemGenerationConstraintList.add(new ProblemGenerationConstraint(constraint, this.problemData));
        }
        for (ProblemGenerationAttribute attribute : this.problemGenerationAttributeList) {
            if (attribute.getType() instanceof ExpressionDataType) {
                this.problemGenerationConstraintList.add(new ProblemGenerationConstraint((ExpressionDataType) attribute.getType(), problemData));
            }
        }
    }

    long getGlobalSeed() {
        return globalSeed;
    }

    public String getVersion() {
        return version;
    }

    List<String> getComments() {
        return comments;
    }

    String getProblemName() {
        return problemName;
    }

    public boolean isNoDuplicates() {
        return noDuplicates;
    }

    boolean isPrintParameters() {
        return this.printParameters;
    }

    public long getNumberOfRows() {
        return numberOfRows;
    }

    String getCommentPrefix() {
        return this.commentPrefix;
    }

    String getSeparator (){
        return this.separator;
    }

    List<ProblemGenerationAttribute> getProblemGenerationAttributeList() {
        return problemGenerationAttributeList;
    }

    ProblemData getProblemData() {
        return this.problemData;
    }

    List<ProblemGenerationParameter> getGenerateParameterList() {
        return generateParameterList;
    }

    List<ProblemGenerationConstraint> getProblemGenerationConstraintList() {
        return problemGenerationConstraintList;
    }

    String getAlternativeHeader() {
        return alternativeHeader;
    }

    Random getGlobalRandom() {
        return globalRandom;
    }
}
