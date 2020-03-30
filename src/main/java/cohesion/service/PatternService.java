package cohesion.service;

import cohesion.entity.Pattern;
import foofah.FoofahService;
import log.entity.Event;

import java.util.List;
import java.util.Map;

public class PatternService {

    private ItemsDependencyService dependencyService;
    private FoofahService foofahService;

    public PatternService() {
        dependencyService = new ItemsDependencyService();
        foofahService = new FoofahService();
    }

    public void setTransformations(Map<String, List<Event>> cases, Pattern pattern) {
        var transformations = foofahService.findTransformations(cases, pattern);
        pattern.setTransformations(transformations);
    }

    public void setDependencies(Map<String, List<Event>> cases, Pattern pattern) {
        var dependencies = dependencyService.findDependencies(cases, pattern);
        pattern.setItemsDependencies(dependencies);
    }

    public void setAutomatability(Pattern pattern) {
        if (pattern.getTransformations() == null || pattern.getTransformations().values().stream().noneMatch(t -> t.equals(""))) {
            pattern.setAutomatable(true);
        } else if (pattern.getItemsDependencies().stream().anyMatch(d -> d.getDependeeValuesPerDepender().isEmpty())) {
            pattern.setAutomatable(false);
        } else {
            pattern.setAutomatable(true);
        }
    }
}
