import cohesion.CohesionParser;
import cohesion.CohesionScorer;
import cohesion.Cutoff;
import cohesion.entity.Pattern;
import cohesion.entity.Sequence;
import log.entity.Event;
import log.parser.LogParser;
import log.parser.LogParserFactory;
import metrics.Coverage;
import metrics.CumulativeCoverage;
import spmf.executor.SPMFAlgorithmName;
import spmf.executor.SPMFExecutor;
import spmf.parser.SPMFParser;
import spmf.utils.SPMFFormatter;
import utils.PropertyValues;
import utils.Writer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String logFile = args[0];

        LogParser logParser = LogParserFactory.getLogParser(logFile);
        List<Event> events = logParser.parseLogFile(logFile);
        StringBuilder spmfInput = SPMFParser.transformActionsToSPMF(events);

        // Write formatted input with aliases
        writeInputFile(spmfInput);

        // Execute SPMF Algorithm
        SPMFExecutor.runSPMFAlgorithm(SPMFAlgorithmName.BIDE, 10);

        // Parse sequences and itemsets
        CohesionParser cohesionParser = new CohesionParser();
        List<Sequence> sequences = cohesionParser.parseFormattedSequences(PropertyValues.getProperty("spmfFormattedInputFilePath"));
        List<Pattern> patterns = cohesionParser.parsePatterns(PropertyValues.getProperty("spmfOutputFilePath"));

        // Generate scores
        CohesionScorer scorer = new CohesionScorer();
        List<Pattern> scoredPatterns = scorer.scorePattern(patterns, sequences);
        System.out.println("\nAll patterns: ");
        scoredPatterns.forEach(System.out::println);

        double cutOffThreshold = 15;
        List<Pattern> topPatterns = Cutoff.cutPatterns(scoredPatterns, cutOffThreshold);
        System.out.println("\nTop patterns (cut-off = " + cutOffThreshold + "%): ");
        topPatterns.forEach(System.out::println);

        // Find coverage
        Coverage coverage = new CumulativeCoverage();
        coverage.findCumulativeCoveragePerPattern(topPatterns, sequences);
        coverage.printCoverage();
    }

    private static void writeInputFile(StringBuilder data) {
        StringBuilder spmfFormattedInput = SPMFFormatter.formatData(data);
        Writer.writeFile(spmfFormattedInput, PropertyValues.getProperty("spmfFormattedInputFilePath"));
    }
}
