import java.util.ArrayList;
import java.util.List;

public class RoutesFactory {
    public List<Route> routes() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(HTTPMethod.HEAD, HTTPRequestURI.INDEX, HTTPVersion.HTTP_1_1));
        routes.add(new Route(HTTPMethod.GET,  HTTPRequestURI.INDEX, HTTPVersion.HTTP_1_1));
        routes.add(new Route(HTTPMethod.PUT,  HTTPRequestURI.FORM, HTTPVersion.HTTP_1_1));
        routes.add(new Route(HTTPMethod.POST, HTTPRequestURI.FORM, HTTPVersion.HTTP_1_1));
        routes.add(new Route(HTTPMethod.OPTIONS, HTTPRequestURI.FORM, HTTPVersion.HTTP_1_1));
        return routes;
    }
}
