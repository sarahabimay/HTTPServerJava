package functions;

import routeActions.RouteAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class FunctionHelpers {
    public static Function<RouteAction, List<RouteAction>> insertToList = ra -> new ArrayList<>(Arrays.asList(ra));
}
